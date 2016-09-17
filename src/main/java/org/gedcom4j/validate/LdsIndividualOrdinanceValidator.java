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

import org.gedcom4j.model.LdsIndividualOrdinance;
import org.gedcom4j.model.enumerations.LdsBaptismDateStatus;
import org.gedcom4j.model.enumerations.LdsChildSealingDateStatus;
import org.gedcom4j.model.enumerations.LdsEndowmentDateStatus;

/**
 * Validator for an {@link LdsIndividualOrdinance}
 * 
 * @author frizbog
 */
public class LdsIndividualOrdinanceValidator extends AbstractValidator {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -4147867708714711190L;

    /**
     * The ordinance being validated
     */
    private final LdsIndividualOrdinance o;

    /**
     * Constructor
     * 
     * @param validator
     *            the base {@link Validator} that tracks errors, etc.
     * @param o
     *            the ordinance to validate
     */
    public LdsIndividualOrdinanceValidator(Validator validator, LdsIndividualOrdinance o) {
        super(validator);
        this.o = o;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validate() {
        checkCitations(o);
        checkCustomTags(o);
        checkNotes(o);
        mustHaveValue(o, "type");
        mustNotHaveValue(o, "yNull");
        mustHaveValueOrBeOmitted(o, "date");
        mustBeDateIfSpecified(o, "date");
        mustHaveValueOrBeOmitted(o, "temple");
        mustHaveValueOrBeOmitted(o, "place");
        mustHaveValueOrBeOmitted(o, "status");
        if (o.getStatus() != null) {
            mustHaveValue(o, "date");
        }
        if (o.getType() != null) {
            switch (o.getType()) {
                case BAPTISM:
                case CONFIRMATION:
                    mustBeInEnumIfSpecified(LdsBaptismDateStatus.class, o, "status");
                    break;
                case CHILD_SEALING:
                    mustBeInEnumIfSpecified(LdsChildSealingDateStatus.class, o, "status");
                    mustHaveValue(o, "familyWhereChild");
                    break;
                case ENDOWMENT:
                    mustBeInEnumIfSpecified(LdsEndowmentDateStatus.class, o, "status");
                    break;
                default:
                    break;
            }
        }

    }

}
