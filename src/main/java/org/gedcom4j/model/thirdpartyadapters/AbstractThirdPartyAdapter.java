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
package org.gedcom4j.model.thirdpartyadapters;

import java.util.List;

import org.gedcom4j.model.CustomFact;
import org.gedcom4j.model.HasCustomFacts;

/**
 * Base class for third party adapter classes
 * 
 * @author frizbog
 */
public abstract class AbstractThirdPartyAdapter {

    /**
     * Clear custom facts of a specific type.
     *
     * @param hct
     *            the object that has custom facts
     * @param tag
     *            the tag type to clear from the custom facts.
     */
    protected void clearCustomTagsOfType(HasCustomFacts hct, String tag) {
        List<CustomFact> customFacts = hct.getCustomFacts();
        if (customFacts != null) {
            for (int i = 0; i < customFacts.size();) {
                CustomFact cf = customFacts.get(i);
                if (tag.equals(cf.getTag())) {
                    customFacts.remove(i);
                } else {
                    i++;
                }
            }
        }
    }

    /**
     * Replaces all the custom facts of a specific type with a list of other custom facts
     * 
     * @param hct
     *            the object that has custom facts to be replaced and added to. Required.
     * @param tag
     *            the tag of the facts being removed from the object. Required.
     * @param facts
     *            the facts being added to the object. Optional.
     */
    protected void replaceAllCustomFactsOfTypeWithNewFacts(HasCustomFacts hct, String tag, List<CustomFact> facts) {
        clearCustomTagsOfType(hct, tag);
        if (facts != null && !facts.isEmpty()) {
            hct.getCustomFacts(true).addAll(facts);
        }
    }
}
