/*
 * Copyright (c) 2009-2013 Matthew R. Harrah
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
package org.gedcom4j.validate;

import java.util.ArrayList;

import org.gedcom4j.model.EventRecorded;
import org.gedcom4j.model.Multimedia;
import org.gedcom4j.model.RepositoryCitation;
import org.gedcom4j.model.Source;
import org.gedcom4j.model.SourceCallNumber;
import org.gedcom4j.model.SourceData;


/**
 * A validator for {@link Source} objects. See {@link GedcomValidator} for usage
 * information.
 * 
 * @author frizbog1
 * 
 */
class SourceValidator extends AbstractValidator {

    /**
     * The source being validated
     */
    private Source source;

    /**
     * Constructor
     * 
     * @param rootValidator
     *            the root validator
     * @param source
     *            the source being validated
     */
    public SourceValidator(GedcomValidator rootValidator, Source source) {
        this.rootValidator = rootValidator;
        this.source = source;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.gedcom4j.validate.AbstractValidator#validate()
     */
    @Override
    protected void validate() {
        if (source == null) {
            addError("Source is null and cannot be validated");
            return;
        }
        checkXref(source);
        checkChangeDate(source.changeDate, source);
        if (source.data != null) {
            SourceData sd = source.data;
            checkNotes(sd.notes, sd);
            checkOptionalString(sd.respAgency, "responsible agency", sd);
            if (sd.eventsRecorded == null) {
                if (rootValidator.autorepair) {
                    sd.eventsRecorded = new ArrayList<EventRecorded>();
                    addInfo("Collection of recorded events in source data structure was null - autorepaired",
                            sd);
                } else {
                    addError(
                            "Collection of recorded events in source data structure is null",
                            sd);
                }
            } else {
                for (EventRecorded er : sd.eventsRecorded) {
                    checkOptionalString(er.datePeriod, "date period", er);
                    checkOptionalString(er.eventType, "event type", er);
                    checkOptionalString(er.jurisdiction, "jurisdiction", er);
                }
            }
        }
        if (source.multimedia == null) {
            if (rootValidator.autorepair) {
                source.multimedia = new ArrayList<Multimedia>();
                addInfo("Multimedia collection on source was null - autorepaired",
                        source);
            }
            addError("Multimedia collection on source is null", source);
        } else {
            for (Multimedia multimedia : source.multimedia) {
                new MultimediaValidator(rootValidator, multimedia).validate();
            }
        }
        checkNotes(source.notes, source);
        checkStringList(source.originatorsAuthors, "originators/authors", false);
        checkStringList(source.publicationFacts, "publication facts", false);
        checkOptionalString(source.recIdNumber, "automated record id", source);
        checkStringList(source.sourceText, "source text", true);
        checkOptionalString(source.sourceFiledBy, "source filed by", source);
        checkStringList(source.title, "title", true);
        checkUserReferences(source.userReferences, source);

        RepositoryCitation c = source.repositoryCitation;
        if (c != null) {
            checkNotes(c.notes, c);
            checkRequiredString(c.repositoryXref, "repository xref", c);
            checkCallNumbers(c);
        }
    }

    /**
     * Check the call numbers on a RepositoryCitation objct
     * 
     * @param citation
     *            the citation to check the call numbers on
     */
    private void checkCallNumbers(RepositoryCitation citation) {
        if (citation.callNumbers == null) {
            if (rootValidator.autorepair) {
                citation.callNumbers = new ArrayList<SourceCallNumber>();
                addInfo("Call numbers collection on repository citation was null - autorepaired",
                        citation);
            } else {
                addError(
                        "Call numbers collection on repository citation is null",
                        citation);
            }
        } else {
            for (SourceCallNumber scn : citation.callNumbers) {
                checkOptionalString(scn.callNumber, "call number", scn);
                if (scn.callNumber != null) {
                    checkOptionalString(scn.mediaType, "media type", scn);
                } else if (scn.mediaType != null) {
                    addError(
                            "You cannot specify media type without a call number in a SourceCallNumber structure",
                            scn);
                }
            }
        }
    }
}
