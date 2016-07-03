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
     * @param rootValidator
     *            the root validator
     * @param address
     *            the address being validated
     * 
     */
    public AddressValidator(GedcomValidator rootValidator, Address address) {
        this.rootValidator = rootValidator;
        this.address = address;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validate() {
        checkStringList(address.getLines(), "address lines", false);
        checkOptionalString(address.getAddr1(), "line 1", address);
        checkOptionalString(address.getAddr2(), "line 2", address);
        checkOptionalString(address.getCity(), "city", address);
        checkOptionalString(address.getStateProvince(), "state/province", address);
        checkOptionalString(address.getPostalCode(), "postal code", address);
        checkOptionalString(address.getCountry(), "country", address);
    }

}
