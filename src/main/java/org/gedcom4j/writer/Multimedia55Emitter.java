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
package org.gedcom4j.writer;

import java.util.Collection;

import org.gedcom4j.exception.GedcomWriterException;
import org.gedcom4j.exception.GedcomWriterVersionDataMismatchException;
import org.gedcom4j.exception.WriterCancelledException;
import org.gedcom4j.model.Multimedia;
import org.gedcom4j.model.UserReference;

/**
 * Emitter for {@link Multimedia} objects when using GEDCOM 5.5
 * 
 * @author frizbog
 */
class Multimedia55Emitter extends AbstractEmitter<Collection<Multimedia>> {

    /**
     * Constructor
     * 
     * @param baseWriter
     *            The base Gedcom writer class this Emitter is partnering with to emit the entire file
     * @param startLevel
     *            write starting at this level
     * @param writeFrom
     *            object to write
     * @throws WriterCancelledException
     *             if cancellation was requested during the operation
     */
    Multimedia55Emitter(GedcomWriter baseWriter, int startLevel, Collection<Multimedia> writeFrom) throws WriterCancelledException {
        super(baseWriter, startLevel, writeFrom);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void emit() throws GedcomWriterException {
        if (writeFrom == null) {
            return;
        }
        for (Multimedia m : writeFrom) {
            emitTag(0, m.getXref(), "OBJE");
            emitTagWithRequiredValue(1, "FORM", m.getEmbeddedMediaFormat());
            emitTagIfValueNotNull(1, "TITL", m.getEmbeddedTitle());
            new NotesEmitter(baseWriter, 1, m.getNotes()).emit();
            emitTag(1, "BLOB");
            for (String b : m.getBlob()) {
                emitTagWithRequiredValue(2, "CONT", b);
            }
            if (m.getContinuedObject() != null && m.getContinuedObject().getXref() != null) {
                emitTagWithRequiredValue(1, "OBJE", m.getContinuedObject().getXref());
            }
            if (m.getUserReferences() != null) {
                for (UserReference u : m.getUserReferences()) {
                    emitTagWithRequiredValue(1, "REFN", u.getReferenceNum());
                    emitTagIfValueNotNull(2, "TYPE", u.getType());
                }
            }
            emitTagIfValueNotNull(1, "RIN", m.getRecIdNumber());
            new ChangeDateEmitter(baseWriter, 1, m.getChangeDate()).emit();
            if (m.getFileReferences() != null && !m.getFileReferences().isEmpty()) {
                throw new GedcomWriterVersionDataMismatchException("GEDCOM version is 5.5, but found file references in multimedia object " + m.getXref()
                        + " which are not allowed until GEDCOM 5.5.1");
            }
            emitCustomTags(1, m.getCustomTags());
        }
    }

}
