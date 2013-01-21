/*
 * Copyright (c) 2009-2013 Matthew R. Harrah
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
package org.gedcom4j.model;

/**
 * A GEDCOM version supported by gedcom4j
 * 
 * @author frizbog1
 * 
 */
public enum SupportedVersion {
    /**
     * Version 5.5
     */
    V5_5("5.5"),
    /**
     * Version 5.5.1
     */
    V5_5_1("5.5.1");

    /**
     * Get the SupportedVersion instance that matches the supplied string
     * 
     * @param string
     *            the string
     * @return the SupportedVersion that matches the supplied string
     * @throws UnsupportedVersionException
     *             if no match can be found
     */
    public static SupportedVersion forString(String string) throws UnsupportedVersionException {
        if (V5_5.stringRepresentation.equals(string)) {
            return V5_5;
        }
        if (V5_5_1.stringRepresentation.equals(string)) {
            return V5_5_1;
        }
        throw new UnsupportedVersionException("Unsupported version: " + string);
    }

    /**
     * The string representation of the version
     */
    private String stringRepresentation;

    /**
     * Constructor that takes the string representation as a parameter
     * 
     * @param stringRep
     *            the string representation of the version
     */
    SupportedVersion(String stringRep) {
        stringRepresentation = stringRep;
    }

    /**
     * Get a string representation of this object
     * 
     * @see java.lang.Enum#toString()
     * @return a string representation of this object
     */
    @Override
    public String toString() {
        return stringRepresentation;
    }
}
