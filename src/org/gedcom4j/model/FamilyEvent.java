/*
 * Copyright (c) 2009-2013 Matthew R. Harrah
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
public class FamilyEvent extends Event {
    /**
     * The type of event. See FAMILY_EVENT_STRUCTURE in teh GEDCOM standard for more info.
     */
    public FamilyEventType type;

    /**
     * Age of husband at time of event
     */
    public StringWithCustomTags husbandAge;

    /**
     * Age of wife at time of event
     */
    public StringWithCustomTags wifeAge;

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
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "FamilyEvent [type=" + type + ", husbandAge=" + husbandAge + ", wifeAge=" + wifeAge
                + (type == FamilyEventType.EVENT ? ", subtype=" + subType : "") + "]";
    }

}
