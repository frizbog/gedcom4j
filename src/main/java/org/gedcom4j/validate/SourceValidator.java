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
package org.gedcom4j.validate;

import java.util.List;

import org.gedcom4j.Options;
import org.gedcom4j.model.EventRecorded;
import org.gedcom4j.model.Multimedia;
import org.gedcom4j.model.RepositoryCitation;
import org.gedcom4j.model.Source;
import org.gedcom4j.model.SourceCallNumber;
import org.gedcom4j.model.SourceData;

/**
 * A validator for {@link Source} objects. See {@link Validator} for usage information.
 * 
 * @author frizbog1
 * 
 */
class SourceValidator extends AbstractValidator {

    /**
     * The source being validated
     */
    private final Source source;

    /**
     * Constructor
     * 
     * @param validator
     *            the root validator
     * @param source
     *            the source being validated
     */
    SourceValidator(Validator validator, Source source) {
        this.validator = validator;
        this.source = source;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validate() {
        if (source == null) {
            addError("Source is null and cannot be validated");
            return;
        }
        checkXref(source);
        checkChangeDate(source.getChangeDate(), source);
        if (source.getData() != null) {
            SourceData sd = source.getData();
            new NotesListValidator(validator, sd).validate();
            mustHaveValueOrBeOmitted(sd, "respAgency");
            List<EventRecorded> eventsRecorded = sd.getEventsRecorded();
            if (eventsRecorded == null) {
                if (validator.isAutorepairEnabled()) {
                    sd.getEventsRecorded(true).clear();
                    addInfo("Collection of recorded events in source data structure was null - autorepaired", sd);
                } else {
                    addError("Collection of recorded events in source data structure is null", sd);
                }
            } else {
                if (validator.isAutorepairEnabled()) {
                    int dups = new DuplicateHandler<>(eventsRecorded).process();
                    if (dups > 0) {
                        validator.addInfo(dups + " duplicate recorded events found and removed", sd);
                    }
                }

                for (EventRecorded er : eventsRecorded) {
                    mustHaveValueOrBeOmitted(er, "datePeriod");
                    mustHaveValueOrBeOmitted(er, "eventType");
                    mustHaveValueOrBeOmitted(er, "jurisdiction");
                }
            }
        }
        List<Multimedia> multimedia = source.getMultimedia();
        if (multimedia == null && Options.isCollectionInitializationEnabled()) {
            if (validator.isAutorepairEnabled()) {
                source.getMultimedia(true).clear();
                addInfo("Multimedia collection on source was null - autorepaired", source);
            }
            addError("Multimedia collection on source is null", source);
        } else {
            if (validator.isAutorepairEnabled()) {
                int dups = new DuplicateHandler<>(multimedia).process();
                if (dups > 0) {
                    validator.addInfo(dups + " duplicate multimedia found and removed", source);
                }
            }

            if (multimedia != null) {
                for (Multimedia mm : multimedia) {
                    new MultimediaValidator(validator, mm).validate();
                }
            }
        }
        new NotesListValidator(validator, source).validate();
        checkStringList(source.getOriginatorsAuthors(), "originators/authors", false);
        checkStringList(source.getPublicationFacts(), "publication facts", false);
        mustHaveValueOrBeOmitted(source, "recIdNumber");
        checkStringList(source.getSourceText(), "source text", true);
        mustHaveValueOrBeOmitted(source, "sourceFiledBy");
        checkStringList(source.getTitle(), "title", true);
        checkUserReferences(source.getUserReferences(), source);

        RepositoryCitation c = source.getRepositoryCitation();
        if (c != null) {
            new NotesListValidator(validator, c).validate();
            mustHaveValue(c, "repositoryXref");
            checkCallNumbers(c);
        }
    }

    /**
     * Check the call numbers on a RepositoryCitation object
     * 
     * @param citation
     *            the citation to check the call numbers on
     */
    private void checkCallNumbers(RepositoryCitation citation) {
        List<SourceCallNumber> callNumbers = citation.getCallNumbers();
        if (callNumbers == null && Options.isCollectionInitializationEnabled()) {
            if (validator.isAutorepairEnabled()) {
                citation.getCallNumbers(true).clear();
                addInfo("Call numbers collection on repository citation was null - autorepaired", citation);
            } else {
                addError("Call numbers collection on repository citation is null", citation);
            }
        } else {
            if (validator.isAutorepairEnabled()) {
                int dups = new DuplicateHandler<>(callNumbers).process();
                if (dups > 0) {
                    validator.addInfo(dups + " duplicate source call numbers found and removed", citation);
                }
            }
            if (callNumbers != null) {
                for (SourceCallNumber scn : callNumbers) {
                    mustHaveValueOrBeOmitted(scn, "callNumber");
                    if (scn.getCallNumber() == null) {
                        if (scn.getMediaType() != null) {
                            addError("You cannot specify media type without a call number in a SourceCallNumber structure", scn);
                        }
                    } else {
                        mustHaveValueOrBeOmitted(scn, "mediaType");
                    }
                }
            }
        }
    }
}
