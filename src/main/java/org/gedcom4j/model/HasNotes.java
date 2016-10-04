/*
 * Copyright (c) 2016 Mark A. Sikes
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
package org.gedcom4j.model;

import java.util.List;

/**
 * Marks an object that has note strucutres (inline notes, or references to root level notes - see {@link NoteStructure})
 * 
 * @author Mark A Sikes
 */
public interface HasNotes extends ModelElement {
    /**
     * Get the notes on this object
     * 
     * @return the notes on this object
     */
    List<NoteStructure> getNoteStructures();

    /**
     * Get the notes on this object, initializing the collection if needed
     * 
     * @param initializeIfNeeded
     *            set to true if you want the backing collection to be instantiated/initialized if it is currently null
     * @return the notes on this object, or null if there are none and <tt>initializeIfNeeded</tt> is false
     */
    List<NoteStructure> getNoteStructures(boolean initializeIfNeeded);
}
