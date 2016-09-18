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

import org.gedcom4j.model.Family;
import org.gedcom4j.model.Individual;

/**
 * Validator that looks for male wives and female husbands which are probably transposition errors.
 * 
 * @author frizbog
 */
public class MaleWivesFemaleHusbandsValidator extends AbstractValidator {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -4550089051329074667L;

    /**
     * Constructor
     * 
     * @param validator
     *            the main {@link Validator} that orchestrates validation and tracks results
     */
    public MaleWivesFemaleHusbandsValidator(Validator validator) {
        super(validator);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validate() {
        for (Family f : getValidator().getGedcom().getFamilies().values()) {
            Individual w = f.getWife();
            Individual h = f.getHusband();
            if (w != null && w.getSex() != null && "M".equals(w.getSex().getValue())) {
                newFinding(f, Severity.WARNING, ProblemCode.WIFE_IS_MALE, "wife");
            }
            if (h != null && h.getSex() != null && "F".equals(h.getSex().getValue())) {
                newFinding(f, Severity.WARNING, ProblemCode.HUSBAND_IS_FEMALE, "husband");
            }
        }

    }

}
