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

import org.gedcom4j.model.AbstractEvent;
import org.gedcom4j.model.StringTree;
import org.gedcom4j.model.StringWithCustomFacts;

/**
 * A base class for an event parser.
 * 
 * @author frizbog
 * @param <T>
 *            The type of event that is being parsed
 */
public abstract class AbstractEventParser<T extends AbstractEvent> extends AbstractParser<T> {

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
    public AbstractEventParser(GedcomParser gedcomParser, StringTree stringTree, T loadInto) {
        super(gedcomParser, stringTree, loadInto);
    }

    /**
     * Parse the Y or Null portion of the event after the tag
     */
    protected void parseYNull() {
        if ("Y".equals(stringTree.getValue())) {
            loadInto.setYNull(stringTree.getValue());
            loadInto.setDescription((String) null);
        } else if (stringTree.getValue() == null || stringTree.getValue().trim().length() == 0) {
            loadInto.setYNull(null);
            loadInto.setDescription((String) null);
        } else {
            loadInto.setYNull(null);
            loadInto.setDescription(new StringWithCustomFacts(stringTree.getValue()));
            addWarning(stringTree.getTag() + " tag had description rather than [Y|<NULL>] - violates standard");
        }
    }

}