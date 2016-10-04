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
import org.gedcom4j.exception.WriterCancelledException;
import org.gedcom4j.model.NoteRecord;
import org.gedcom4j.model.UserReference;

/**
 * Emitter for {@link NoteRecord}s.
 * 
 * @author frizbog
 *
 */
class NoteRecordEmitter extends AbstractEmitter<Collection<NoteRecord>> {

    /**
     * Constructor
     * 
     * @param baseWriter
     *            The base Gedcom writer class this Emitter is partnering with to emit the entire file
     * @param startLevel
     *            write starting at this level
     * @param collection
     *            object to write
     * @throws WriterCancelledException
     *             if cancellation was requested during the operation
     */
    NoteRecordEmitter(GedcomWriter baseWriter, int startLevel, Collection<NoteRecord> collection) throws WriterCancelledException {
        super(baseWriter, startLevel, collection);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void emit() throws GedcomWriterException {
        if (writeFrom != null) {
            for (NoteRecord n : writeFrom) {
                emitNoteRecord(n);
                if (baseWriter.isCancelled()) {
                    throw new WriterCancelledException("Construction and writing of GEDCOM cancelled");
                }
            }
        }
    }

    /**
     * Emit a note structure (possibly multi-line)
     * 
     * @param note
     *            the note structure
     * @throws GedcomWriterException
     *             if the data is malformed and cannot be written
     */
    private void emitNoteRecord(NoteRecord note) throws GedcomWriterException {
        if (note.getXref() == null || note.getXref().trim().isEmpty()) {
            throw new GedcomWriterException(NoteRecord.class.getName() + " has no xref");
        }
        emitLinesOfText(0, note.getXref(), "NOTE", note.getLines());
        new SourceCitationEmitter(baseWriter, 1, note.getCitations()).emit();
        if (note.getUserReferences() != null) {
            for (UserReference u : note.getUserReferences()) {
                emitTagWithRequiredValue(1, "REFN", u.getReferenceNum());
                emitTagIfValueNotNull(2, "TYPE", u.getType());
            }
        }
        emitTagIfValueNotNull(1, "RIN", note.getRecIdNumber());
        new ChangeDateEmitter(baseWriter, 1, note.getChangeDate()).emit();
        emitCustomFacts(1, note.getCustomFacts());
    }

}
