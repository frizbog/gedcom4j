/*
 * Copyright (c) 2009-2014 Matthew R. Harrah
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
 * A variation on a personal name - either a romanized version or a phonetic version. Introduced with GEDCOM 5.5.1.
 * 
 * @author frizbog
 * 
 */
public class PersonalNameVariation extends NameVariation {
    /**
     * The prefix for the name
     */
    public StringWithCustomTags prefix;

    /**
     * The given (aka "Christian" or "first") names
     */
    public StringWithCustomTags givenName;

    /**
     * Nickname
     */
    public StringWithCustomTags nickname;

    /**
     * Surname prefix
     */
    public StringWithCustomTags surnamePrefix;

    /**
     * The surname (aka "family" or "last" name)
     */
    public StringWithCustomTags surname;

    /**
     * The suffix
     */
    public StringWithCustomTags suffix;

    /**
     * Notes on this name
     */
    public List<Note> notes = new ArrayList<Note>();

    /**
     * Citations for this name
     */
    public List<AbstractCitation> citations = new ArrayList<AbstractCitation>();

    /**
     * Determine if this object is equal to another
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     * @param obj
     *            the other object we are comparing this one to
     * @return true if and only if the other object is equal to this one
     */
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
        PersonalNameVariation other = (PersonalNameVariation) obj;
        if (citations == null) {
            if (other.citations != null) {
                return false;
            }
        } else if (!citations.equals(other.citations)) {
            return false;
        }
        if (givenName == null) {
            if (other.givenName != null) {
                return false;
            }
        } else if (!givenName.equals(other.givenName)) {
            return false;
        }
        if (nickname == null) {
            if (other.nickname != null) {
                return false;
            }
        } else if (!nickname.equals(other.nickname)) {
            return false;
        }
        if (notes == null) {
            if (other.notes != null) {
                return false;
            }
        } else if (!notes.equals(other.notes)) {
            return false;
        }
        if (prefix == null) {
            if (other.prefix != null) {
                return false;
            }
        } else if (!prefix.equals(other.prefix)) {
            return false;
        }
        if (suffix == null) {
            if (other.suffix != null) {
                return false;
            }
        } else if (!suffix.equals(other.suffix)) {
            return false;
        }
        if (surname == null) {
            if (other.surname != null) {
                return false;
            }
        } else if (!surname.equals(other.surname)) {
            return false;
        }
        if (surnamePrefix == null) {
            if (other.surnamePrefix != null) {
                return false;
            }
        } else if (!surnamePrefix.equals(other.surnamePrefix)) {
            return false;
        }
        if (variation == null) {
            if (other.variation != null) {
                return false;
            }
        } else if (!variation.equals(other.variation)) {
            return false;
        }
        if (variationType == null) {
            if (other.variationType != null) {
                return false;
            }
        } else if (!variationType.equals(other.variationType)) {
            return false;
        }
        return true;
    }

    /**
     * Calculate a hashcode for this object
     * 
     * @see java.lang.Object#hashCode()
     * @return a hashcode for this object
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (citations == null ? 0 : citations.hashCode());
        result = prime * result + (givenName == null ? 0 : givenName.hashCode());
        result = prime * result + (nickname == null ? 0 : nickname.hashCode());
        result = prime * result + (notes == null ? 0 : notes.hashCode());
        result = prime * result + (prefix == null ? 0 : prefix.hashCode());
        result = prime * result + (suffix == null ? 0 : suffix.hashCode());
        result = prime * result + (surname == null ? 0 : surname.hashCode());
        result = prime * result + (surnamePrefix == null ? 0 : surnamePrefix.hashCode());
        result = prime * result + (variation == null ? 0 : variation.hashCode());
        result = prime * result + (variationType == null ? 0 : variationType.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "PersonalNameVariation [" + (prefix != null ? "prefix=" + prefix + ", " : "") + (givenName != null ? "givenName=" + givenName + ", " : "")
                + (nickname != null ? "nickname=" + nickname + ", " : "") + (surnamePrefix != null ? "surnamePrefix=" + surnamePrefix + ", " : "")
                + (surname != null ? "surname=" + surname + ", " : "") + (suffix != null ? "suffix=" + suffix + ", " : "")
                + (notes != null ? "notes=" + notes + ", " : "") + (citations != null ? "citations=" + citations + ", " : "")
                + (variationType != null ? "variationType=" + variationType + ", " : "") + (variation != null ? "variation=" + variation + ", " : "")
                + (customTags != null ? "customTags=" + customTags : "") + "]";
    }

}
