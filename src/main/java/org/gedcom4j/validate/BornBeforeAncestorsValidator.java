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

import java.util.Date;

import org.gedcom4j.model.Individual;
import org.gedcom4j.model.IndividualEvent;
import org.gedcom4j.model.enumerations.IndividualEventType;
import org.gedcom4j.parser.DateParser;
import org.gedcom4j.parser.DateParser.ImpreciseDatePreference;

/**
 * Validator that finds people with birthdates earlier than their ancestors.
 * 
 * @author frizbog
 */
public class BornBeforeAncestorsValidator extends AbstractValidator {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -9011648733929347548L;

    /**
     * Constructor
     * 
     * @param validator
     *            the {@link Validator} that orchestrates validation and tracks results
     */
    public BornBeforeAncestorsValidator(Validator validator) {
        super(validator);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validate() {
        DateParser dp = new DateParser();
        for (Individual i : getValidator().getGedcom().getIndividuals().values()) {
            IndividualEvent ib = getEarliestEventOfType(i, IndividualEventType.BIRTH);
            if (ib == null || ib.getDate() == null || ib.getDate().getValue() == null) {
                continue;
            }
            Date ibd = dp.parse(ib.getDate().getValue(), ImpreciseDatePreference.FAVOR_EARLIEST);
            if (ibd == null) {
                continue;
            }
            for (Individual a : i.getAncestors()) {
                if (a == i) { // NOPMD - deliberate checking if same individual
                    continue;
                }
                IndividualEvent ab = getLatestEventOfType(a, IndividualEventType.BIRTH);
                if (ab == null || ab.getDate() == null || ab.getDate().getValue() == null) {
                    continue;
                }
                Date abd = dp.parse(ab.getDate().getValue(), ImpreciseDatePreference.FAVOR_LATEST);
                if (abd != null && abd.after(ibd)) {
                    newFinding(i, Severity.WARNING, ProblemCode.DESCENDANT_BORN_BEFORE_ANCESTOR, null).getRelatedItems(true).add(a);
                }
            }
        }
    }

}
