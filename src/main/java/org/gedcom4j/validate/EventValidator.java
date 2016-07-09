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

import org.gedcom4j.Options;
import org.gedcom4j.model.AbstractCitation;
import org.gedcom4j.model.AbstractEvent;
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
    private final AbstractEvent e;

    /**
     * Constructor
     * 
     * @param rootValidator
     *            the root {@link GedcomValidator} that contains the findings and the settings
     * @param e
     *            the event beign validated
     */
    public EventValidator(GedcomValidator rootValidator, AbstractEvent e) {
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
        if (e.getAddress() != null) {
            new AddressValidator(rootValidator, e.getAddress()).validate();
        }
        checkOptionalString(e.getAge(), "age", e);
        checkOptionalString(e.getCause(), "cause", e);
        if (e.getCitations() == null && Options.isCollectionInitializationEnabled()) {
            if (rootValidator.isAutorepairEnabled()) {
                e.getCitations(true).clear();
                rootValidator.addInfo("Event had null list of citations - repaired", e);
            } else {
                rootValidator.addError("Event has null list of citations", e);
                return;
            }
        } else {
            if (e.getCitations() != null) {
                for (AbstractCitation c : e.getCitations()) {
                    new CitationValidator(rootValidator, c).validate();
                }
            }
        }
        checkCustomTags(e);
        checkOptionalString(e.getDate(), "date", e);
        if (e.getDescription() != null && e.getDescription().trim().length() != 0) {
            rootValidator.addError("Event has description, which is non-standard. Remove this value, or move it (perhaps to a Note).", e);
        }
        if (e.getEmails() == null && Options.isCollectionInitializationEnabled()) {
            if (rootValidator.isAutorepairEnabled()) {
                e.getEmails(true).clear();
                rootValidator.addInfo("Event had null list of emails - repaired", e);
            } else {
                rootValidator.addError("Event has null list of emails", e);
            }
        } else {
            if (e.getEmails() != null) {
                for (StringWithCustomTags swct : e.getEmails()) {
                    checkRequiredString(swct, "email", e);
                }
            }
        }
        if (e.getFaxNumbers() == null && Options.isCollectionInitializationEnabled()) {
            if (rootValidator.isAutorepairEnabled()) {
                e.getFaxNumbers(true).clear();
                rootValidator.addInfo("Event had null list of fax numbers - repaired", e);
            } else {
                rootValidator.addError("Event has null list of fax numbers", e);
            }
        } else {
            if (e.getFaxNumbers() != null) {
                for (StringWithCustomTags swct : e.getFaxNumbers()) {
                    checkRequiredString(swct, "fax number", e);
                }
            }
        }
        if (e.getMultimedia() == null && Options.isCollectionInitializationEnabled()) {
            if (rootValidator.isAutorepairEnabled()) {
                e.getMultimedia(true).clear();
                rootValidator.addInfo("Event had null list of multimedia - repaired", e);
            } else {
                rootValidator.addError("Event has null list of multimedia", e);
            }
        } else {
            if (e.getMultimedia() != null) {
                for (Multimedia m : e.getMultimedia()) {
                    new MultimediaValidator(rootValidator, m).validate();
                }
            }
        }
        checkNotes(e.getNotes(), e);
        if (e.getPhoneNumbers() == null && Options.isCollectionInitializationEnabled()) {
            if (rootValidator.isAutorepairEnabled()) {
                e.getPhoneNumbers(true).clear();
                rootValidator.addInfo("Event had null list of phone numbers - repaired", e);
            } else {
                rootValidator.addError("Event has null list of phone numbers", e);
            }
        } else {
            if (e.getPhoneNumbers() != null) {
                for (StringWithCustomTags swct : e.getPhoneNumbers()) {
                    checkRequiredString(swct, "phone number", e);
                }
            }
        }
        if (e.getPlace() != null) {
            new PlaceValidator(rootValidator, e.getPlace()).validate();
        }
        checkOptionalString(e.getReligiousAffiliation(), "religious affiliation", e);
        checkOptionalString(e.getRespAgency(), "responsible agency", e);
        checkOptionalString(e.getRestrictionNotice(), "restriction notice", e);
        checkOptionalString(e.getSubType(), "subtype", e);
        if (e.getWwwUrls() == null && Options.isCollectionInitializationEnabled()) {
            if (rootValidator.isAutorepairEnabled()) {
                e.getWwwUrls(true).clear();
                rootValidator.addInfo("Event had null list of www urls - repaired", e);
            } else {
                rootValidator.addError("Event has null list of www url", e);
            }
        } else {
            if (e.getWwwUrls() != null) {
                for (StringWithCustomTags swct : e.getWwwUrls()) {
                    checkRequiredString(swct, "www url", e);
                }
            }
        }

    }
}
