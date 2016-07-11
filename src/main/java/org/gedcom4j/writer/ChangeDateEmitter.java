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

package org.gedcom4j.writer;

import org.gedcom4j.exception.GedcomWriterException;
import org.gedcom4j.model.ChangeDate;

/**
 * @author frizbog
 *
 */
class ChangeDateEmitter extends AbstractEmitter<ChangeDate> {
    /**
     * Constructor
     * 
     * @param baseWriter
     *            The base Gedcom writer class this Emitter is partnering with to emit the entire file
     * @param startLevel
     *            write starting at this level
     * @param writeFrom
     *            object to write
     */
    ChangeDateEmitter(GedcomWriter baseWriter, int startLevel, ChangeDate writeFrom) {
        super(baseWriter, startLevel, writeFrom);
    }

    @Override
    protected void emit() throws GedcomWriterException {
        if (writeFrom != null) {
            emitTag(startLevel, "CHAN");
            emitTagWithRequiredValue(startLevel + 1, "DATE", writeFrom.getDate());
            emitTagIfValueNotNull(startLevel + 2, "TIME", writeFrom.getTime());
            new NotesEmitter(baseWriter, startLevel + 1, writeFrom.getNotes()).emit();
            emitCustomTags(startLevel + 1, writeFrom.getCustomTags());
        }
    }

}
