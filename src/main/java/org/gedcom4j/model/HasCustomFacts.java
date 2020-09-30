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
package org.gedcom4j.model;

import java.util.List;

/**
 * Marks an object that has custom facts (from custom tags)
 * 
 * @author frizbog
 */
public interface HasCustomFacts extends ModelElement {
    /**
     * Gets the custom facts on this object
     * 
     * @return the custom facts on this object
     */
    List<CustomFact> getCustomFacts();

    /**
     * Get the custom facts on this object, initializing the collection if needed
     * 
     * @param initializeIfNeeded
     *            set to true if you want the backing collection to be instantiated/initialized if it is currently null
     * @return the custom facts on this object, or null if there are none and <var>initializeIfNeeded</var> is false
     */
    List<CustomFact> getCustomFacts(boolean initializeIfNeeded);

    /**
     * Gets the custom facts that have a tag that matches the one supplied
     * 
     * @param tag
     *            the tag we are looking for
     * @return a list of custom facts that have the desired tag. Always returns a list but it might be empty.
     */
    List<CustomFact> getCustomFactsWithTag(String tag);
}
