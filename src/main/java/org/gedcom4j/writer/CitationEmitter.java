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
import org.gedcom4j.exception.WriterCancelledException;
import org.gedcom4j.model.AbstractCitation;
import org.gedcom4j.model.CitationData;
import org.gedcom4j.model.CitationWithSource;
import org.gedcom4j.model.CitationWithoutSource;
import org.gedcom4j.model.MultiStringWithCustomFacts;
import org.gedcom4j.model.Source;

/**
 * Emitter for source citations
 * 
 * @author frizbog
 */
class CitationEmitter extends AbstractEmitter<List<AbstractCitation>> {

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
    protected CitationEmitter(GedcomWriter baseWriter, int startLevel, List<AbstractCitation> writeFrom)
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
        for (AbstractCitation c : writeFrom) {
            if (c instanceof CitationWithoutSource) {
                emitCitationWithoutSource(startLevel, c);
            } else if (c instanceof CitationWithSource) {
                emitCitationWithSource(startLevel, (CitationWithSource) c);
            }
        }
    }

    /**
     * Emit a citation without a source
     * 
     * @param level
     *            the level in the hierarchy at which we are emitting data
     * @param c
     *            the citation
     * @throws GedcomWriterException
     *             when the data is malformed and cannot be written
     */
    private void emitCitationWithoutSource(int level, AbstractCitation c) throws GedcomWriterException {
        CitationWithoutSource cws = (CitationWithoutSource) c;
        emitLinesOfText(level, "SOUR", cws.getDescription());
        if (cws.getTextFromSource() != null) {
            for (List<String> linesOfText : cws.getTextFromSource()) {
                emitLinesOfText(level + 1, "TEXT", linesOfText);
            }
        }
        new MultimediaLinksEmitter(baseWriter, level + 1, cws.getMultimedia()).emit();
        new NoteStructureEmitter(baseWriter, level + 1, cws.getNoteStructures()).emit();
        emitTagIfValueNotNull(level + 1, "QUAY", cws.getCertainty());
        emitCustomFacts(level + 1, cws.getCustomFacts());
    }

    /**
     * Emit a citation with source
     * 
     * @param level
     *            the level in the hierarchy at which we are emitting data
     * @param cws
     *            the citation with source
     * @throws GedcomWriterException
     *             when the data is malformed and cannot be written
     */
    private void emitCitationWithSource(int level, CitationWithSource cws) throws GedcomWriterException {
        Source source = cws.getSource();
        if (source == null || source.getXref() == null || source.getXref().length() == 0) {
            throw new GedcomWriterException("Citation with source must have a source record with an xref/id");
        }
        emitTagWithRequiredValue(level, "SOUR", source.getXref());
        emitTagIfValueNotNull(level + 1, "PAGE", cws.getWhereInSource());
        emitTagIfValueNotNull(level + 1, "EVEN", cws.getEventCited());
        emitTagIfValueNotNull(level + 2, "ROLE", cws.getRoleInEvent());
        if (cws.getData() != null && !cws.getData().isEmpty()) {
            emitTag(level + 1, "DATA");
            for (CitationData cd : cws.getData()) {
                emitTagIfValueNotNull(level + 2, "DATE", cd.getEntryDate());
                for (MultiStringWithCustomFacts linesOfText : cd.getSourceText()) {
                    emitLinesOfText(level + 2, "TEXT", linesOfText);
                }
                emitCustomFacts(level + 2, cd);
            }
        }
        emitTagIfValueNotNull(level + 1, "QUAY", cws.getCertainty());
        new MultimediaLinksEmitter(baseWriter, level + 1, cws.getMultimedia()).emit();
        new NoteStructureEmitter(baseWriter, level + 1, cws.getNoteStructures()).emit();
        emitCustomFacts(level + 1, cws.getCustomFacts());
    }

}
