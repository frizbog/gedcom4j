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

import org.gedcom4j.model.AbstractEvent;
import org.gedcom4j.model.IndividualAttribute;
import org.gedcom4j.model.Multimedia;
import org.gedcom4j.model.StringWithCustomTags;
import org.gedcom4j.model.enumerations.IndividualAttributeType;

/**
 * Validator for {@link IndividualAttribute} objects
 * 
 * @author frizbog
 */
public class IndividualAttributeValidator extends AbstractValidator {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 9121742173084690556L;

    /**
     * Regexes that match the age formats defined in AGE_AT_EVENT structure
     */
    private static final String[] AGE_FORMATS = { "CHILD", "INFANT", "STILLBORN", "[<>]?\\s?\\d+y", "[<>]?\\s?\\d+m",
            "[<>]\\s?\\d+d", "[<>]\\s?\\d+y \\d+m \\d+d", "[<>]\\s?\\d+y \\d+m", "[<>]\\s?\\d+y \\d+d", "[<>]\\s?\\d+m \\d+d" };

    /**
     * The individual attribute being validated
     */
    private final IndividualAttribute ia;

    /**
     * Constructor
     * 
     * @param validator
     *            the main {@link Validator} class that holds all the findings and results
     * 
     * @param ia
     *            the individual attribute being validated
     */
    public IndividualAttributeValidator(Validator validator, IndividualAttribute ia) {
        super(validator);
        this.ia = ia;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validate() {
        mustHaveValue(ia, "type");
        if (ia.getType() == IndividualAttributeType.FACT) {
            mustHaveValue(ia, "subType");
        }
        if (ia.getType() != IndividualAttributeType.RESIDENCE) {
            mustHaveValue(ia, "description");
        }
        if (ia.getPlace() != null) {
            new PlaceValidator(getValidator(), ia.getPlace()).validate();
        }

        mustBeAgeFormatIfSpecified(ia, ia.getAge(), "age");
        mustHaveValueOrBeOmitted(ia, "cause");
        checkCitations(ia);
        checkCustomTags(ia);
        mustHaveValueOrBeOmitted(ia, "date");
        mustBeDateIfSpecified(ia, "date");
        checkEmails(ia);
        checkFaxNumbers(ia);
        checkMultimedia();
        new NotesListValidator(getValidator(), ia).validate();
        checkPhoneNumbers(ia);
        mustHaveValueOrBeOmitted(ia, "religiousAffiliation");
        mustHaveValueOrBeOmitted(ia, "respAgency");
        mustHaveValueOrBeOmitted(ia, "restrictionNotice");
        if (ia.getPlace() != null) {
            new PlaceValidator(getValidator(), ia.getPlace()).validate();
        }
        checkWwwUrls(ia);

    }

    /**
     * Check the multimedia
     */
    private void checkMultimedia() {
        checkUninitializedCollection(ia, "multimedia");
        List<Multimedia> multimedia = ia.getMultimedia();
        if (multimedia != null) {
            checkListOfModelElementsForDups(ia, "multimedia");
            checkListOfModelElementsForNulls(ia, "multimedia");
            for (Multimedia m : multimedia) {
                new MultimediaValidator(getValidator(), m).validate();
            }
        }
    }

    /**
     * Age values must be in proper format if specified
     * 
     * @param ev
     *            the event
     * @param val
     *            the age value to be checked
     * @param fieldName
     *            the name of the age field
     */
    private void mustBeAgeFormatIfSpecified(AbstractEvent ev, StringWithCustomTags val, String fieldName) {
        if (val == null || !isSpecified(val.getValue())) {
            return;
        }
        String s = val.getValue().trim();
        for (String regex : AGE_FORMATS) {
            if (regex.matches(s)) {
                return;
            }
        }
        newFinding(ev, Severity.ERROR, ProblemCode.ILLEGAL_VALUE, fieldName);
    }

}
