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

import org.gedcom4j.model.Individual;

/**
 * <p>
 * A custom tag adapter for Family Tree Maker.
 * </p>
 * <p>
 * As of Sep 19 2016, Family Tree Maker is a registered trademark of The Software MacKiev Company. This software is neither written
 * nor endorsed by the authors and copyright holders of Family Tree Maker.
 * </p>
 * 
 * @author frizbog
 */
public class FamilyTreeMakerAdapter extends AbstractCustomTagsAdapter {

    /**
     * Gets the cause of death.
     *
     * @param i
     *            the individual
     * @return the cause of death
     */
    public List<String> getCauseOfDeath(Individual i) {
        return getDescriptions(i, "_DCAUSE");
    }

    /**
     * Sets the causes of death.
     *
     * @param i
     *            the individual
     * @param descriptions
     *            the descriptions of the causes of death
     */
    public void setCausesOfDeath(Individual i, List<String> descriptions) {
        setAllDescriptions(i, "_DCAUSE", descriptions);
    }

}
