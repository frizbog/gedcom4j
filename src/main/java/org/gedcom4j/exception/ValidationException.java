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
 * An exception indicating that the process of validation failed. Does not mean that there is a problem with the data -- those are
 * found in the {@link org.gedcom4j.validate.ValidationResults} class.
 * 
 * @author frizbog1
 * 
 */
public class ValidationException extends RuntimeException {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -5656983786362148078L;

    /**
     * Default constructor
     */
    public ValidationException() {
        super();
    }

    /**
     * Constructor that only takes a message
     * 
     * @param message
     *            the message of the exception
     */
    public ValidationException(String message) {
        super(message);
    }

    /**
     * Constructor that takes both a message and an underlying cause
     * 
     * @param message
     *            the message of the exception
     * @param cause
     *            the underlying cause of the exception
     */
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor that only takes a causing throwable
     * 
     * @param cause
     *            the underlying cause of the exception
     */
    public ValidationException(Throwable cause) {
        super(cause);
    }

}
