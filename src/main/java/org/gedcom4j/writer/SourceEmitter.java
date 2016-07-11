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

import java.util.Collection;

import org.gedcom4j.exception.GedcomWriterException;
import org.gedcom4j.exception.WriterCancelledException;
import org.gedcom4j.model.*;

/**
 * Emitter for {@link Source} objects
 * 
 * @author frizbog
 */
class SourceEmitter extends AbstractEmitter<Collection<Source>> {

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
    SourceEmitter(GedcomWriter baseWriter, int startLevel, Collection<Source> writeFrom) throws WriterCancelledException {
        super(baseWriter, startLevel, writeFrom);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void emit() throws GedcomWriterException {
        for (Source s : writeFrom) {
            emitTag(0, s.getXref(), "SOUR");
            SourceData d = s.getData();
            if (d != null) {
                emitTag(1, "DATA");
                for (EventRecorded e : d.getEventsRecorded()) {
                    emitTagWithOptionalValue(2, "EVEN", e.getEventType());
                    emitTagIfValueNotNull(3, "DATE", e.getDatePeriod());
                    emitTagIfValueNotNull(3, "PLAC", e.getJurisdiction());
                }
                emitTagIfValueNotNull(2, "AGNC", d.getRespAgency());
                new NotesEmitter(baseWriter, 2, d.getNotes()).emit();
            }
            emitLinesOfText(1, "AUTH", s.getOriginatorsAuthors());
            emitLinesOfText(1, "TITL", s.getTitle());
            emitTagIfValueNotNull(1, "ABBR", s.getSourceFiledBy());
            emitLinesOfText(1, "PUBL", s.getPublicationFacts());
            emitLinesOfText(1, "TEXT", s.getSourceText());
            emitRepositoryCitation(1, s.getRepositoryCitation());
            new MultimediaLinksEmitter(baseWriter, 1, s.getMultimedia()).emit();
            new NotesEmitter(baseWriter, 1, s.getNotes()).emit();
            if (s.getUserReferences() != null) {
                for (UserReference u : s.getUserReferences()) {
                    emitTagWithRequiredValue(1, "REFN", u.getReferenceNum());
                    emitTagIfValueNotNull(2, "TYPE", u.getType());
                }
            }
            emitTagIfValueNotNull(1, "RIN", s.getRecIdNumber());
            new ChangeDateEmitter(baseWriter, 1, s.getChangeDate()).emit();
            emitCustomTags(1, s.getCustomTags());
        }
    }

    /**
     * Write out a repository citation (see SOURCE_REPOSITORY_CITATION in the gedcom spec)
     * 
     * @param level
     *            the level we're writing at
     * @param repositoryCitation
     *            the repository citation to write out
     * @throws GedcomWriterException
     *             if the repository citation passed in has a null repository reference
     */
    private void emitRepositoryCitation(int level, RepositoryCitation repositoryCitation) throws GedcomWriterException {
        if (repositoryCitation != null) {
            if (repositoryCitation.getRepositoryXref() == null) {
                throw new GedcomWriterException("Repository Citation has null repository reference");
            }
            emitTagWithRequiredValue(level, "REPO", repositoryCitation.getRepositoryXref());
            new NotesEmitter(baseWriter, level + 1, repositoryCitation.getNotes()).emit();
            if (repositoryCitation.getCallNumbers() != null) {
                for (SourceCallNumber scn : repositoryCitation.getCallNumbers()) {
                    emitTagWithRequiredValue(level + 1, "CALN", scn.getCallNumber());
                    emitTagIfValueNotNull(level + 2, "MEDI", scn.getMediaType());
                }
            }
            emitCustomTags(level + 1, repositoryCitation.getCustomTags());
        }

    }
}
