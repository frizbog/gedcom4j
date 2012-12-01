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
package org.gedcom4j.io;

/**
 * Represents an unsupported/illegal value for a charset encoding in a GEDCOM
 * file
 * 
 * @author frizbog1
 */
public class UnsupportedGedcomCharsetException extends Exception {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -1209602510830929697L;

    /**
     * Default constructor
     */
    public UnsupportedGedcomCharsetException() {
        // Superclass handles just fine
    }

    /**
     * Constructor that takes just the message
     * 
     * @param message
     *            the message
     */
    public UnsupportedGedcomCharsetException(String message) {
        super(message);
    }

    /**
     * Constructor that takes the message and cause
     * 
     * @param message
     *            the message
     * @param cause
     *            the cause
     */
    public UnsupportedGedcomCharsetException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor that takes just the cause
     * 
     * @param cause
     *            the cause
     */
    public UnsupportedGedcomCharsetException(Throwable cause) {
        super(cause);
    }

}
