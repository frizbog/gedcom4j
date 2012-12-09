/*
 * Copyright (c) 2009-2012 Matthew R. Harrah
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
import java.io.InputStream;
import java.util.List;

import org.gedcom4j.io.GedcomFileReader;
import org.gedcom4j.model.StringTree;

/**
 * A helper class so GedcomParser won't be so huge
 * 
 * @author frizbog
 * 
 */
public final class GedcomParserHelper {

    /**
     * Find the last item at the supplied level in the supplied tree
     * 
     * @param tree
     *            the tree
     * @param level
     *            the level at which we are parsing
     * @return the last item at the supplied level in the supplied tree
     */
    static StringTree findLast(StringTree tree, int level) {
        if (tree.level == level) {
            return tree;
        }
        StringTree lastChild = tree.children.get(tree.children.size() - 1);
        if (lastChild.level == level) {
            return lastChild;
        }
        return findLast(lastChild, level);
    }

    /**
     * Read data from an {@link InputStream} and construct a {@link StringTree} object from its contents
     * 
     * @param bytes
     *            the input stream over the bytes of the file
     * @return the {@link StringTree} created from the contents of the input stream
     * @throws IOException
     *             if there is a problem reading the data from the reader
     */
    static StringTree makeStringTreeFromStream(BufferedInputStream bytes) throws IOException {
        List<String> lines = new GedcomFileReader().getLines(bytes);
        StringTree result = new StringTree();
        result.level = -1;
        try {
            for (int lineNum = 1; lineNum <= lines.size(); lineNum++) {
                String line = lines.get(lineNum - 1);
                LinePieces lp = new LinePieces(line);
                StringTree st = new StringTree();
                st.lineNum = lineNum;
                st.level = lp.level;
                st.id = lp.id;
                st.tag = lp.tag;
                st.value = lp.remainder;
                StringTree addTo = findLast(result, lp.level - 1);
                addTo.children.add(st);
                st.parent = addTo;
            }
        } finally {
            if (bytes != null) {
                bytes.close();
            }
        }
        return result;
    }

    /**
     * Load the flat file into a tree structure that reflects the heirarchy of its contents, using the default encoding
     * for yor JVM
     * 
     * @param filename
     *            the file to load
     * @return the string tree representation of the data from the file
     * @throws IOException
     *             if there is a problem reading the file
     */
    static StringTree readFile(String filename) throws IOException {
        FileInputStream fis = new FileInputStream(filename);
        try {
            return GedcomParserHelper.makeStringTreeFromStream(new BufferedInputStream(fis));
        } finally {
            fis.close();
        }
    }

    /**
     * Read all the data from a stream and return the StringTree representation of that data
     * 
     * @param stream
     *            the stream to read
     * @return the data from the stream as a StringTree
     * @throws IOException
     *             if there's a problem reading the data off the stream
     */
    static StringTree readStream(BufferedInputStream stream) throws IOException {
        return GedcomParserHelper.makeStringTreeFromStream(stream);
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
     * Utility class, so prevent instantiation (and thus subclassing)
     */
    private GedcomParserHelper() {
        // Do nothing
    }

}
