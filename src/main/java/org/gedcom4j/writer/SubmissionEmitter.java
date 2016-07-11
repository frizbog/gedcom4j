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
import org.gedcom4j.exception.WriterCancelledException;
import org.gedcom4j.model.Submission;

/**
 * Emitter for {@link Submission} objects
 * 
 * @author frizbog
 *
 */
class SubmissionEmitter extends AbstractEmitter<Submission> {

    /**
     * @param baseWriter
     * @param startLevel
     * @param writeFrom
     * @throws WriterCancelledException
     */
    SubmissionEmitter(GedcomWriter baseWriter, int startLevel, Submission writeFrom) throws WriterCancelledException {
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
        emitTag(0, writeFrom.getXref(), "SUBN");
        if (writeFrom.getSubmitter() != null) {
            emitTagWithOptionalValue(1, "SUBM", writeFrom.getSubmitter().getXref());
        }
        emitTagIfValueNotNull(1, "FAMF", writeFrom.getNameOfFamilyFile());
        emitTagIfValueNotNull(1, "TEMP", writeFrom.getTempleCode());
        emitTagIfValueNotNull(1, "ANCE", writeFrom.getAncestorsCount());
        emitTagIfValueNotNull(1, "DESC", writeFrom.getDescendantsCount());
        emitTagIfValueNotNull(1, "ORDI", writeFrom.getOrdinanceProcessFlag());
        emitTagIfValueNotNull(1, "RIN", writeFrom.getRecIdNumber());
        emitCustomTags(1, writeFrom.getCustomTags());
    }

}
