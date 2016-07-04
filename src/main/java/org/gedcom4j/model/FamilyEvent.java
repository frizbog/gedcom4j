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

/**
 * Represents a family event. Corresponds to the FAMILY_EVENT_STRUCTURE from the GEDCOM standard along with the two
 * child elements of the wife and husband ages.
 * 
 * @author frizbog1
 * 
 */
public class FamilyEvent extends AbstractEvent {
    /**
     * The type of event. See FAMILY_EVENT_STRUCTURE in the GEDCOM standard for more info.
     */
    private FamilyEventType type;

    /**
     * Age of husband at time of event
     */
    private StringWithCustomTags husbandAge;

    /**
     * Age of wife at time of event
     */
    private StringWithCustomTags wifeAge;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof FamilyEvent)) {
            return false;
        }
        FamilyEvent other = (FamilyEvent) obj;
        if (husbandAge == null) {
            if (other.husbandAge != null) {
                return false;
            }
        } else if (!husbandAge.equals(other.husbandAge)) {
            return false;
        }
        if (type != other.type) {
            return false;
        }
        if (wifeAge == null) {
            if (other.wifeAge != null) {
                return false;
            }
        } else if (!wifeAge.equals(other.wifeAge)) {
            return false;
        }
        return true;
    }

    /**
     * Get the husbandAge
     * 
     * @return the husbandAge
     */
    public StringWithCustomTags getHusbandAge() {
        return husbandAge;
    }

    /**
     * Get the type
     * 
     * @return the type
     */
    public FamilyEventType getType() {
        return type;
    }

    /**
     * Get the wifeAge
     * 
     * @return the wifeAge
     */
    public StringWithCustomTags getWifeAge() {
        return wifeAge;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (husbandAge == null ? 0 : husbandAge.hashCode());
        result = prime * result + (type == null ? 0 : type.hashCode());
        result = prime * result + (wifeAge == null ? 0 : wifeAge.hashCode());
        return result;
    }

    /**
     * Set the husbandAge
     * 
     * @param husbandAge
     *            the husbandAge to set
     */
    public void setHusbandAge(StringWithCustomTags husbandAge) {
        this.husbandAge = husbandAge;
    }

    /**
     * Set the type
     * 
     * @param type
     *            the type to set
     */
    public void setType(FamilyEventType type) {
        this.type = type;
    }

    /**
     * Set the wifeAge
     * 
     * @param wifeAge
     *            the wifeAge to set
     */
    public void setWifeAge(StringWithCustomTags wifeAge) {
        this.wifeAge = wifeAge;
    }

    @Override
    public String toString() {
        return "FamilyEvent [" + (type != null ? "type=" + type + ", " : "") + (husbandAge != null ? "husbandAge=" + husbandAge + ", " : "") + (wifeAge != null
                ? "wifeAge=" + wifeAge + ", " : "") + (address != null ? "address=" + address + ", " : "") + (phoneNumbers != null ? "phoneNumbers="
                        + phoneNumbers + ", " : "") + (wwwUrls != null ? "wwwUrls=" + wwwUrls + ", " : "") + (faxNumbers != null ? "faxNumbers=" + faxNumbers
                                + ", " : "") + (emails != null ? "emails=" + emails + ", " : "") + (age != null ? "age=" + age + ", " : "") + (cause != null
                                        ? "cause=" + cause + ", " : "") + (citations != null ? "citations=" + citations + ", " : "") + (date != null ? "date="
                                                + date + ", " : "") + (description != null ? "description=" + description + ", " : "") + (multimedia != null
                                                        ? "multimedia=" + multimedia + ", " : "") + (notes != null ? "notes=" + notes + ", " : "")
                + (place != null ? "place=" + place + ", " : "") + (respAgency != null ? "respAgency=" + respAgency + ", " : "") + (yNull != null ? "yNull="
                        + yNull + ", " : "") + (subType != null ? "subType=" + subType + ", " : "") + (religiousAffiliation != null ? "religiousAffiliation="
                                + religiousAffiliation + ", " : "") + (restrictionNotice != null ? "restrictionNotice=" + restrictionNotice + ", " : "")
                + (getCustomTags() != null ? "customTags=" + getCustomTags() : "") + "]";
    }

}
