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
import org.gedcom4j.model.Family;
import org.gedcom4j.model.FamilyEvent;
import org.gedcom4j.model.Individual;
import org.gedcom4j.model.LdsSpouseSealing;
import org.gedcom4j.model.Submitter;
import org.gedcom4j.model.UserReference;

/**
 * Emitter for {@link Family} objects
 * 
 * @author frizbog
 */
class FamilyEmitter extends AbstractEmitter<Collection<Family>> {

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
    FamilyEmitter(GedcomWriter baseWriter, int startLevel, Collection<Family> writeFrom) throws WriterCancelledException {
        super(baseWriter, startLevel, writeFrom);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void emit() throws GedcomWriterException {
        for (Family f : writeFrom) {
            emitTag(0, f.getXref(), "FAM");
            if (f.getEvents() != null) {
                for (FamilyEvent e : f.getEvents()) {
                    emitFamilyEventStructure(1, e);
                }
            }
            if (f.getHusband() != null) {
                emitTagWithRequiredValue(1, "HUSB", f.getHusband().getXref());
            }
            if (f.getWife() != null) {
                emitTagWithRequiredValue(1, "WIFE", f.getWife().getXref());
            }
            if (f.getChildren() != null) {
                for (Individual i : f.getChildren()) {
                    emitTagWithRequiredValue(1, "CHIL", i.getXref());
                }
            }
            emitTagIfValueNotNull(1, "NCHI", f.getNumChildren());
            if (f.getSubmitters() != null) {
                for (Submitter s : f.getSubmitters()) {
                    emitTagWithRequiredValue(1, "SUBM", s.getXref());
                }
            }
            if (f.getLdsSpouseSealings() != null) {
                for (LdsSpouseSealing s : f.getLdsSpouseSealings()) {
                    emitLdsFamilyOrdinance(1, s);
                }
            }
            emitTagIfValueNotNull(1, "RESN", f.getRestrictionNotice());
            new SourceCitationEmitter(baseWriter, 1, f.getCitations()).emit();
            new MultimediaLinksEmitter(baseWriter, 1, f.getMultimedia()).emit();
            new NotesEmitter(baseWriter, 1, f.getNotes()).emit();
            if (f.getUserReferences() != null) {
                for (UserReference u : f.getUserReferences()) {
                    emitTagWithRequiredValue(1, "REFN", u.getReferenceNum());
                    emitTagIfValueNotNull(2, "TYPE", u.getType());
                }
            }
            emitTagIfValueNotNull(1, "RIN", f.getAutomatedRecordId());
            new ChangeDateEmitter(baseWriter, 1, f.getChangeDate()).emit();
            emitCustomTags(1, f.getCustomFacts());
        }
    }

    /**
     * Emit a family event structure (see FAMILY_EVENT_STRUCTURE in the GEDCOM spec)
     * 
     * @param level
     *            the level we're writing at
     * @param e
     *            the event
     * @throws GedcomWriterException
     *             if the data is malformed and cannot be written
     */
    private void emitFamilyEventStructure(int level, FamilyEvent e) throws GedcomWriterException {
        emitTagWithOptionalValue(level, e.getType().getTag(), e.getYNull());
        new EventEmitter(baseWriter, level + 1, e).emit();
        if (e.getHusbandAge() != null) {
            emitTag(level + 1, "HUSB");
            emitTagWithRequiredValue(level + 2, "AGE", e.getHusbandAge());
        }
        if (e.getWifeAge() != null) {
            emitTag(level + 1, "WIFE");
            emitTagWithRequiredValue(level + 2, "AGE", e.getWifeAge());
        }
    }

    /**
     * Emit the LDS spouse sealing information
     * 
     * @param level
     *            the level we're writing at
     * @param sealings
     *            the {@link LdsSpouseSealing} structure
     * @throws GedcomWriterException
     *             if the data is malformed and cannot be written
     */
    private void emitLdsFamilyOrdinance(int level, LdsSpouseSealing sealings) throws GedcomWriterException {
        emitTag(level, "SLGS");
        emitTagIfValueNotNull(level + 1, "STAT", sealings.getStatus());
        emitTagIfValueNotNull(level + 1, "DATE", sealings.getDate());
        emitTagIfValueNotNull(level + 1, "TEMP", sealings.getTemple());
        emitTagIfValueNotNull(level + 1, "PLAC", sealings.getPlace());
        new SourceCitationEmitter(baseWriter, level + 1, sealings.getCitations()).emit();
        new NotesEmitter(baseWriter, level + 1, sealings.getNotes()).emit();
        emitCustomTags(level + 1, sealings.getCustomFacts());
    }

}
