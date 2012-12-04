/*
 * Copyright (c) 2009-2012 Matthew R. Harrah
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
 * Represents an individual event. Corresponds to the INDIVIDUAL_EVENT_STRUCTURE item in the GEDCOM specification.
 * 
 * @author frizbog1
 */
public class IndividualEvent extends Event {
    /**
     * The type of event this represents
     */
    public IndividualEventType type;

    /**
     * The family to which this individual adopted was adopted
     */
    public FamilyChild family;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof IndividualEvent)) {
            return false;
        }
        IndividualEvent other = (IndividualEvent) obj;
        if (family == null) {
            if (other.family != null) {
                return false;
            }
        } else if (!family.equals(other.family)) {
            return false;
        }
        if (type != other.type) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (family == null ? 0 : family.hashCode());
        result = prime * result + (type == null ? 0 : type.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "IndividualEvent [type=" + type + ", family=" + family + ", address=" + address + ", phoneNumbers="
                + phoneNumbers + ", wwwUrls=" + wwwUrls + ", faxNumbers=" + faxNumbers + ", emails=" + emails
                + ", age=" + age + ", cause=" + cause + ", citations=" + citations + ", date=" + date
                + ", description=" + description + ", multimedia=" + multimedia + ", notes=" + notes + ", place="
                + place + ", respAgency=" + respAgency + ", yNull=" + yNull + ", subType=" + subType
                + ", religiousAffiliation=" + religiousAffiliation + ", restrictionNotice=" + restrictionNotice
                + ", customTags=" + customTags + "]";
    }

}
