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

import org.gedcom4j.model.Address;

/**
 * Validator for an {@link Address}. See {@link GedcomValidator} for usage information.
 * 
 * @author frizbog1
 * 
 */
class AddressValidator extends AbstractValidator {

    /**
     * The address being validated
     */
    private final Address address;

    /**
     * Constructor
     * 
     * @param validator
     *            the root validator
     * @param address
     *            the address being validated
     * 
     */
    AddressValidator(Validator validator, Address address) {
        this.validator = validator;
        this.address = address;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validate() {
        checkStringList(address.getLines(), "address lines", false);
        mustHaveValueOrBeOmitted(address.getAddr1(), "line 1", address);
        mustHaveValueOrBeOmitted(address.getAddr2(), "line 2", address);
        mustHaveValueOrBeOmitted(address.getCity(), "city", address);
        mustHaveValueOrBeOmitted(address.getStateProvince(), "state/province", address);
        mustHaveValueOrBeOmitted(address.getPostalCode(), "postal code", address);
        mustHaveValueOrBeOmitted(address.getCountry(), "country", address);
    }

}
