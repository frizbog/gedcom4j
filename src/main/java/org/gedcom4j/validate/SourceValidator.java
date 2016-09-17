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

import org.gedcom4j.model.EventRecorded;
import org.gedcom4j.model.Multimedia;
import org.gedcom4j.model.RepositoryCitation;
import org.gedcom4j.model.Source;
import org.gedcom4j.model.SourceCallNumber;
import org.gedcom4j.model.SourceData;
import org.gedcom4j.validate.Validator.Finding;

/**
 * A validator for {@link Source} objects. See {@link Validator} for usage information.
 * 
 * @author frizbog1
 * 
 */
class SourceValidator extends AbstractValidator {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -1329075355558483196L;

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
        super(validator);
        this.source = source;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validate() {
        xrefMustBePresentAndWellFormed(source);
        checkChangeDate(source.getChangeDate(), source);
        if (source.getData() != null) {
            SourceData sd = source.getData();
            new NotesListValidator(getValidator(), sd).validate();
            mustHaveValueOrBeOmitted(sd, "respAgency");
            List<EventRecorded> eventsRecorded = sd.getEventsRecorded();
            if (eventsRecorded == null) {
                Finding vf = newFinding(sd, Severity.INFO, ProblemCode.UNINITIALIZED_COLLECTION, "eventsRecorded");
                initializeCollectionIfAllowed(vf);
            } else {
                checkListOfModelElementsForDups(sd, "eventsRecorded");
                checkListOfModelElementsForNulls(sd, "eventsRecorded");

                for (EventRecorded er : eventsRecorded) {
                    mustHaveValueOrBeOmitted(er, "datePeriod");
                    mustHaveValueOrBeOmitted(er, "eventType");
                    mustHaveValueOrBeOmitted(er, "jurisdiction");
                }
            }
        }
        checkUninitializedCollection(source, "multimedia");
        if (source.getMultimedia() != null) {
            checkListOfModelElementsForDups(source, "multimedia");
            checkListOfModelElementsForNulls(source, "multimedia");
            for (Multimedia mm : source.getMultimedia()) {
                new MultimediaValidator(getValidator(), mm).validate();
            }
        }
        new NotesListValidator(getValidator(), source).validate();
        checkStringList(source, "originatorsAuthors", false);
        checkStringList(source, "publicationFacts", false);
        mustHaveValueOrBeOmitted(source, "recIdNumber");
        checkStringList(source, "sourceText", true);
        mustHaveValueOrBeOmitted(source, "sourceFiledBy");
        checkStringList(source, "title", true);
        checkUserReferences(source.getUserReferences(), source);

        RepositoryCitation c = source.getRepositoryCitation();
        if (c != null) {
            new NotesListValidator(getValidator(), c).validate();
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
        checkUninitializedCollection(citation, "callNumbers");
        if (citation.getCallNumbers() != null) {
            checkListOfModelElementsForDups(citation, "callNumbers");
            checkListOfModelElementsForNulls(citation, "callNumbers");
            for (SourceCallNumber scn : citation.getCallNumbers()) {
                mustHaveValueOrBeOmitted(scn, "callNumber");
                if (scn.getCallNumber() == null) {
                    if (scn.getMediaType() != null) {
                        newFinding(scn, Severity.ERROR, ProblemCode.ILLEGAL_VALUE, "mediaType");
                    }
                } else {
                    mustHaveValueOrBeOmitted(scn, "mediaType");
                }
            }
        }
    }
}
