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

import org.gedcom4j.model.HasCustomTags;
import org.gedcom4j.model.StringTree;

/**
 * Base class for third party custom tag adapters
 */
public abstract class AbstractCustomTagsAdapter {

    /**
     * Add dates/places/descriptions of a specific type of custom tag
     * 
     * @param hct
     *            the object that has custom tags
     * @param tag
     *            the tag type we are adding dates, places, and descriptions for
     * @param datesPlacesDescriptions
     *            the dates/places/descriptions we are adding
     */
    protected void addDatePlaceDescriptions(HasCustomTags hct, String tag, List<DatePlaceDescription> datesPlacesDescriptions) {
        List<StringTree> customTags = hct.getCustomTags(true);
        for (DatePlaceDescription dpd : datesPlacesDescriptions) {
            StringTree st = new StringTree();
            if (dpd.getDescription() != null) {
                st.setTag(tag);
                st.setValue(dpd.getDescription());
            }
            if (dpd.getDate() != null) {
                StringTree ch = new StringTree();
                ch.setValue(dpd.getDate());
                ch.setTag("DATE");
                st.getChildren(true).add(ch);
            }
            if (dpd.getPlace() != null) {
                StringTree ch = new StringTree();
                ch.setValue(dpd.getPlace());
                ch.setTag("PLAC");
                st.getChildren(true).add(ch);
            }
            customTags.add(st);
        }
    }

    /**
     * Add values (descriptions) of a specific type of custom tag
     * 
     * @param hct
     *            the object that has custom tags
     * @param tag
     *            the tag type we are adding dates, places, and descriptions for
     * @param descriptions
     *            the descriptions we are adding
     */
    protected void addDescriptions(HasCustomTags hct, String tag, List<String> descriptions) {
        List<StringTree> customTags = hct.getCustomTags(true);
        for (String desc : descriptions) {
            StringTree st = new StringTree();
            st.setTag(tag);
            st.setValue(desc);
            customTags.add(st);
        }
    }

    /**
     * Clear custom tags of a specific type.
     *
     * @param hct
     *            the object that has custom tags
     * @param tag
     *            the tag type to clear from the custom tags.
     */
    protected void clearCustomTagsOfType(HasCustomTags hct, String tag) {
        List<StringTree> customTags = hct.getCustomTags();
        if (customTags != null) {
            for (int i = 0; i < customTags.size();) {
                StringTree st = customTags.get(i);
                if (tag.equals(st.getTag())) {
                    customTags.remove(i);
                } else {
                    i++;
                }
            }
        }
    }

    /**
     * Get a list of {@link DatePlaceDescription} objects from an object that has custom tags, based on a specific tag value
     * 
     * @param hct
     *            the object with custom tags
     * @param tag
     *            the custom tag to look for
     * @return a list of {@link DatePlaceDescription} objects that match the tag requested
     */
    protected List<DatePlaceDescription> getDatePlaceDescriptions(HasCustomTags hct, String tag) {
        List<DatePlaceDescription> result = new ArrayList<>();
        for (StringTree st : getSpecificCustomTags(tag, hct.getCustomTags())) {
            DatePlaceDescription dpd = new DatePlaceDescription();
            result.add(dpd);
            dpd.setDescription(st.getValue());
            if (st.getChildren() != null) {
                // Take the first date available
                List<StringTree> dates = getSpecificCustomTags("DATE", st.getChildren());
                if (!dates.isEmpty()) {
                    dpd.setDate(dates.get(0).getValue());
                }

                // Take the first place available
                List<StringTree> places = getSpecificCustomTags("PLAC", st.getChildren());
                if (!places.isEmpty()) {
                    dpd.setPlace(places.get(0).getValue());
                }
            }
        }
        return result;
    }

    /**
     * Get a list of descriptions (strings) from an object that has custom tags, based on a specific tag value. Expected that the
     * custom tag has no subtags.
     * 
     * @param hct
     *            the object with custom tags
     * @param tag
     *            the custom tag to look for
     * @return a list of {@link String} descriptions that match the tag requested
     */
    protected List<String> getDescriptions(HasCustomTags hct, String tag) {
        List<String> result = new ArrayList<>();
        for (StringTree st : getSpecificCustomTags(tag, hct.getCustomTags())) {
            result.add(st.getValue());
        }
        return result;
    }

    /**
     * Get all the instances of a specific tag type from the custom tags.
     * 
     * @param tag
     *            the tag we're looking for
     * @param hct
     *            the object that has custom tags
     * @return a list of matching custom tags of the type requested. Always returns a non-null list, though it may be empty.
     */
    protected List<StringTree> getSpecificCustomTags(String tag, HasCustomTags hct) {
        List<StringTree> customTags = hct.getCustomTags();
        return getSpecificCustomTags(tag, customTags);
    }

    /**
     * Get all the instances of a specific tag type from the custom tags.
     * 
     * @param tag
     *            the tag we're looking for
     * @param customTags
     *            the custom tags from the object we're examining
     * @return a list of matching custom tags of the type requested. Always returns a non-null list, though it may be empty.
     */
    protected List<StringTree> getSpecificCustomTags(String tag, List<StringTree> customTags) {
        List<StringTree> result = new ArrayList<>();
        if (customTags != null) {
            for (StringTree st : customTags) {
                if (tag.equals(st.getTag())) {
                    result.add(st);
                }
            }
        }
        return result;
    }

    /**
     * Set all the dates/places/descriptions of a specific type of custom tag
     * 
     * @param hct
     *            the object that has custom tags
     * @param tag
     *            the tag type we are setting dates, places, and descriptions for
     * @param datesPlacesDescriptions
     *            the dates/places/descriptions we are setting
     */
    protected void setAllDatePlaceDescription(HasCustomTags hct, String tag, List<DatePlaceDescription> datesPlacesDescriptions) {
        clearCustomTagsOfType(hct, tag);
        addDatePlaceDescriptions(hct, tag, datesPlacesDescriptions);
    }

    /**
     * Set all the values (descriptions) of a specific type of custom tag
     * 
     * @param hct
     *            the object that has custom tags
     * @param tag
     *            the tag type we are setting dates, places, and descriptions for
     * @param descriptions
     *            the descriptions we are setting
     */
    protected void setAllDescriptions(HasCustomTags hct, String tag, List<String> descriptions) {
        clearCustomTagsOfType(hct, tag);
        addDescriptions(hct, tag, descriptions);
    }

}
