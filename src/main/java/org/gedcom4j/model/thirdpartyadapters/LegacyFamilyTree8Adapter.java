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

import org.gedcom4j.model.Address;
import org.gedcom4j.model.Individual;
import org.gedcom4j.model.Multimedia;

/**
 * <p>
 * A custom tag adapter for Legacy Family Tree 8.
 * </p>
 * <p>
 * As of 1 Oct 2016, Legacy&copy; is a registered trademark of Millennia Corporation, Surprise, AZ 85374. This software is neither
 * written nor endorsed by the authors and copyright holders of Family Historian.
 * </p>
 * 
 * @author frizbog
 */
public class LegacyFamilyTree8Adapter extends AbstractThirdPartyAdapter {

    /**
     * Get the value used for sorting an address
     * 
     * @param addr
     *            the address
     * @return the value used for sorting the address
     */
    public String getAddressSortValue(Address addr) {
        return getDescriptionForCustomTag(addr, "_SORT");
    }

    /**
     * Get the UID assigned to the individual
     * 
     * @param ind
     *            the individual
     * @return the UID assigned to the individual
     */
    public String getIndividualUID(Individual ind) {
        return getDescriptionForCustomTag(ind, "_UID");
    }

    /**
     * Get the date of the multimedia item
     * 
     * @param mm
     *            the multimedia item
     * @return the date of the multimedia item
     */
    public String getMultimediaDate(Multimedia mm) {
        return getDescriptionForCustomTag(mm, "_DATE");
    }

    /**
     * Get the name used (by the individual) at an address
     * 
     * @param addr
     *            the address
     * @return the name used at the address
     */
    public String getNameAtAddress(Address addr) {
        return getDescriptionForCustomTag(addr, "_NAME");
    }

    /**
     * Set the value used for sorting an address
     * 
     * @param addr
     *            the address
     * @param nameAtAddress
     *            the value used for sorting the address. Optional - pass in null to remove.
     */
    public void setAddressSortValue(Address addr, String nameAtAddress) {
        setDescriptionForCustomTag(addr, "_SORT", nameAtAddress);
    }

    /**
     * Set the UID assigned to the individual
     * 
     * @param ind
     *            the individual
     * @param individualUid
     *            the the UID assigned to the individual. Optional - pass in null to remove.
     */
    public void setIndividualUID(Individual ind, String individualUid) {
        setDescriptionForCustomTag(ind, "_UID", individualUid);
    }

    /**
     * Set the multimedia date
     * 
     * @param mm
     *            the multimedia
     * @param multimediaDate
     *            the date of the multimedia. Optional; pass null to remove the date.
     */
    public void setMultimediaDate(Multimedia mm, String multimediaDate) {
        setDescriptionForCustomTag(mm, "_DATE", multimediaDate);
    }

    /**
     * Set the name used (by the individual) at an address
     * 
     * @param addr
     *            the address
     * @param nameAtAddress
     *            the name used at the address. Optional - pass in null to remove.
     */
    public void setNameAtAddress(Address addr, String nameAtAddress) {
        setDescriptionForCustomTag(addr, "_NAME", nameAtAddress);
    }

}
