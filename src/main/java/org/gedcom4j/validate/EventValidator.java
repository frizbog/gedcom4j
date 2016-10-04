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
import org.gedcom4j.model.FamilyEvent;
import org.gedcom4j.model.IndividualEvent;
import org.gedcom4j.model.ModelElement;
import org.gedcom4j.model.Multimedia;
import org.gedcom4j.model.NoteStructure;
import org.gedcom4j.model.StringWithCustomFacts;
import org.gedcom4j.model.enumerations.FamilyEventType;
import org.gedcom4j.model.enumerations.IndividualEventType;
import org.gedcom4j.model.enumerations.RestrictionNoticeType;
import org.gedcom4j.validate.Validator.Finding;

/**
 * Validator for events
 * 
 * @author frizbog1
 */
class EventValidator extends AbstractValidator {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -1272765738333620248L;

    /**
     * Regexes that match the age formats defined in AGE_AT_EVENT structure
     */
    private static final String[] AGE_FORMATS = { "CHILD", "INFANT", "STILLBORN", "[<>]?\\s?\\d+y", "[<>]?\\s?\\d+m",
            "[<>]\\s?\\d+d", "[<>]\\s?\\d+y \\d+m \\d+d", "[<>]\\s?\\d+y \\d+m", "[<>]\\s?\\d+y \\d+d", "[<>]\\s?\\d+m \\d+d" };

    /**
     * The event being validated
     */
    private final AbstractEvent e;

    /**
     * Constructor
     * 
     * @param validator
     *            the {@link Validator} that contains the findings and the settings
     * @param e
     *            the event beign validated
     */
    EventValidator(Validator validator, AbstractEvent e) {
        super(validator);
        this.e = e;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("PMD.ExcessiveMethodLength")
    protected void validate() {
        if (e instanceof IndividualEvent) {
            IndividualEvent ie = (IndividualEvent) e;
            mustHaveValue(ie, "type");
            if (ie.getType() == IndividualEventType.BIRTH || ie.getType() == IndividualEventType.CHRISTENING || ie
                    .getType() == IndividualEventType.DEATH) {
                mustHaveValueOrBeOmitted(ie, "yNull");
            } else {
                mustNotHaveValue(ie, "yNull");
            }
            if (ie.getType() != IndividualEventType.BIRTH && ie.getType() != IndividualEventType.CHRISTENING && ie
                    .getType() != IndividualEventType.ADOPTION) {
                mustNotHaveValue(ie, "family");
            }
        } else if (e instanceof FamilyEvent) {
            FamilyEvent fe = (FamilyEvent) e;
            mustHaveValue(fe, "type");
            if (fe.getType() == FamilyEventType.MARRIAGE) {
                mustHaveValueOrBeOmitted(fe, "yNull");
            } else {
                mustNotHaveValue(fe, "yNull");
            }
            if (fe.getType() == FamilyEventType.EVENT) {
                mustHaveValueOrBeOmitted(fe, "description");
            } else {
                mustNotHaveValue(fe, "description");
            }
            mustBeAgeFormatIfSpecified(fe, fe.getHusbandAge(), "husbandAge");
            mustBeAgeFormatIfSpecified(fe, fe.getWifeAge(), "husbandAge");
            mustNotHaveValue(fe, "age");
        }
        if (e.getAddress() != null) {
            new AddressValidator(getValidator(), e.getAddress()).validate();
        }
        mustBeAgeFormatIfSpecified(e, e.getAge(), "age");
        mustHaveValueOrBeOmitted(e, "cause");
        checkCitations(e);
        checkCustomFacts(e);
        mustHaveValueOrBeOmitted(e, "date");
        mustBeDateIfSpecified(e, "date");
        if (e.getDescription() != null && e.getDescription().trim().length() != 0 && !"Y".equals(e.getDescription().trim())) {
            Finding vf = newFinding(e, Severity.ERROR, ProblemCode.ILLEGAL_VALUE, "description");
            if (mayRepair(vf)) {
                ModelElement before = makeCopy(e);
                NoteStructure n = new NoteStructure();
                n.getLines(true).add(e.getDescription().getValue());
                e.getNoteStructures(true).add(n);
                e.getDescription().setValue(null);
                vf.addRepair(new AutoRepair(before, makeCopy(e)));
            }
        }
        checkEmails(e);
        checkFaxNumbers(e);
        checkMultimedia();
        new NoteStructureListValidator(getValidator(), e).validate();
        checkPhoneNumbers(e);
        mustHaveValueOrBeOmitted(e, "religiousAffiliation");
        mustHaveValueOrBeOmitted(e, "respAgency");
        mustHaveValueOrBeOmitted(e, "restrictionNotice");
        if (e.getRestrictionNotice() != null) {
            mustBeInEnumIfSpecified(RestrictionNoticeType.class, e, "restrictionNotice");
        }
        if (e.getPlace() != null) {
            new PlaceValidator(getValidator(), e.getPlace()).validate();
        }
        checkWwwUrls(e);

    }

    /**
     * Check the multimedia
     */
    private void checkMultimedia() {
        checkUninitializedCollection(e, "multimedia");
        List<Multimedia> multimedia = e.getMultimedia();
        if (multimedia != null) {
            checkListOfModelElementsForDups(e, "multimedia");
            checkListOfModelElementsForNulls(e, "multimedia");
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
    private void mustBeAgeFormatIfSpecified(AbstractEvent ev, StringWithCustomFacts val, String fieldName) {
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
