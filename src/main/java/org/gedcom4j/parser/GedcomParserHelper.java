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

import org.gedcom4j.model.StringTree;

/**
 * A helper class so GedcomParser won't be so huge
 * 
 * @author frizbog
 * 
 */
final class GedcomParserHelper {

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
    static StringTree readFile(String filename, List<String> errors, List<String> warnings, boolean strictLineBreaks)
            throws IOException, GedcomParserException {
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
    static StringTree readStream(BufferedInputStream stream, List<String> errors, List<String> warnings, boolean strictLineBreaks)
            throws IOException, GedcomParserException {
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
        if (st.value == null) {
            return false;
        }
        int r1 = st.value.indexOf('@');
        if (r1 == -1) {
            return false;
        }
        int r2 = st.value.indexOf('@', r1);
        if (r2 == -1) {
            return false;
        }
        return true;
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
        return new StringTreeBuilder(bytes, errors, warnings, strictLineBreaks).makeStringTreeFromStream();
    }

    /**
     * Utility class, so prevent instantiation (and thus subclassing)
     */
    private GedcomParserHelper() {
        // Do nothing
    }

}
