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
 * A variation on a personal name - either a romanized version or a phonetic version. Introduced with GEDCOM 5.5.1.
 * 
 * @author frizbog
 */
public class PersonalNameVariation extends AbstractNameVariation implements HasNotes, HasCitations {
    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 1262477720634081355L;

    /**
     * The citations for this object
     */
    private List<AbstractCitation> citations = getCitations(Options.isCollectionInitializationEnabled());

    /**
     * The given (aka "Christian" or "first") names
     */
    private StringWithCustomFacts givenName;

    /**
     * Nickname
     */
    private StringWithCustomFacts nickname;

    /**
     * Notes about this object
     */
    private List<NoteStructure> notes = getNoteStructures(Options.isCollectionInitializationEnabled());

    /**
     * The prefix for the name
     */
    private StringWithCustomFacts prefix;

    /**
     * The suffix
     */
    private StringWithCustomFacts suffix;

    /**
     * The surname (aka "family" or "last" name)
     */
    private StringWithCustomFacts surname;

    /**
     * Surname prefix
     */
    private StringWithCustomFacts surnamePrefix;

    /** Default constructor */
    public PersonalNameVariation() {
        // Default constructor does nothing
    }

    /**
     * Copy constructor
     * 
     * @param other
     *            object being copied
     */
    public PersonalNameVariation(PersonalNameVariation other) {
        super(other);
        if (other.citations != null) {
            citations = new ArrayList<>();
            for (AbstractCitation ac : other.citations) {
                if (ac instanceof CitationWithoutSource) {
                    citations.add(new CitationWithoutSource((CitationWithoutSource) ac));
                } else if (ac instanceof CitationWithSource) {
                    citations.add(new CitationWithSource((CitationWithSource) ac));
                }
            }
        }
        if (other.givenName != null) {
            givenName = new StringWithCustomFacts(other.givenName);
        }
        if (other.nickname != null) {
            nickname = new StringWithCustomFacts(other.nickname);
        }
        if (other.getNoteStructures() != null) {
            notes = new ArrayList<>();
            for (NoteStructure n : other.getNoteStructures()) {
                notes.add(new NoteStructure(n));
            }
        }
        if (other.prefix != null) {
            prefix = new StringWithCustomFacts(other.prefix);
        }
        if (other.suffix != null) {
            suffix = new StringWithCustomFacts(other.suffix);
        }
        if (other.surname != null) {
            surname = new StringWithCustomFacts(other.surname);
        }
        if (other.surnamePrefix != null) {
            surnamePrefix = new StringWithCustomFacts(other.surnamePrefix);
        }
    }

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
     * Gets the citations.
     *
     * @return the citations
     */
    @Override
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
    @Override
    public List<AbstractCitation> getCitations(boolean initializeIfNeeded) {
        if (initializeIfNeeded && citations == null) {
            citations = new ArrayList<>(0);
        }
        return citations;
    }

    /**
     * Gets the given name.
     *
     * @return the given name
     */
    public StringWithCustomFacts getGivenName() {
        return givenName;
    }

    /**
     * Gets the nickname.
     *
     * @return the nickname
     */
    public StringWithCustomFacts getNickname() {
        return nickname;
    }

    /**
     * Gets the notes.
     *
     * @return the notes
     */
    @Override
    public List<NoteStructure> getNoteStructures() {
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
    @Override
    public List<NoteStructure> getNoteStructures(boolean initializeIfNeeded) {
        if (initializeIfNeeded && notes == null) {
            notes = new ArrayList<>(0);
        }
        return notes;
    }

    /**
     * Gets the prefix.
     *
     * @return the prefix
     */
    public StringWithCustomFacts getPrefix() {
        return prefix;
    }

    /**
     * Gets the suffix.
     *
     * @return the suffix
     */
    public StringWithCustomFacts getSuffix() {
        return suffix;
    }

    /**
     * Gets the surname.
     *
     * @return the surname
     */
    public StringWithCustomFacts getSurname() {
        return surname;
    }

    /**
     * Gets the surname prefix.
     *
     * @return the surname prefix
     */
    public StringWithCustomFacts getSurnamePrefix() {
        return surnamePrefix;
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

    /**
     * Sets the given name.
     *
     * @param givenName
     *            the new given name
     */
    public void setGivenName(String givenName) {
        this.givenName = givenName == null ? null : new StringWithCustomFacts(givenName);
    }

    /**
     * Sets the given name.
     *
     * @param givenName
     *            the new given name
     */
    public void setGivenName(StringWithCustomFacts givenName) {
        this.givenName = givenName;
    }

    /**
     * Sets the nickname.
     *
     * @param nickname
     *            the new nickname
     */
    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : new StringWithCustomFacts(nickname);
    }

    /**
     * Sets the nickname.
     *
     * @param nickname
     *            the new nickname
     */
    public void setNickname(StringWithCustomFacts nickname) {
        this.nickname = nickname;
    }

    /**
     * Sets the prefix.
     *
     * @param prefix
     *            the new prefix
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix == null ? null : new StringWithCustomFacts(prefix);
    }

    /**
     * Sets the prefix.
     *
     * @param prefix
     *            the new prefix
     */
    public void setPrefix(StringWithCustomFacts prefix) {
        this.prefix = prefix;
    }

    /**
     * Sets the suffix.
     *
     * @param suffix
     *            the new suffix
     */
    public void setSuffix(String suffix) {
        this.suffix = suffix == null ? null : new StringWithCustomFacts(suffix);
    }

    /**
     * Sets the suffix.
     *
     * @param suffix
     *            the new suffix
     */
    public void setSuffix(StringWithCustomFacts suffix) {
        this.suffix = suffix;
    }

    /**
     * Sets the surname.
     *
     * @param surname
     *            the new surname
     */
    public void setSurname(String surname) {
        this.surname = surname == null ? null : new StringWithCustomFacts(surname);
    }

    /**
     * Sets the surname.
     *
     * @param surname
     *            the new surname
     */
    public void setSurname(StringWithCustomFacts surname) {
        this.surname = surname;
    }

    /**
     * Sets the surname prefix.
     *
     * @param surnamePrefix
     *            the new surname prefix
     */
    public void setSurnamePrefix(String surnamePrefix) {
        this.surnamePrefix = surnamePrefix == null ? null : new StringWithCustomFacts(surnamePrefix);
    }

    /**
     * Sets the surname prefix.
     *
     * @param surnamePrefix
     *            the new surname prefix
     */
    public void setSurnamePrefix(StringWithCustomFacts surnamePrefix) {
        this.surnamePrefix = surnamePrefix;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(64);
        builder.append("PersonalNameVariation [");
        if (citations != null) {
            builder.append("citations=");
            builder.append(citations);
            builder.append(", ");
        }
        if (givenName != null) {
            builder.append("givenName=");
            builder.append(givenName);
            builder.append(", ");
        }
        if (nickname != null) {
            builder.append("nickname=");
            builder.append(nickname);
            builder.append(", ");
        }
        if (notes != null) {
            builder.append("notes=");
            builder.append(notes);
            builder.append(", ");
        }
        if (prefix != null) {
            builder.append("prefix=");
            builder.append(prefix);
            builder.append(", ");
        }
        if (suffix != null) {
            builder.append("suffix=");
            builder.append(suffix);
            builder.append(", ");
        }
        if (surname != null) {
            builder.append("surname=");
            builder.append(surname);
            builder.append(", ");
        }
        if (surnamePrefix != null) {
            builder.append("surnamePrefix=");
            builder.append(surnamePrefix);
            builder.append(", ");
        }
        if (variation != null) {
            builder.append("variation=");
            builder.append(variation);
            builder.append(", ");
        }
        if (variationType != null) {
            builder.append("variationType=");
            builder.append(variationType);
            builder.append(", ");
        }
        if (getCustomFacts() != null) {
            builder.append("customFacts=");
            builder.append(customFacts);
        }
        builder.append("]");
        return builder.toString();
    }

}
