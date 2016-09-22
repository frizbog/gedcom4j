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
package org.gedcom4j.parser;

import org.gedcom4j.model.Address;
import org.gedcom4j.model.StringTree;

/**
 * Parser for {@link Address} objects
 * 
 * @author frizbog
 *
 */
class AddressParser extends AbstractParser<Address> {

    /**
     * Constructor
     * 
     * @param gedcomParser
     *            a reference to the root {@link GedcomParser}
     * @param stringTree
     *            {@link StringTree} to be parsed
     * @param loadInto
     *            the object we are loading data into
     */
    AddressParser(GedcomParser gedcomParser, StringTree stringTree, Address loadInto) {
        super(gedcomParser, stringTree, loadInto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void parse() {
        if (stringTree.getValue() != null) {
            loadInto.getLines(true).add(stringTree.getValue());
        }
        if (stringTree.getChildren() != null) {
            for (StringTree ch : stringTree.getChildren()) {
                if (Tag.ADDRESS_1.equalsText(ch.getTag())) {
                    loadInto.setAddr1(parseStringWithCustomFacts(ch));
                } else if (Tag.ADDRESS_2.equalsText(ch.getTag())) {
                    loadInto.setAddr2(parseStringWithCustomFacts(ch));
                } else if (Tag.CITY.equalsText(ch.getTag())) {
                    loadInto.setCity(parseStringWithCustomFacts(ch));
                } else if (Tag.STATE.equalsText(ch.getTag())) {
                    loadInto.setStateProvince(parseStringWithCustomFacts(ch));
                } else if (Tag.POSTAL_CODE.equalsText(ch.getTag())) {
                    loadInto.setPostalCode(parseStringWithCustomFacts(ch));
                } else if (Tag.COUNTRY.equalsText(ch.getTag())) {
                    loadInto.setCountry(parseStringWithCustomFacts(ch));
                } else if (Tag.CONCATENATION.equalsText(ch.getTag())) {
                    if (loadInto.getLines(true).isEmpty()) {
                        loadInto.getLines().add(ch.getValue());
                    } else {
                        loadInto.getLines().set(loadInto.getLines().size() - 1, loadInto.getLines().get(loadInto.getLines().size()
                                - 1) + ch.getValue());
                    }
                } else if (Tag.CONTINUATION.equalsText(ch.getTag())) {
                    loadInto.getLines(true).add(ch.getValue() == null ? "" : ch.getValue());
                } else {
                    unknownTag(ch, loadInto);
                }
            }
        }
    }

}
