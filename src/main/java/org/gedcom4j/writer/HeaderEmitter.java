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
import org.gedcom4j.exception.WriterCancelledException;
import org.gedcom4j.model.Corporation;
import org.gedcom4j.model.Header;
import org.gedcom4j.model.HeaderSourceData;
import org.gedcom4j.model.SourceSystem;
import org.gedcom4j.model.StringWithCustomFacts;

/**
 * Emitter for {@link Header} objects
 * 
 * @author frizbog
 */
class HeaderEmitter extends AbstractEmitter<Header> {

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
    HeaderEmitter(GedcomWriter baseWriter, int startLevel, Header writeFrom) throws WriterCancelledException {
        super(baseWriter, startLevel, writeFrom);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void emit() throws GedcomWriterException {
        Header header = writeFrom;
        if (header == null) {
            header = new Header();
        }
        baseWriter.lines.add("0 HEAD");
        emitSourceSystem(header.getSourceSystem());
        emitTagIfValueNotNull(1, "DEST", header.getDestinationSystem());
        if (header.getDate() != null) {
            emitTagIfValueNotNull(1, "DATE", header.getDate());
            emitTagIfValueNotNull(2, "TIME", header.getTime());
        }
        if (header.getSubmitterReference() != null) {
            emitTagWithRequiredValue(1, "SUBM", header.getSubmitterReference().getSubmitter().getXref());
            emitCustomFacts(2, header.getSubmitterReference());
        }
        if (header.getSubmissionReference() != null) {
            emitTagWithRequiredValue(1, "SUBN", header.getSubmissionReference().getSubmission().getXref());
            emitCustomFacts(2, header.getSubmissionReference());
        }

        emitTagIfValueNotNull(1, "FILE", header.getFileName());

        emitLinesOfText(1, "COPR", header.getCopyrightData());

        emitTag(1, "GEDC");
        emitTagWithRequiredValue(2, "VERS", header.getGedcomVersion().getVersionNumber().toString());
        emitCustomFacts(3, header.getGedcomVersion().getVersionNumber());

        emitTagWithRequiredValue(2, "FORM", header.getGedcomVersion().getGedcomForm());
        emitCustomFacts(2, header.getGedcomVersion());

        emitTagWithRequiredValue(1, "CHAR", header.getCharacterSet().getCharacterSetName());

        emitTagIfValueNotNull(1, "LANG", header.getLanguage());
        if (header.getPlaceHierarchy() != null && header.getPlaceHierarchy().getValue() != null && header.getPlaceHierarchy()
                .getValue().length() > 0) {
            emitTag(1, "PLAC");
            emitTagWithRequiredValue(2, "FORM", header.getPlaceHierarchy());
        }
        new NoteStructureEmitter(baseWriter, 1, header.getNoteStructures()).emit();
        emitCustomFacts(1, header.getCustomFacts());
    }

    /**
     * Write a source system structure (see APPROVED_SYSTEM_ID in the GEDCOM spec)
     * 
     * @param sourceSystem
     *            the source system
     * @throws GedcomWriterException
     *             if data is malformed and cannot be written
     */
    private void emitSourceSystem(SourceSystem sourceSystem) throws GedcomWriterException {
        if (sourceSystem == null) {
            return;
        }
        emitTagWithRequiredValue(1, "SOUR", sourceSystem.getSystemId());
        emitTagIfValueNotNull(2, "VERS", sourceSystem.getVersionNum());
        emitTagIfValueNotNull(2, "NAME", sourceSystem.getProductName());
        Corporation corporation = sourceSystem.getCorporation();
        if (corporation != null) {
            emitTagWithOptionalValue(2, "CORP", corporation.getBusinessName());
            new AddressEmitter(baseWriter, 3, corporation.getAddress()).emit();
            emitStringsWithCustomFacts(3, corporation.getPhoneNumbers(), "PHON");
            emitStringsWithCustomFacts(3, corporation.getFaxNumbers(), "FAX");
            emitStringsWithCustomFacts(3, corporation.getWwwUrls(), "WWW");
            emitStringsWithCustomFacts(3, corporation.getEmails(), "EMAIL");
        }
        HeaderSourceData sourceData = sourceSystem.getSourceData();
        if (sourceData != null) {
            emitTagIfValueNotNull(2, "DATA", new StringWithCustomFacts(sourceData.getName()));
            emitTagIfValueNotNull(3, "DATE", sourceData.getPublishDate());
            emitTagIfValueNotNull(3, "COPR", sourceData.getCopyright());
        }
        emitCustomFacts(2, sourceSystem.getCustomFacts());
    }

}
