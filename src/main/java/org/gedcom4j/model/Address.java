/*
 * Copyright (c) 2009-2016 Matthew R. Harrah
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.gedcom4j.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an address. Corresponds to part of the ADDRESS_STRUCTURE element in the GEDCOM specification. The other
 * parts of the structure containing phone numbers, faxes, urls, and emails are kept in in the objects that have those
 * attributes.
 * 
 * @author frizbog1
 */
public class Address extends AbstractElement {
    /**
     * The lines of the address
     */
    public List<String> lines = new ArrayList<String>();

    /**
     * Line one of the address
     */
    public StringWithCustomTags addr1;

    /**
     * Line two of the address
     */
    public StringWithCustomTags addr2;

    /**
     * City
     */
    public StringWithCustomTags city;

    /**
     * State/province
     */
    public StringWithCustomTags stateProvince;

    /**
     * Postal code
     */
    public StringWithCustomTags postalCode;

    /**
     * Country
     */
    public StringWithCustomTags country;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Address other = (Address) obj;
        if (addr1 == null) {
            if (other.addr1 != null) {
                return false;
            }
        } else if (!addr1.equals(other.addr1)) {
            return false;
        }
        if (addr2 == null) {
            if (other.addr2 != null) {
                return false;
            }
        } else if (!addr2.equals(other.addr2)) {
            return false;
        }
        if (city == null) {
            if (other.city != null) {
                return false;
            }
        } else if (!city.equals(other.city)) {
            return false;
        }
        if (country == null) {
            if (other.country != null) {
                return false;
            }
        } else if (!country.equals(other.country)) {
            return false;
        }
        if (lines == null) {
            if (other.lines != null) {
                return false;
            }
        } else if (!lines.equals(other.lines)) {
            return false;
        }
        if (postalCode == null) {
            if (other.postalCode != null) {
                return false;
            }
        } else if (!postalCode.equals(other.postalCode)) {
            return false;
        }
        if (stateProvince == null) {
            if (other.stateProvince != null) {
                return false;
            }
        } else if (!stateProvince.equals(other.stateProvince)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (addr1 == null ? 0 : addr1.hashCode());
        result = prime * result + (addr2 == null ? 0 : addr2.hashCode());
        result = prime * result + (city == null ? 0 : city.hashCode());
        result = prime * result + (country == null ? 0 : country.hashCode());
        result = prime * result + (lines == null ? 0 : lines.hashCode());
        result = prime * result + (postalCode == null ? 0 : postalCode.hashCode());
        result = prime * result + (stateProvince == null ? 0 : stateProvince.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "Address [" + (lines != null ? "lines=" + lines + ", " : "") + (addr1 != null ? "addr1=" + addr1 + ", " : "")
                + (addr2 != null ? "addr2=" + addr2 + ", " : "") + (city != null ? "city=" + city + ", " : "")
                + (stateProvince != null ? "stateProvince=" + stateProvince + ", " : "") + (postalCode != null ? "postalCode=" + postalCode + ", " : "")
                + (country != null ? "country=" + country + ", " : "") + (customTags != null ? "customTags=" + customTags : "") + "]";
    }
}
