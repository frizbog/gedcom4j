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

import org.gedcom4j.exception.GedcomWriterException;
import org.gedcom4j.exception.GedcomWriterVersionDataMismatchException;
import org.gedcom4j.exception.WriterCancelledException;
import org.gedcom4j.model.AbstractNameVariation;
import org.gedcom4j.model.Place;

/**
 * @author frizbog
 *
 */
class PlaceEmitter extends AbstractEmitter<Place> {

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
    PlaceEmitter(GedcomWriter baseWriter, int startLevel, Place writeFrom) throws WriterCancelledException {
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
        emitTagWithOptionalValue(startLevel, "PLAC", writeFrom.getPlaceName());
        emitTagIfValueNotNull(startLevel + 1, "FORM", writeFrom.getPlaceFormat());
        new SourceCitationEmitter(baseWriter, startLevel + 1, writeFrom.getCitations()).emit();
        new NotesEmitter(baseWriter, startLevel + 1, writeFrom.getNotes()).emit();
        if (writeFrom.getRomanized() != null) {
            for (AbstractNameVariation nv : writeFrom.getRomanized()) {
                if (g55()) {
                    throw new GedcomWriterVersionDataMismatchException(
                            "GEDCOM version is 5.5, but romanized variation was specified on place " + writeFrom.getPlaceName()
                                    + ", which is only allowed in GEDCOM 5.5.1");
                }
                emitTagWithRequiredValue(startLevel + 1, "ROMN", nv.getVariation());
                emitTagIfValueNotNull(startLevel + 2, "TYPE", nv.getVariationType());
            }
        }
        if (writeFrom.getPhonetic() != null) {
            for (AbstractNameVariation nv : writeFrom.getPhonetic()) {
                if (g55()) {
                    throw new GedcomWriterVersionDataMismatchException(
                            "GEDCOM version is 5.5, but phonetic variation was specified on place " + writeFrom.getPlaceName()
                                    + ", which is only allowed in GEDCOM 5.5.1");
                }
                emitTagWithRequiredValue(startLevel + 1, "FONE", nv.getVariation());
                emitTagIfValueNotNull(startLevel + 2, "TYPE", nv.getVariationType());
            }
        }
        if (writeFrom.getLatitude() != null || writeFrom.getLongitude() != null) {
            emitTag(startLevel + 1, "MAP");
            emitTagWithRequiredValue(startLevel + 2, "LATI", writeFrom.getLatitude());
            emitTagWithRequiredValue(startLevel + 2, "LONG", writeFrom.getLongitude());
            if (g55()) {
                throw new GedcomWriterVersionDataMismatchException(
                        "GEDCOM version is 5.5, but map coordinates were specified on place " + writeFrom.getPlaceName()
                                + ", which is only allowed in GEDCOM 5.5.1");
            }
        }
        emitCustomFacts(startLevel + 1, writeFrom.getCustomFacts());
    }

}
