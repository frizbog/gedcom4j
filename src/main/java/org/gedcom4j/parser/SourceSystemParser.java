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

import org.gedcom4j.model.*;

/**
 * A parser for {@link SourceSystem} objects
 * 
 * @author frizbog
 */
class SourceSystemParser extends AbstractParser<SourceSystem> {

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
    SourceSystemParser(GedcomParser gedcomParser, StringTree stringTree, SourceSystem loadInto) {
        super(gedcomParser, stringTree, loadInto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void parse() {
        loadInto.setSystemId(stringTree.getValue());
        if (stringTree.getChildren() != null) {
            for (StringTree ch : stringTree.getChildren()) {
                if (Tag.VERSION.equalsText(ch.getTag())) {
                    loadInto.setVersionNum(new StringWithCustomTags(ch));
                } else if (Tag.NAME.equalsText(ch.getTag())) {
                    loadInto.setProductName(new StringWithCustomTags(ch));
                } else if (Tag.CORPORATION.equalsText(ch.getTag())) {
                    Corporation corporation = new Corporation();
                    loadInto.setCorporation(corporation);
                    new CorporationParser(gedcomParser, ch, corporation).parse();
                } else if (Tag.DATA_FOR_CITATION.equalsText(ch.getTag())) {
                    HeaderSourceData headerSourceData = new HeaderSourceData();
                    loadInto.setSourceData(headerSourceData);
                    new HeaderSourceDataParser(gedcomParser, ch, headerSourceData).parse();
                } else {
                    unknownTag(ch, loadInto);
                }
            }
        }
    }

}
