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
package org.gedcom4j.validate;

import java.util.ArrayList;

import org.gedcom4j.model.AbstractCitation;
import org.gedcom4j.model.Event;
import org.gedcom4j.model.Multimedia;
import org.gedcom4j.model.StringWithCustomTags;

/**
 * Validator for events
 * 
 * @author frizbog1
 */
class EventValidator extends AbstractValidator {

    /**
     * The event being validated
     */
    private final Event e;

    /**
     * Constructor
     * 
     * @param rootValidator
     *            the root {@link GedcomValidator} that contains the findings and the settings
     * @param e
     *            the event beign validated
     */
    public EventValidator(GedcomValidator rootValidator, Event e) {
        this.rootValidator = rootValidator;
        this.e = e;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validate() {
        if (e == null) {
            addError("Event is null and cannot be validated or autorepaired");
            return;
        }
        if (e.address != null) {
            new AddressValidator(rootValidator, e.address).validate();
        }
        checkOptionalString(e.age, "age", e);
        checkOptionalString(e.cause, "cause", e);
        if (e.citations == null) {
            if (rootValidator.autorepair) {
                e.citations = new ArrayList<AbstractCitation>();
                rootValidator.addInfo("Event had null list of citations - repaired", e);
            } else {
                rootValidator.addError("Event has null list of citations", e);
                return;
            }
        } else {
            for (AbstractCitation c : e.citations) {
                new CitationValidator(rootValidator, c).validate();
            }
        }
        checkCustomTags(e);
        checkOptionalString(e.date, "date", e);
        if (e.description != null && e.description.trim().length() != 0) {
            rootValidator.addError("Event has description, which is non-standard. Remove this value, or move it (perhaps to a Note).", e);
        }
        if (e.emails == null) {
            if (rootValidator.autorepair) {
                e.emails = new ArrayList<StringWithCustomTags>();
                rootValidator.addInfo("Event had null list of emails - repaired", e);
            } else {
                rootValidator.addError("Event has null list of emails", e);
            }
        } else {
            for (StringWithCustomTags swct : e.emails) {
                checkRequiredString(swct, "email", e);
            }
        }
        if (e.faxNumbers == null) {
            if (rootValidator.autorepair) {
                e.faxNumbers = new ArrayList<StringWithCustomTags>();
                rootValidator.addInfo("Event had null list of fax numbers - repaired", e);
            } else {
                rootValidator.addError("Event has null list of fax numbers", e);
            }
        } else {
            for (StringWithCustomTags swct : e.faxNumbers) {
                checkRequiredString(swct, "fax number", e);
            }
        }
        if (e.multimedia == null) {
            if (rootValidator.autorepair) {
                e.multimedia = new ArrayList<Multimedia>();
                rootValidator.addInfo("Event had null list of multimedia - repaired", e);
            } else {
                rootValidator.addError("Event has null list of multimedia", e);
            }
        } else {
            for (Multimedia m : e.multimedia) {
                new MultimediaValidator(rootValidator, m).validate();
            }
        }
        checkNotes(e.notes, e);
        if (e.phoneNumbers == null) {
            if (rootValidator.autorepair) {
                e.faxNumbers = new ArrayList<StringWithCustomTags>();
                rootValidator.addInfo("Event had null list of phone numbers - repaired", e);
            } else {
                rootValidator.addError("Event has null list of phone numbers", e);
            }
        } else {
            for (StringWithCustomTags swct : e.phoneNumbers) {
                checkRequiredString(swct, "phone numbe", e);
            }
        }
        if (e.place != null) {
            new PlaceValidator(rootValidator, e.place).validate();
        }
        checkOptionalString(e.religiousAffiliation, "religious affiliation", e);
        checkOptionalString(e.respAgency, "responsible agency", e);
        checkOptionalString(e.restrictionNotice, "restriction notice", e);
        checkOptionalString(e.subType, "subtype", e);
        if (e.wwwUrls == null) {
            if (rootValidator.autorepair) {
                e.wwwUrls = new ArrayList<StringWithCustomTags>();
                rootValidator.addInfo("Event had null list of www urls - repaired", e);
            } else {
                rootValidator.addError("Event has null list of www url", e);
            }
        } else {
            for (StringWithCustomTags swct : e.wwwUrls) {
                checkRequiredString(swct, "www url", e);
            }
        }

    }
}
