/*
 * Copyright (c) 2009-2016 Matthew R. Harrah
 *
 * MIT License
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package org.gedcom4j.exception;

/**
 * Exception indicating that the file construction and writing operation was cancelled
 * 
 * @author frizbog
 */
public class WriterCancelledException extends GedcomWriterException {

    /**
     * Serial version uid
     */
    private static final long serialVersionUID = -3295979672863551432L;

    /**
     * No-arg constructor
     */
    public WriterCancelledException() {
        super();
    }

    /**
     * Constructor that takes just a message
     * 
     * @param message
     *            the message
     */
    public WriterCancelledException(String message) {
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
    public WriterCancelledException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor that takes just a cause
     * 
     * @param cause
     *            the cause
     */
    public WriterCancelledException(Throwable cause) {
        super(cause);
    }

}
