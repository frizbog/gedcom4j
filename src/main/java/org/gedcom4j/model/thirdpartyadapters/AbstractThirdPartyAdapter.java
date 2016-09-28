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

import java.util.ArrayList;
import java.util.List;

import org.gedcom4j.model.AbstractElement;
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
     * <p>
     * Get a list of custom facts from the supplied element, each of which has a tag that matches the value supplied, and has a
     * sub-element named "TYPE" with the value specified. This is similar to generic EVEN or FACT tags, but also supports other
     * custom tags that follow that convention.
     * </p>
     * <p>
     * Example: given a structure like this:
     * </p>
     * 
     * <pre>
     * 0 @I1@ INDI
     * 1 NAME Edward /Example/
     * 1 _FAVF Pizza
     * 2 TYPE Favorite Food
     * </pre>
     * 
     * a concrete adapter might provide a method like this:
     * 
     * <pre>
     * public List<CustomFact> getFavoriteFoods(Individual ind) {
     *    List<CustomFact> favoriteFoods = getCustomTagsWithTagAndType(elem, "_FAVF", "Favorite Food");
     *    return favoriteFoods;
     * </pre>
     * 
     * and the caller could iterate through the {@link CustomFact#getDescription()} property of each value in the resulting list,
     * where "Pizza" would be found in the list.
     * 
     * 
     * @param elem
     *            the element containing custom facts/tags
     * @param tag
     *            the main tag for the custom fact we're looking for
     * @param type
     *            the type of element we are looking for. Must match a child node of the main custom fact's tag,
     * @return a list of custom tags with the specified tag and (sub)type.
     */
    protected List<CustomFact> getCustomTagsWithTagAndType(AbstractElement elem, String tag, String type) {
        List<CustomFact> result = new ArrayList<>();
        if (elem.getCustomFacts() == null) {
            return result;
        }
        for (CustomFact outer : elem.getCustomFacts()) {
            if (!outer.getTag().equals(tag)) {
                continue;
            }
            for (CustomFact inner : outer.getCustomFacts()) {
                if ("TYPE".equals(inner.getTag()) && inner.getDescription() != null && type.equals(inner.getDescription()
                        .getValue())) {
                    result.add(outer);
                    break;
                }
            }
        }
        return result;
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
