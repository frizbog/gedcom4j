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

import org.gedcom4j.model.LdsSpouseSealing;
import org.gedcom4j.model.enumerations.LdsSpouseSealingDateStatus;

/**
 * Validator for {@link LdsSpouseSealing} objects
 * 
 * @author frizbog1
 * 
 */
class LdsSpouseSealingValidator extends AbstractValidator {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -7894442750246320800L;

    /**
     * The sealing being validated
     */
    private final LdsSpouseSealing s;

    /**
     * Constructor
     * 
     * @param validator
     *            the {@link Validator} that contains findings and settings
     * @param s
     *            the sealing being validated
     */
    LdsSpouseSealingValidator(Validator validator, LdsSpouseSealing s) {
        super(validator);
        this.s = s;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validate() {
        checkCitations(s);
        checkCustomFacts(s);
        new NoteStructureListValidator(getValidator(), s).validate();
        mustHaveValueOrBeOmitted(s, "place");
        mustBeInEnumIfSpecified(LdsSpouseSealingDateStatus.class, s, "status");
        if (s.getStatus() != null && isSpecified(s.getStatus().getValue())) {
            mustHaveValue(s, "date");
            mustBeDateIfSpecified(s, "date");
        } else {
            mustNotHaveValue(s, "date");
        }
        mustHaveValueOrBeOmitted(s, "temple");
    }

}
