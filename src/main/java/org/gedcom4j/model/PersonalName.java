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

import org.gedcom4j.Options;

/**
 * Name for an individual. Corresponds to PERSONAL_NAME_STRUCTURE in the GEDCOM standard.
 * 
 * @author frizbog1
 * 
 */
public class PersonalName extends AbstractElement {
    /**
     * The name in basic, unbroken-down format
     */
    public String basic;

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
     * Romanized variant. New for GEDCOM 5.5.1
     */
    public List<PersonalNameVariation> romanized = new ArrayList<PersonalNameVariation>(0);

    /**
     * Phonetic spelling. New for GEDCOM 5.5.1
     */
    public List<PersonalNameVariation> phonetic = new ArrayList<PersonalNameVariation>(0);

    /**
     * Notes about this object
     */
    private List<Note> notes = Options.isCollectionInitializationEnabled() ? getNotes(true) : null;

    /**
     * The citations for this object
     */
    private List<AbstractCitation> citations = Options.isCollectionInitializationEnabled() ? new ArrayList<AbstractCitation>(0) : null;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof PersonalName)) {
            return false;
        }
        PersonalName other = (PersonalName) obj;
        if (basic == null) {
            if (other.basic != null) {
                return false;
            }
        } else if (!basic.equals(other.basic)) {
            return false;
        }
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
        if (romanized == null) {
            if (other.romanized != null) {
                return false;
            }
        } else if (!romanized.equals(other.romanized)) {
            return false;
        }
        if (phonetic == null) {
            if (other.phonetic != null) {
                return false;
            }
        } else if (!phonetic.equals(other.phonetic)) {
            return false;
        }
        return true;
    }

    /**
     * Get the citations
     * 
     * @return the citations
     */
    public List<AbstractCitation> getCitations() {
        return citations;
    }

    /**
     * Get the citations
     * 
     * @param initializeIfNeeded
     *            initialize the collection if needed?
     * 
     * @return the citations
     */
    public List<AbstractCitation> getCitations(boolean initializeIfNeeded) {
        if (initializeIfNeeded && citations == null) {
            citations = new ArrayList<AbstractCitation>(0);
        }
        return citations;
    }

    /**
     * Get the notes
     * 
     * @return the notes
     */
    public List<Note> getNotes() {
        return notes;
    }

    /**
     * Get the notes
     * 
     * @param initializeIfNeeded
     *            initialize the collection if needed?
     * 
     * @return the notes
     */
    public List<Note> getNotes(boolean initializeIfNeeded) {
        if (initializeIfNeeded && notes == null) {
            notes = new ArrayList<Note>(0);
        }
        return notes;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (basic == null ? 0 : basic.hashCode());
        result = prime * result + (citations == null ? 0 : citations.hashCode());
        result = prime * result + (givenName == null ? 0 : givenName.hashCode());
        result = prime * result + (nickname == null ? 0 : nickname.hashCode());
        result = prime * result + (notes == null ? 0 : notes.hashCode());
        result = prime * result + (prefix == null ? 0 : prefix.hashCode());
        result = prime * result + (suffix == null ? 0 : suffix.hashCode());
        result = prime * result + (surname == null ? 0 : surname.hashCode());
        result = prime * result + (surnamePrefix == null ? 0 : surnamePrefix.hashCode());
        result = prime * result + (romanized == null ? 0 : romanized.hashCode());
        result = prime * result + (phonetic == null ? 0 : phonetic.hashCode());
        return result;
    }

    @Override
    public String toString() {
        if (surname != null || givenName != null) {
            return surname + ", " + givenName + (nickname == null ? "" : " \"" + nickname + "\"");
        }
        return basic;
    }

}
