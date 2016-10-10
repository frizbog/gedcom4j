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

import java.util.List;

import org.gedcom4j.exception.GedcomWriterException;
import org.gedcom4j.exception.GedcomWriterVersionDataMismatchException;
import org.gedcom4j.exception.WriterCancelledException;
import org.gedcom4j.model.FileReference;
import org.gedcom4j.model.Multimedia;
import org.gedcom4j.model.MultimediaReference;

/**
 * Emitter for multimedia links
 * 
 * @author frizbog
 */
class MultimediaLinksEmitter extends AbstractEmitter<List<MultimediaReference>> {

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
    MultimediaLinksEmitter(GedcomWriter baseWriter, int startLevel, List<MultimediaReference> writeFrom)
            throws WriterCancelledException {
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
        for (MultimediaReference mr : writeFrom) {
            if (mr == null) {
                continue;
            }
            Multimedia m = mr.getMultimedia();
            if (m == null) {
                continue;
            }
            if (m.getXref() == null) {
                // This is an inline multimedia reference, not to one of the root-level multimedia records
                emitTag(startLevel, "OBJE");
                if (g55()) {
                    // GEDCOM 5.5 format
                    if (m.getFileReferences().size() > 1) {
                        throw new GedcomWriterVersionDataMismatchException("GEDCOM version is 5.5, but multimedia link references "
                                + "multiple files, which is only allowed in GEDCOM 5.5.1");
                    }
                    if (m.getFileReferences().size() == 1) {
                        FileReference fr = m.getFileReferences().get(0);
                        if (fr.getFormat() == null) {
                            emitTagWithRequiredValue(startLevel + 1, "FORM", m.getEmbeddedMediaFormat());
                        } else {
                            emitTagWithRequiredValue(startLevel + 1, "FORM", fr.getFormat());
                        }
                        emitTagIfValueNotNull(startLevel + 1, "TITL", m.getEmbeddedTitle());
                        emitTagWithRequiredValue(startLevel + 1, "FILE", fr.getReferenceToFile());
                    } else {
                        emitTagWithRequiredValue(startLevel + 1, "FORM", m.getEmbeddedMediaFormat());
                        emitTagIfValueNotNull(startLevel + 1, "TITL", m.getEmbeddedTitle());
                    }
                    new NoteStructureEmitter(baseWriter, startLevel + 1, m.getNoteStructures()).emit();
                } else {
                    // GEDCOM 5.5.1 format
                    for (FileReference fr : m.getFileReferences()) {
                        emitTagWithRequiredValue(startLevel + 1, "FILE", fr.getReferenceToFile());
                        emitTagIfValueNotNull(startLevel + 2, "FORM", fr.getFormat());
                        emitTagIfValueNotNull(startLevel + 3, "MEDI", fr.getMediaType());
                        emitTagIfValueNotNull(startLevel + 1, "TITL", fr.getTitle());
                    }
                    if (m.getNoteStructures() != null && !m.getNoteStructures().isEmpty()) {
                        throw new GedcomWriterVersionDataMismatchException(
                                "GEDCOM version is 5.5.1, but multimedia link has notes which are no longer allowed in 5.5");
                    }
                }
            } else {
                // This is an multimedia reference to one of the root-level multimedia records
                emitTagWithRequiredValue(startLevel, "OBJE", m.getXref());
            }
            emitCustomFacts(startLevel + 1, m.getCustomFacts());
            emitCustomFacts(startLevel + 1, mr.getCustomFacts());
        }
    }

}
