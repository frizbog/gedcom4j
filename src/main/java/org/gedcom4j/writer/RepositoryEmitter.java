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
import org.gedcom4j.model.Repository;
import org.gedcom4j.model.UserReference;

/**
 * @author frizbog
 *
 */
class RepositoryEmitter extends AbstractEmitter<Collection<Repository>> {

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
    RepositoryEmitter(GedcomWriter baseWriter, int startLevel, Collection<Repository> writeFrom) throws WriterCancelledException {
        super(baseWriter, startLevel, writeFrom);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void emit() throws GedcomWriterException {
        for (Repository r : writeFrom) {
            emitTag(0, r.getXref(), "REPO");
            emitTagIfValueNotNull(1, "NAME", r.getName());
            new AddressEmitter(baseWriter, 1, r.getAddress()).emit();
            new NoteStructureEmitter(baseWriter, 1, r.getNoteStructures()).emit();
            if (r.getUserReferences() != null) {
                for (UserReference u : r.getUserReferences()) {
                    emitTagWithRequiredValue(1, "REFN", u.getReferenceNum());
                    emitTagIfValueNotNull(2, "TYPE", u.getType());
                }
            }
            emitTagIfValueNotNull(1, "RIN", r.getRecIdNumber());
            emitStringsWithCustomFacts(1, r.getPhoneNumbers(), "PHON");
            emitStringsWithCustomFacts(1, r.getWwwUrls(), "WWW");
            emitStringsWithCustomFacts(1, r.getFaxNumbers(), "FAX");
            emitStringsWithCustomFacts(1, r.getEmails(), "EMAIL");
            new ChangeDateEmitter(baseWriter, 1, r.getChangeDate()).emit();
            emitCustomFacts(1, r.getCustomFacts());
        }
    }

}
