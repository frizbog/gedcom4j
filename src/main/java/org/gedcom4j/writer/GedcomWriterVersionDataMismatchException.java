/*
 * Copyright (c) 2009-2014 Matthew R. Harrah
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

package org.gedcom4j.writer;

/**
 * Represents a mismatch between the data being written and the GEDCOM version under which it's supposed to be written... in other words, the code is
 * trying to write 5.5.1 data as a 5.5 file or vice-versa.
 * 
 * @author frizbog
 * 
 */
public class GedcomWriterVersionDataMismatchException extends GedcomWriterException {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -3893462493329487757L;

    /**
     * Default constructor
     */
    public GedcomWriterVersionDataMismatchException() {
        super();
    }

    /**
     * Constructor that takes just a message
     * 
     * @param message
     *            the message
     */
    public GedcomWriterVersionDataMismatchException(String message) {
        super(message);
    }

    /**
     * Constructor that takes a message and a cause
     * 
     * @param message
     *            the message
     * @param cause
     *            the cause
     */
    public GedcomWriterVersionDataMismatchException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor that takes just a cause
     * 
     * @param cause
     *            the cause
     */
    public GedcomWriterVersionDataMismatchException(Throwable cause) {
        super(cause);
    }

}
