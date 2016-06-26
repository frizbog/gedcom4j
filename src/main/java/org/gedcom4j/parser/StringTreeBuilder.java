package org.gedcom4j.parser;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.List;

import org.gedcom4j.io.GedcomFileReader;
import org.gedcom4j.model.StringTree;

/**
 * Class for building the big StringTree representing the input file, so it can be parsed and loaded into the object
 * model
 * 
 * @author frizbog
 */
public class StringTreeBuilder {

    /**
     * Trim all whitespace off the left side (only) of the supplied string.
     * 
     * @param line
     *            the string to trim left leading whitespace from
     * @return the line passed in with the leading whitespace removed. If the original string passed in was null, null
     *         is returned here.
     */
    static String leftTrim(String line) {
        if (line == null) {
            return null;
        }
        if (line == "") {
            return "";
        }
        for (int i = 0; i < line.length(); i++) {
            if (!Character.isWhitespace(line.charAt(i))) {
                return line.substring(i);
            }
        }
        return "";
    }

    /**
     * The buffered input stream we are reading from
     */
    private final BufferedInputStream inputStream;

    /**
     * The list of errors we might add new error messages to
     */
    private final List<String> errors;

    /**
     * The list of warnings we might add new warnings to
     */
    private final List<String> warnings;

    /**
     * Whether we require strict line breaks to be observed, or whether we will try and interpret line breaks as if they
     * had been properly escaped with CONC/CONT tags
     */
    private final boolean strictLineBreaks;

    private boolean beginsWithLevelAndSpace;

    private StringTree stringTreeForCurrentFileLine;

    /**
     * Constructor
     * 
     * @param inputStream
     *            The buffered input stream we are reading from
     * @param errors
     *            The list of errors we might add new error messages to
     * @param warnings
     *            The list of warnings we might add new warnings to
     * @param strictLineBreaks
     *            Whether we require strict line breaks to be observed, or whether we will try and interpret line breaks
     *            as if they had been properly escaped with CONC/CONT tags
     */
    public StringTreeBuilder(BufferedInputStream inputStream, List<String> errors, List<String> warnings, boolean strictLineBreaks) {
        this.inputStream = inputStream;
        this.errors = errors;
        this.warnings = warnings;
        this.strictLineBreaks = strictLineBreaks;
    }

    /**
     * Read data from an {@link java.io.InputStream} and construct a {@link StringTree} object from its contents
     * 
     * @return the {@link StringTree} created from the contents of the input stream
     * @throws IOException
     *             if there is a problem reading the data from the reader
     * @throws GedcomParserException
     *             if there is an error with parsing the data from the stream
     */
    StringTree makeStringTreeFromStream() throws IOException, GedcomParserException {
        List<String> lines = new GedcomFileReader(inputStream).getLines();
        StringTree entireStringTree = new StringTree();
        entireStringTree.level = -1;
        StringTree lastAdded = null;
        try {
            for (int lineNum = 1; lineNum <= lines.size(); lineNum++) {
                stringTreeForCurrentFileLine = new StringTree();
                stringTreeForCurrentFileLine.lineNum = lineNum;

                String line = lines.get(lineNum - 1);
                line = leftTrim(line); // See issue 57

                beginsWithLevelAndSpace = false;
                try {
                    // Probably sets it to true, but might not for a non-standard file - see Issue 100
                    beginsWithLevelAndSpace = startsWithLevelAndSpace(line, lineNum);
                } catch (GedcomParserException e) {
                    if (strictLineBreaks) {
                        throw e;
                    }
                }

                if (beginsWithLevelAndSpace) {
                    LinePieces lp = new LinePieces(line, lineNum);
                    stringTreeForCurrentFileLine.level = lp.level;
                    stringTreeForCurrentFileLine.id = (lp.id == null ? null : lp.id.intern());
                    stringTreeForCurrentFileLine.tag = lp.tag.intern();
                    stringTreeForCurrentFileLine.value = lp.remainder;
                    StringTree addTo;
                    addTo = findPlaceToAttachNode(entireStringTree);
                    if (addTo != null) {
                        addTo.children.add(stringTreeForCurrentFileLine);
                        stringTreeForCurrentFileLine.parent = addTo;
                    }
                    lastAdded = stringTreeForCurrentFileLine;
                } else {
                    // Doesn't begin with a level number followed by a space, and we don't have strictLineBreaks
                    // required, so it's probably meant to be a continuation of the previous text value.
                    if (lastAdded != null) {
                        // Try to add as a CONT line to previous node, as if the file had been properly escaped
                        stringTreeForCurrentFileLine.level = lastAdded.level + 1;
                        stringTreeForCurrentFileLine.tag = Tag.CONTINUATION.tagText;
                        stringTreeForCurrentFileLine.value = line;
                        stringTreeForCurrentFileLine.parent = lastAdded;
                        lastAdded.children.add(stringTreeForCurrentFileLine);
                        warnings.add("Line " + lineNum + " did not begin with a level and tag, so it was treated as a "
                                + "non-standard continuation of the previous line.");
                    } else {
                        warnings.add("Line " + lineNum + " did not begin with a level and tag, so it was discarded.");
                    }
                }
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return entireStringTree;
    }

    /**
     * Find the last item at the supplied level in the supplied tree, so we can find a parent node to hang the current
     * node on. Uses recursion to look for the latest child item of the latest child item (etc) of the root of the tree.
     * 
     * @param lookInSubtree
     *            the tree (or portion thereof) we want to look through for a place to attach currentNode
     * 
     * @return the last item at the supplied level in the supplied tree
     * @throws GedcomParserException
     *             if there is an issue with level numbers not being consistent
     */
    private StringTree findPlaceToAttachNode(StringTree lookInSubtree) throws GedcomParserException {
        if (lookInSubtree.level == (stringTreeForCurrentFileLine.level - 1)) {
            // If the level we're looking in is the level we want, we're done
            return lookInSubtree;
        }
        if (lookInSubtree.children.isEmpty()) {
            // If the tree we're looking for has no children, we can't proceed
            errors.add(stringTreeForCurrentFileLine.tag + " tag at line " + stringTreeForCurrentFileLine.lineNum
                    + ": Unable to find suitable parent node at level " + (stringTreeForCurrentFileLine.level - 1) + " under " + lookInSubtree);
            return null;
        }
        // Get the most recently added item in the tree we're looking through
        StringTree lastChild = lookInSubtree.children.get(lookInSubtree.children.size() - 1);

        if (lastChild.level == (stringTreeForCurrentFileLine.level - 1)) {
            // that child is at the level we want, so return it
            return lastChild;
        }

        // Found the most recently loaded node, but the level isn't right, so recurse into
        return findPlaceToAttachNode(lastChild);
    }

    /**
     * Does this line start with a 1-2 digit level number and a space?
     * 
     * @param line
     *            the line being read
     * @param lineNum
     *            the line number being read
     * @return true if and only if the line begins with a 1-2 digit level number followed by a space
     * @throws GedcomParserException
     *             if the line does not begin with a 1-2 digit number followed by a space
     */
    private boolean startsWithLevelAndSpace(String line, int lineNum) throws GedcomParserException {

        try {
            char c1 = line.charAt(0);
            char c2 = line.charAt(1);
            char c3 = line.charAt(2);

            if (Character.isDigit(c1)) {
                if (' ' == c2) {
                    return true;
                } else if (Character.isDigit(c2) && ' ' == c3) {
                    return true;
                } else {
                    throw new GedcomParserException(
                            "Line " + lineNum + " does not begin with a 1 or 2 digit number for the level followed by a space: " + line);
                }
            } else {
                throw new GedcomParserException("Line " + lineNum + " does not begin with a 1 or 2 digit number for the level followed by a space: " + line);
            }
        } catch (IndexOutOfBoundsException e) {
            throw new GedcomParserException("Line " + lineNum + " does not begin with a 1 or 2 digit number for the level followed by a space: " + line);
        }

    }

}
