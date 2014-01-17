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
package org.gedcom4j.parser;

/**
 * An exception thrown by the parser indicating inability to deal with the input
 * file
 * 
 * @author frizbog1
 */
public class GedcomParserException extends Exception {

    /**
     * The serial version UID
     */
    public static final long serialVersionUID = 6803960812766915985L;

    /**
     * No-arg constructor.
     */
    public GedcomParserException() {
        super();
    }

    /**
     * Constructor that takes a message only
     * 
     * @param message
     *            the message
     */
    public GedcomParserException(String message) {
        super(message);
    }

    /**
     * Constructor that takes a message and a causing exception
     * 
     * @param message
     *            the message
     * @param cause
     *            the causing exception
     */
    public GedcomParserException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor that takes only a causing exception
     * 
     * @param cause
     *            the causing exception
     */
    public GedcomParserException(Throwable cause) {
        super(cause);
    }

}
