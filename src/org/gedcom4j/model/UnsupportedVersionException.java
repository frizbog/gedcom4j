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
package org.gedcom4j.model;

/**
 * An exception for when an unsupported GEDCOM version is used
 * 
 * @author frizbog1
 * 
 */
public class UnsupportedVersionException extends Exception {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -7778881391248235659L;

    /**
     * Default constructor
     */
    public UnsupportedVersionException() {
        // Nothing special to do - super behavior works
    }

    /**
     * Constructor that takes only a message
     * 
     * @param message
     *            the message
     */
    public UnsupportedVersionException(String message) {
        super(message);
        // Nothing special to do - super behavior works
    }

    /**
     * Constructor that takes a message and cause
     * 
     * @param message
     *            the message
     * @param cause
     *            the cause
     */
    public UnsupportedVersionException(String message, Throwable cause) {
        super(message, cause);
        // Nothing special to do - super behavior works
    }

    /**
     * Constructor that takes only a cause
     * 
     * @param cause
     *            the cause
     */
    public UnsupportedVersionException(Throwable cause) {
        super(cause);
        // Nothing special to do - super behavior works
    }

}
