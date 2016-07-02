package org.gedcom4j.parser;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.gedcom4j.exception.GedcomParserException;
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
        if (line.length() == 0) {
            return "";
        }
        if (!Character.isWhitespace(line.charAt(0))) {
            return line;
        }
        for (int i = 0; i < line.length(); i++) {
            if (!Character.isWhitespace(line.charAt(i))) {
                return line.substring(i);
            }
        }
        return "";
    }

    /**
     * An array of references to the most recently added node for each given level. Works as a fast index to the nodes
     * so we can find parents quickly. Whenever a new node is added, set, or removed for level N, all entries in this
     * index &gt; N (i.e., all the child levels) need to be cleared.
     */
    private final StringTree[] lastNodeAtLevel = new StringTree[100];

    /**
     * The buffered input stream we are reading from
     */
    private final BufferedInputStream inputStream;

    /**
     * A flag indicating whether the current line from the input file begins with a 1-2 digit level number followed by a
     * space
     */
    private boolean beginsWithLevelAndSpace;

    /**
     * The string tree node that represents the current line and all its children.
     */
    private StringTree treeForCurrentLine;

    /**
     * The tree that represents the entire file. Will be returned at the end of the StringTree construction process.
     */
    private final StringTree treeForWholeFile = new StringTree();

    /**
     * The most recently added node
     */
    private StringTree mostRecentlyAdded;

    /**
     * The {@link GedcomParser} instance this object is building {@link StringTree} instances for
     */
    private final GedcomParser parser;

    /**
     * Constructor
     * 
     * @param parser
     *            the {@link GedcomParser} this object will be assisting with making a {@link StringTree} for
     * 
     * @param inputStream
     *            The buffered input stream we are reading from
     */
    public StringTreeBuilder(GedcomParser parser, BufferedInputStream inputStream) {
        this.parser = parser;
        this.inputStream = inputStream;
    }

    /**
     * Construct a {@link StringTree} out of a flat {@link List} of Strings
     * 
     * @param lines
     *            the lines of the file
     * 
     * @return the {@link StringTree} created from the contents of the input stream
     * @throws IOException
     *             if there is a problem reading the data from the reader
     * @throws GedcomParserException
     *             if there is an error with parsing the data from the stream
     */
    StringTree makeStringTreeFromFlatLines(List<String> lines) throws IOException, GedcomParserException {
        treeForWholeFile.level = -1;
        mostRecentlyAdded = null;
        try {
            for (int lineNum = 1; lineNum <= lines.size(); lineNum++) {
                treeForCurrentLine = new StringTree();
                treeForCurrentLine.lineNum = lineNum;

                String line = lines.get(lineNum - 1);
                line = leftTrim(line); // See issue 57

                checkIfNewLevelLine(lineNum, line);

                if (beginsWithLevelAndSpace) {
                    addNewNode(lineNum, line);
                    mostRecentlyAdded = treeForCurrentLine;
                } else {
                    makeConcatenationOfPreviousNode(lineNum, line);
                }
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return treeForWholeFile;
    }

    /**
     * Add a new node to the correct parent node in the StringTree
     * 
     * @param lineNum
     *            the current line number in the file
     * @param line
     *            the current line of the file
     * 
     * @throws GedcomParserException
     *             if there are file lines that are not well formed - see {@link LinePieces#LinePieces(String, int)}
     */
    private void addNewNode(int lineNum, String line) throws GedcomParserException {
        LinePieces lp = new LinePieces(line, lineNum);
        treeForCurrentLine.level = lp.level;
        treeForCurrentLine.id = lp.id;
        treeForCurrentLine.tag = lp.tag.intern();
        treeForCurrentLine.value = lp.remainder;
        lp = null;

        StringTree addTo = null;
        if (treeForCurrentLine.level == 0) {
            addTo = treeForWholeFile;
        } else {
            addTo = lastNodeAtLevel[treeForCurrentLine.level - 1];
        }
        if (addTo == null) {
            parser.errors.add(treeForCurrentLine.tag + " tag at line " + treeForCurrentLine.lineNum + ": Unable to find suitable parent node at level "
                    + (treeForCurrentLine.level - 1));
        } else {
            addTo.children.add(treeForCurrentLine);
            treeForCurrentLine.parent = addTo;
            lastNodeAtLevel[treeForCurrentLine.level] = treeForCurrentLine;
        }
        Arrays.fill(lastNodeAtLevel, treeForCurrentLine.level + 1, 100, null);
    }

    /**
     * Check that the line has a level number so we can know whether it's a new line or a continuation of the previous
     * one
     * 
     * @param lineNum
     *            the current line number in the file
     * @param line
     *            the current line of the file
     * @throws GedcomParserException
     *             if we can't determine whether the current line is a new leveled line in the file or not when strict
     *             line breaks are off, or that the line does not begin with a level number when strict line breaks are
     *             enabled.
     */
    private void checkIfNewLevelLine(int lineNum, String line) throws GedcomParserException {
        beginsWithLevelAndSpace = false;
        try {
            // Probably sets it to true, but might not for a non-standard file - see Issue 100
            beginsWithLevelAndSpace = startsWithLevelAndSpace(lineNum, line);
        } catch (GedcomParserException e) {
            if (parser.strictLineBreaks) {
                throw e;
            }
        }
    }

    /**
     * Make the current node a concatenation of the previous node.
     * 
     * @param lineNum
     *            the current line number in the file
     * @param line
     *            the current line of the file
     */
    private void makeConcatenationOfPreviousNode(int lineNum, String line) {
        // Doesn't begin with a level number followed by a space, and we don't have strictLineBreaks
        // required, so it's probably meant to be a continuation of the previous text value.
        if (mostRecentlyAdded != null) {
            // Try to add as a CONT line to previous node, as if the file had been properly escaped
            treeForCurrentLine.level = mostRecentlyAdded.level + 1;
            treeForCurrentLine.tag = Tag.CONTINUATION.tagText;
            treeForCurrentLine.value = line;
            treeForCurrentLine.parent = mostRecentlyAdded;
            mostRecentlyAdded.children.add(treeForCurrentLine);
            parser.warnings.add(
                    "Line " + lineNum + " did not begin with a level and tag, so it was treated as a " + "non-standard continuation of the previous line.");
        } else {
            parser.warnings.add("Line " + lineNum + " did not begin with a level and tag, so it was discarded.");
        }
    }

    /**
     * Does this line start with a 1-2 digit level number and a space?
     * 
     * @param lineNum
     *            the line number being read
     * @param line
     *            the line being read
     * 
     * @return true if and only if the line begins with a 1-2 digit level number followed by a space
     * @throws GedcomParserException
     *             if the line does not begin with a 1-2 digit number followed by a space
     */
    private boolean startsWithLevelAndSpace(int lineNum, String line) throws GedcomParserException {

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
            }
            throw new GedcomParserException("Line " + lineNum + " does not begin with a 1 or 2 digit number for the level followed by a space: " + line);
        } catch (IndexOutOfBoundsException e) {
            throw new GedcomParserException("Line " + lineNum + " does not begin with a 1 or 2 digit number for the level followed by a space: " + line, e);
        }

    }

}
