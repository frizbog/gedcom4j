/*
 * Copyright (c) 2009-2016 Matthew R. Harrah
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.gedcom4j.parser;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.gedcom4j.io.GedcomFileReader;
import org.gedcom4j.model.StringTree;

/**
 * A helper class so GedcomParser won't be so huge
 * 
 * @author frizbog
 * 
 */
final class GedcomParserHelper {

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

        return line.replaceAll("\\A\\s*", "");
    }

    /**
     * Load the flat file into a tree structure that reflects the heirarchy of its contents, using the default encoding
     * for your JVM
     * 
     * @param filename
     *            the file to load
     * @param errors
     *            a list of error strings we can add to (if we can keep processing)
     * @param warnings
     *            a list of warning strings we can add to (if we can keep processing)
     * @param strictLineBreaks
     *            whether line breaks in the text values that are not escaped with CONT tags should be processed
     *            strictly according to the spec. If false, these lines are ignored.
     * @return the string tree representation of the data from the file
     * @throws IOException
     *             if there is a problem reading the file
     * @throws GedcomParserException
     *             if there is a problem parsing the data in the file
     */
    static StringTree readFile(String filename, List<String> errors, List<String> warnings, boolean strictLineBreaks) throws IOException, GedcomParserException {
        FileInputStream fis = new FileInputStream(filename);
        try {
            return makeStringTreeFromStream(new BufferedInputStream(fis), errors, warnings, strictLineBreaks);
        } finally {
            fis.close();
        }
    }

    /**
     * Read all the data from a stream and return the StringTree representation of that data
     * 
     * @param stream
     *            the stream to read
     * @param errors
     *            a list of error strings
     * @param warnings
     *            a list of warnings
     * @param strictLineBreaks
     *            whether line breaks in the text values that are not escaped with CONT tags should be processed
     *            strictly according to the spec. If false, these lines are ignored.
     * @return the data from the stream as a StringTree
     * @throws IOException
     *             if there's a problem reading the data off the stream
     * @throws GedcomParserException
     *             if there is an error parsing the gedcom data
     */
    static StringTree readStream(BufferedInputStream stream, List<String> errors, List<String> warnings, boolean strictLineBreaks) throws IOException,
    GedcomParserException {
        return makeStringTreeFromStream(stream, errors, warnings, strictLineBreaks);
    }

    /**
     * Returns true if the node passed in uses a cross-reference to another node
     * 
     * @param st
     *            the node
     * @return true if and only if the node passed in uses a cross-reference to another node
     */
    static boolean referencesAnotherNode(StringTree st) {
        return st.value != null && st.value.matches("\\@.*\\@");
    }

    /**
     * Find the last item at the supplied level in the supplied tree. Uses recursion to look for the latest child item
     * of the latest child item (etc) of the root of the tree.
     * 
     * @param tree
     *            the tree (or portion thereof) we want to look through
     * @param lookingForLevel
     *            the level in the tree that we want to find
     * @param errors
     *            a list of error strings we can add to if we can keep processing
     * @param currentNode
     *            the current node we are trying to process and find a parent to hang this node on
     * @return the last item at the supplied level in the supplied tree
     * @throws GedcomParserException
     *             if there is an issue with level numbers not being consistent
     */
    private static StringTree findLast(StringTree tree, int lookingForLevel, List<String> errors, StringTree currentNode) throws GedcomParserException {
        if (tree.level == lookingForLevel) {
            return tree;
        }
        if (tree.children.isEmpty()) {
            errors.add(currentNode.tag + " tag at line " + currentNode.lineNum + ": Unable to find suitable parent node at level " + lookingForLevel
                    + " under " + tree);
            return null;
        }
        StringTree lastChild = tree.children.get(tree.children.size() - 1);
        if (lastChild.level == lookingForLevel) {
            return lastChild;
        }
        return findLast(lastChild, lookingForLevel, errors, currentNode);
    }

    /**
     * Read data from an {@link java.io.InputStream} and construct a {@link StringTree} object from its contents
     * 
     * @param bytes
     *            the input stream over the bytes of the file
     * @param errors
     *            a list of error strings we can add to (if we can keep processing)
     * @param warnings
     *            a list of warning strings we can add to (if we can keep processing)
     * @param strictLineBreaks
     *            whether line breaks in the text values that are not escaped with CONT tags should be processed
     *            strictly according to the spec. If false, these lines are ignored.
     * @return the {@link StringTree} created from the contents of the input stream
     * @throws IOException
     *             if there is a problem reading the data from the reader
     * @throws GedcomParserException
     *             if there is an error with parsing the data from the stream
     */
    private static StringTree makeStringTreeFromStream(BufferedInputStream bytes, List<String> errors, List<String> warnings, boolean strictLineBreaks)
            throws IOException, GedcomParserException {
        List<String> lines = new GedcomFileReader(bytes).getLines();
        StringTree result = new StringTree();
        result.level = -1;
        StringTree lastAdded = null;
        try {
            for (int lineNum = 1; lineNum <= lines.size(); lineNum++) {
                String line = lines.get(lineNum - 1);
                line = leftTrim(line); // See issue 57
                StringTree st = new StringTree();
                st.lineNum = lineNum;
                if (line.matches("\\d .+") || strictLineBreaks) {
                    if (!line.matches("\\d .+")) {
                        throw new GedcomParserException("Line " + lineNum + " does not begin with a level number and cannot be parsed.");
                    }
                    LinePieces lp = new LinePieces(line);
                    st.level = lp.level;
                    st.id = lp.id;
                    st.tag = lp.tag;
                    st.value = lp.remainder;
                    StringTree addTo;
                    addTo = findLast(result, lp.level - 1, errors, st);
                    if (addTo != null) {
                        addTo.children.add(st);
                        st.parent = addTo;
                    }
                    lastAdded = st;
                } else {
                    // Doesn't begin with a level number followed by a space, and we don't have strictLineBreaks
                    // required, so it's probably meant to be a continuation of the previous text value.
                    if (lastAdded != null) {
                        // Try to add as a CONT line to previous node, as if the file had been properly escaped
                        st.level = lastAdded.level + 1;
                        st.tag = Tag.CONTINUATION.tagText;
                        st.value = line;
                        st.parent = lastAdded;
                        lastAdded.children.add(st);
                        warnings.add("Line " + lineNum + " did not begin with a level and tag, so it was treated as a "
                                + "non-standard continuation of the previous line.");
                    } else {
                        warnings.add("Line " + lineNum + " did not begin with a level and tag, so it was discarded.");
                    }
                }
            }
        } finally {
            if (bytes != null) {
                bytes.close();
            }
        }
        return result;
    }

    /**
     * Utility class, so prevent instantiation (and thus subclassing)
     */
    private GedcomParserHelper() {
        // Do nothing
    }

}
