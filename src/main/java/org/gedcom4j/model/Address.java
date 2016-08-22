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

import java.util.ArrayList;
import java.util.List;

import org.gedcom4j.Options;

/**
 * Represents an address. Corresponds to part of the ADDRESS_STRUCTURE element in the GEDCOM specification. The other
 * parts of the structure containing phone numbers, faxes, urls, and emails are kept in in the objects that have those
 * attributes.
 * 
 * @author frizbog1
 */
public class Address extends AbstractElement {
    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 8172155175015540119L;

    /**
     * Line one of the address
     */
    private StringWithCustomTags addr1;

    /**
     * Line two of the address
     */
    private StringWithCustomTags addr2;

    /**
     * City
     */
    private StringWithCustomTags city;

    /**
     * Country
     */
    private StringWithCustomTags country;

    /**
     * The lines of the address
     */
    private List<String> lines = getLines(Options.isCollectionInitializationEnabled());

    /**
     * Postal code
     */
    private StringWithCustomTags postalCode;

    /**
     * State/province
     */
    private StringWithCustomTags stateProvince;

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

    /**
     * Gets the addr line 1.
     *
     * @return the addr line 1
     */
    public StringWithCustomTags getAddr1() {
        return addr1;
    }

    /**
     * Gets the addr line 2.
     *
     * @return the addr line 2
     */
    public StringWithCustomTags getAddr2() {
        return addr2;
    }

    /**
     * Gets the city.
     *
     * @return the city
     */
    public StringWithCustomTags getCity() {
        return city;
    }

    /**
     * Gets the country.
     *
     * @return the country
     */
    public StringWithCustomTags getCountry() {
        return country;
    }

    /**
     * Gets the lines.
     *
     * @return the lines
     */
    public List<String> getLines() {
        return lines;
    }

    /**
     * Get the lines
     * 
     * @param initializeIfNeeded
     *            initialize the lines collection if needed
     * 
     * @return the lines
     */
    public List<String> getLines(boolean initializeIfNeeded) {
        if (initializeIfNeeded && lines == null) {
            lines = new ArrayList<>(0);
        }
        return lines;
    }

    /**
     * Gets the postal code.
     *
     * @return the postal code
     */
    public StringWithCustomTags getPostalCode() {
        return postalCode;
    }

    /**
     * Gets the state province.
     *
     * @return the state province
     */
    public StringWithCustomTags getStateProvince() {
        return stateProvince;
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

    /**
     * Sets the addr line 1.
     *
     * @param addr1
     *            the new addr line 1
     */
    public void setAddr1(StringWithCustomTags addr1) {
        this.addr1 = addr1;
    }

    /**
     * Sets the addr line 2.
     *
     * @param addr2
     *            the new addr line 2
     */
    public void setAddr2(StringWithCustomTags addr2) {
        this.addr2 = addr2;
    }

    /**
     * Sets the city.
     *
     * @param city
     *            the new city
     */
    public void setCity(StringWithCustomTags city) {
        this.city = city;
    }

    /**
     * Sets the country.
     *
     * @param country
     *            the new country
     */
    public void setCountry(StringWithCustomTags country) {
        this.country = country;
    }

    /**
     * Sets the postal code.
     *
     * @param postalCode
     *            the new postal code
     */
    public void setPostalCode(StringWithCustomTags postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * Sets the state province.
     *
     * @param stateProvince
     *            the new state province
     */
    public void setStateProvince(StringWithCustomTags stateProvince) {
        this.stateProvince = stateProvince;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(64);
        builder.append("Address [");
        if (addr1 != null) {
            builder.append("addr1=");
            builder.append(addr1);
            builder.append(", ");
        }
        if (addr2 != null) {
            builder.append("addr2=");
            builder.append(addr2);
            builder.append(", ");
        }
        if (city != null) {
            builder.append("city=");
            builder.append(city);
            builder.append(", ");
        }
        if (country != null) {
            builder.append("country=");
            builder.append(country);
            builder.append(", ");
        }
        if (lines != null) {
            builder.append("lines=");
            builder.append(lines);
            builder.append(", ");
        }
        if (postalCode != null) {
            builder.append("postalCode=");
            builder.append(postalCode);
            builder.append(", ");
        }
        if (stateProvince != null) {
            builder.append("stateProvince=");
            builder.append(stateProvince);
            builder.append(", ");
        }
        if (getCustomTags() != null) {
            builder.append("customTags=");
            builder.append(getCustomTags());
        }
        builder.append("]");
        return builder.toString();
    }
}
