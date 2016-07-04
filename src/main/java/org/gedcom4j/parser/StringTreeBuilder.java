package org.gedcom4j.parser;

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
     * The line number we're on - 1-based!!!!!!
     */
    private int lineNum = 0;

    /**
     * The line we're currently processing
     */
    private String line;

    /**
     * A canonicalizing string pool to reduce the number of repeated instances of strings, without using String.intern()
     */
    private final StringCanonicalizer canonizer = new StringCanonicalizer();

    /**
     * Constructor
     * 
     * @param parser
     *            the {@link GedcomParser} this object will be assisting with making a {@link StringTree} for
     * 
     */
    public StringTreeBuilder(GedcomParser parser) {
        this.parser = parser;
        getTree().setLevel(-1);
        mostRecentlyAdded = null;
        lineNum = 0; // Haven't read any lines yet
    }

    /**
     * Get the string tree representing the entire file - the string tree that this {@link StringTreeBuilder} object was
     * created to build
     * 
     * @return the string tree representing the entire file
     */
    public StringTree getTree() {
        return treeForWholeFile;
    }

    /**
     * Add the supplied line to the right place in the StringTree being built
     * 
     * @param l
     *            the line to add
     * 
     * @throws GedcomParserException
     *             if there is an error with parsing the data from the stream
     */
    void appendLine(String l) throws GedcomParserException {
        line = l;
        lineNum++;
        treeForCurrentLine = new StringTree();
        treeForCurrentLine.setLineNum(lineNum);

        checkIfNewLevelLine();

        if (beginsWithLevelAndSpace) {
            addNewNode();
            mostRecentlyAdded = treeForCurrentLine;
        } else {
            makeConcatenationOfPreviousNode();
        }
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
        getTree().setLevel(-1);
        mostRecentlyAdded = null;
        while (lineNum < lines.size()) {
            String l = leftTrim(lines.get(lineNum));
            appendLine(l);
        }
        return getTree();
    }

    /**
     * Add a new node to the correct parent node in the StringTree
     * 
     * @throws GedcomParserException
     *             if there are file lines that are not well formed - see {@link LinePieces#LinePieces(String, int)}
     */
    private void addNewNode() throws GedcomParserException {
        LinePieces lp = new LinePieces(line, lineNum);
        treeForCurrentLine.setLevel(lp.level);
        treeForCurrentLine.setId(lp.id);
        treeForCurrentLine.setTag(lp.tag.intern());
        treeForCurrentLine.setValue(canonizer.getCanonicalVersion(lp.remainder));
        lp = null;

        StringTree addTo = null;
        if (treeForCurrentLine.getLevel() == 0) {
            addTo = getTree();
        } else {
            addTo = lastNodeAtLevel[treeForCurrentLine.getLevel() - 1];
        }
        if (addTo == null) {
            parser.getErrors().add(treeForCurrentLine.getTag() + " tag at line " + treeForCurrentLine.getLineNum()
                    + ": Unable to find suitable parent node at level " + (treeForCurrentLine.getLevel() - 1));
        } else {
            addTo.getChildren().add(treeForCurrentLine);
            treeForCurrentLine.setParent(addTo);
            lastNodeAtLevel[treeForCurrentLine.getLevel()] = treeForCurrentLine;
        }
        Arrays.fill(lastNodeAtLevel, treeForCurrentLine.getLevel() + 1, 100, null);
    }

    /**
     * Check that the line has a level number so we can know whether it's a new line or a continuation of the previous
     * one
     * 
     * @throws GedcomParserException
     *             if we can't determine whether the current line is a new leveled line in the file or not when strict
     *             line breaks are off, or that the line does not begin with a level number when strict line breaks are
     *             enabled.
     */
    private void checkIfNewLevelLine() throws GedcomParserException {
        beginsWithLevelAndSpace = false;
        try {
            // Probably sets it to true, but might not for a non-standard file - see Issue 100
            beginsWithLevelAndSpace = startsWithLevelAndSpace();
        } catch (GedcomParserException e) {
            if (parser.isStrictLineBreaks()) {
                throw e;
            }
        }
    }

    /**
     * Make the current node a concatenation of the previous node.
     */
    private void makeConcatenationOfPreviousNode() {
        // Doesn't begin with a level number followed by a space, and we don't have strictLineBreaks
        // required, so it's probably meant to be a continuation of the previous text value.
        if (mostRecentlyAdded != null) {
            // Try to add as a CONT line to previous node, as if the file had been properly escaped
            treeForCurrentLine.setLevel(mostRecentlyAdded.getLevel() + 1);
            treeForCurrentLine.setTag(Tag.CONTINUATION.tagText);
            treeForCurrentLine.setValue(line);
            treeForCurrentLine.setParent(mostRecentlyAdded);
            mostRecentlyAdded.getChildren().add(treeForCurrentLine);
            parser.getWarnings().add("Line " + lineNum + " did not begin with a level and tag, so it was treated as a "
                    + "non-standard continuation of the previous line.");
        } else {
            parser.getWarnings().add("Line " + lineNum + " did not begin with a level and tag, so it was discarded.");
        }
    }

    /**
     * Does this line start with a 1-2 digit level number and a space?
     * 
     * @return true if and only if the line begins with a 1-2 digit level number followed by a space
     * @throws GedcomParserException
     *             if the line does not begin with a 1-2 digit number followed by a space
     */
    private boolean startsWithLevelAndSpace() throws GedcomParserException {

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
                    throw new GedcomParserException("Line " + lineNum + " does not begin with a 1 or 2 digit number for the level followed by a space: "
                            + line);
                }
            }
            throw new GedcomParserException("Line " + lineNum + " does not begin with a 1 or 2 digit number for the level followed by a space: " + line);
        } catch (IndexOutOfBoundsException e) {
            throw new GedcomParserException("Line " + lineNum + " does not begin with a 1 or 2 digit number for the level followed by a space: " + line, e);
        }

    }

}
