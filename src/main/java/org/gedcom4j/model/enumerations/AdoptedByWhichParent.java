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
package org.gedcom4j.model.enumerations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.gedcom4j.Options;
import org.gedcom4j.model.CustomFact;
import org.gedcom4j.model.HasCustomFacts;

/**
 * An enumeration of who adopted a child. Corresponds to the ADOPTED_BY_WHICH_PARENT definition in the GEDCOM specification.
 * 
 * @author frizbog1
 */
public enum AdoptedByWhichParent implements HasCustomFacts {
    /**
     * Both parents adopted
     */
    BOTH,
    /**
     * The husband did the adopting
     */
    HUSB,
    /**
     * The wife did the adopting
     */
    WIFE;

    /**
     * A list of custom facts on this item.
     */
    protected List<CustomFact> customFacts = getCustomFacts(Options.isCollectionInitializationEnabled());

    /**
     * Gets the custom facts.
     *
     * @return the custom facts
     */
    @Override
    public List<CustomFact> getCustomFacts() {
        return customFacts;
    }

    /**
     * Get the custom facts
     * 
     * @param initializeIfNeeded
     *            initialize the collection if needed
     * @return the customFacts
     */
    @Override
    public List<CustomFact> getCustomFacts(boolean initializeIfNeeded) {
        if (initializeIfNeeded && customFacts == null) {
            customFacts = new ArrayList<>(0);
        }
        return customFacts;
    }

    /**
     * Gets the custom facts that have a tag that matches the one supplied
     * 
     * @param tag
     *            the tag we are looking for
     * @return a list of custom facts that have the desired tag. Always returns a list but it might be empty. Although the entries
     *         in the result list are modifiable, the list itself is not.
     */
    @Override
    public List<CustomFact> getCustomFactsWithTag(String tag) {
        List<CustomFact> result = new ArrayList<>();
        if (customFacts != null) {
            for (CustomFact cf : customFacts) {
                if (cf.getTag() != null && cf.getTag().equals(tag)) {
                    result.add(cf);
                }
            }
        }
        return Collections.unmodifiableList(result);
    }
}
