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
import java.util.List;

import org.gedcom4j.model.Individual;
import org.gedcom4j.model.IndividualEvent;
import org.gedcom4j.model.enumerations.IndividualEventType;
import org.gedcom4j.parser.DateParser;

/**
 * Validator that checks for birth or death dates in the future
 * 
 * @author frizbog
 */
public class FutureBirthOrDeathValidator extends AbstractValidator {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -8668522986153083890L;

    /**
     * Date parser
     */
    private final DateParser dp = new DateParser();

    /**
     * Right now
     */
    private final Date now = new Date();

    /**
     * Constructor
     * 
     * @param validator
     *            the main {@link Validator} that orchestrates validation and collects results
     * 
     */
    public FutureBirthOrDeathValidator(Validator validator) {
        super(validator);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validate() {
        for (Individual i : getValidator().getGedcom().getIndividuals().values()) {
            List<IndividualEvent> births = i.getEventsOfType(IndividualEventType.BIRTH);
            for (IndividualEvent b : births) {
                if (b.getDate() != null && b.getDate().getValue() != null && !b.getDate().getValue().isEmpty()) {
                    String dateString = b.getDate().getValue();
                    Date bd = dp.parse(dateString);
                    if (bd != null && now.before(bd)) {
                        newFinding(b, Severity.ERROR, ProblemCode.DATE_IN_FUTURE, "date").getRelatedItems(true).add(i);
                    }
                }
            }
            List<IndividualEvent> deaths = i.getEventsOfType(IndividualEventType.DEATH);
            for (IndividualEvent d : deaths) {
                if (d.getDate() != null && d.getDate().getValue() != null && !d.getDate().getValue().isEmpty()) {
                    String dateString = d.getDate().getValue();
                    Date dd = dp.parse(dateString);
                    if (dd != null && now.before(dd)) {
                        newFinding(d, Severity.ERROR, ProblemCode.DATE_IN_FUTURE, "date").getRelatedItems(true).add(i);
                    }
                }
            }
        }
    }

}
