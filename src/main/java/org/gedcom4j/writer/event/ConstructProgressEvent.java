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
package org.gedcom4j.writer.event;

import java.util.EventObject;

/**
 * An event to hold progress information about construction of GEDCOM strings
 * 
 * @author frizbog
 */
public class ConstructProgressEvent extends EventObject {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -8214346212885031181L;

    /**
     * Is the file completely processed?
     */
    private final boolean complete;

    /**
     * How many lines have been processed
     */
    private final int linesProcessed;

    /**
     * Constructor
     * 
     * @param source
     *            the source object
     * @param linesProcessed
     *            the number of lines processed
     * @param complete
     *            is the file complete
     */
    public ConstructProgressEvent(Object source, int linesProcessed, boolean complete) {
        super(source);
        this.linesProcessed = linesProcessed;
        this.complete = complete;
    }

    /**
     * Get the number of lines processed
     * 
     * @return the number of lines processed
     */
    public int getLinesProcessed() {
        return linesProcessed;
    }

    /**
     * Is the file completely processed?
     * 
     * @return true if the file is completely processed
     */
    public boolean isComplete() {
        return complete;
    }

    @Override
    public String toString() {
        return "FileProgressEvent [complete=" + complete + ", linesProcessed=" + linesProcessed + "]";
    }

}
