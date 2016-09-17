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
 * Name for an individual. Corresponds to PERSONAL_NAME_STRUCTURE in the GEDCOM standard.
 * 
 * @author frizbog1
 * 
 */
@SuppressWarnings("PMD.GodClass")
public class PersonalName extends AbstractNotesElement implements HasCitations {
    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -3038084172374523321L;

    /**
     * The name in basic, unbroken-down format
     */
    private String basic;

    /**
     * The citations for this object
     */
    private List<AbstractCitation> citations = getCitations(Options.isCollectionInitializationEnabled());

    /**
     * The given (aka "Christian" or "first") names
     */
    private StringWithCustomTags givenName;

    /**
     * Nickname
     */
    private StringWithCustomTags nickname;

    /**
     * Phonetic spelling. New for GEDCOM 5.5.1
     */
    private List<PersonalNameVariation> phonetic = getPhonetic(Options.isCollectionInitializationEnabled());

    /**
     * The prefix for the name
     */
    private StringWithCustomTags prefix;

    /**
     * Romanized variant. New for GEDCOM 5.5.1
     */
    private List<PersonalNameVariation> romanized = getRomanized(Options.isCollectionInitializationEnabled());

    /**
     * The suffix
     */
    private StringWithCustomTags suffix;

    /**
     * The surname (aka "family" or "last" name)
     */
    private StringWithCustomTags surname;

    /**
     * Surname prefix
     */
    private StringWithCustomTags surnamePrefix;

    /** Default constructor */
    public PersonalName() {
        // Default constructor does nothing
    }

    /**
     * Copy constructor
     * 
     * @param other
     *            object being copied
     */
    public PersonalName(PersonalName other) {
        super(other);
        basic = other.basic;
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
            givenName = new StringWithCustomTags(other.givenName);
        }
        if (other.nickname != null) {
            nickname = new StringWithCustomTags(other.nickname);
        }
        if (other.phonetic != null) {
            phonetic = new ArrayList<>();
            for (AbstractNameVariation ph : other.phonetic) {
                phonetic.add(new PersonalNameVariation((PersonalNameVariation) ph));
            }
        }
        if (other.prefix != null) {
            prefix = new StringWithCustomTags(other.prefix);
        }
        if (other.romanized != null) {
            romanized = new ArrayList<>();
            for (AbstractNameVariation ph : other.romanized) {
                romanized.add(new PersonalNameVariation((PersonalNameVariation) ph));
            }
        }
        if (other.suffix != null) {
            suffix = new StringWithCustomTags(other.suffix);
        }
        if (other.surname != null) {
            surname = new StringWithCustomTags(other.surname);
        }
        if (other.surnamePrefix != null) {
            surnamePrefix = new StringWithCustomTags(other.surnamePrefix);
        }
    }

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
     * Gets the basic name
     *
     * @return the basic name
     */
    public String getBasic() {
        return basic;
    }

    /**
     * Gets the citations.
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
            citations = new ArrayList<>(0);
        }
        return citations;
    }

    /**
     * Gets the given name.
     *
     * @return the given name
     */
    public StringWithCustomTags getGivenName() {
        return givenName;
    }

    /**
     * Gets the nickname.
     *
     * @return the nickname
     */
    public StringWithCustomTags getNickname() {
        return nickname;
    }

    /**
     * Gets the phonetic variation(s)
     *
     * @return the phonetic varation(s)
     */
    public List<PersonalNameVariation> getPhonetic() {
        return phonetic;
    }

    /**
     * Get the phonetic variation(s)
     * 
     * @param initializeIfNeeded
     *            initialize the collection, if needed?
     * @return the phonetic variation(s)
     */
    public List<PersonalNameVariation> getPhonetic(boolean initializeIfNeeded) {
        if (initializeIfNeeded && phonetic == null) {
            phonetic = new ArrayList<>(0);
        }
        return phonetic;
    }

    /**
     * Gets the prefix.
     *
     * @return the prefix
     */
    public StringWithCustomTags getPrefix() {
        return prefix;
    }

    /**
     * Gets the romanized variation(s)
     *
     * @return the romanized variation(s)
     */
    public List<PersonalNameVariation> getRomanized() {
        return romanized;
    }

    /**
     * Get the romanized variation(s)
     * 
     * @param initializeIfNeeded
     *            initialize the collection, if needed?
     * @return the romanized variation(s)
     */
    public List<PersonalNameVariation> getRomanized(boolean initializeIfNeeded) {
        if (initializeIfNeeded && romanized == null) {
            romanized = new ArrayList<>(0);
        }
        return romanized;
    }

    /**
     * Gets the suffix.
     *
     * @return the suffix
     */
    public StringWithCustomTags getSuffix() {
        return suffix;
    }

    /**
     * Gets the surname.
     *
     * @return the surname
     */
    public StringWithCustomTags getSurname() {
        return surname;
    }

    /**
     * Gets the surname prefix.
     *
     * @return the surname prefix
     */
    public StringWithCustomTags getSurnamePrefix() {
        return surnamePrefix;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (basic == null ? 0 : basic.hashCode());
        result = prime * result + (citations == null ? 0 : citations.hashCode());
        result = prime * result + (givenName == null ? 0 : givenName.hashCode());
        result = prime * result + (nickname == null ? 0 : nickname.hashCode());
        result = prime * result + (prefix == null ? 0 : prefix.hashCode());
        result = prime * result + (suffix == null ? 0 : suffix.hashCode());
        result = prime * result + (surname == null ? 0 : surname.hashCode());
        result = prime * result + (surnamePrefix == null ? 0 : surnamePrefix.hashCode());
        result = prime * result + (romanized == null ? 0 : romanized.hashCode());
        result = prime * result + (phonetic == null ? 0 : phonetic.hashCode());
        return result;
    }

    /**
     * Sets the basic.
     *
     * @param basic
     *            the new basic
     */
    public void setBasic(String basic) {
        this.basic = basic;
    }

    /**
     * Sets the given name.
     *
     * @param givenName
     *            the new given name
     */
    public void setGivenName(String givenName) {
        this.givenName = givenName == null ? null : new StringWithCustomTags(givenName);
    }

    /**
     * Sets the given name.
     *
     * @param givenName
     *            the new given name
     */
    public void setGivenName(StringWithCustomTags givenName) {
        this.givenName = givenName;
    }

    /**
     * Sets the nickname.
     *
     * @param nickname
     *            the new nickname
     */
    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : new StringWithCustomTags(nickname);
    }

    /**
     * Sets the nickname.
     *
     * @param nickname
     *            the new nickname
     */
    public void setNickname(StringWithCustomTags nickname) {
        this.nickname = nickname;
    }

    /**
     * Sets the prefix.
     *
     * @param prefix
     *            the new prefix
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix == null ? null : new StringWithCustomTags(prefix);
    }

    /**
     * Sets the prefix.
     *
     * @param prefix
     *            the new prefix
     */
    public void setPrefix(StringWithCustomTags prefix) {
        this.prefix = prefix;
    }

    /**
     * Sets the suffix.
     *
     * @param suffix
     *            the new suffix
     */
    public void setSuffix(String suffix) {
        this.suffix = suffix == null ? null : new StringWithCustomTags(suffix);
    }

    /**
     * Sets the suffix.
     *
     * @param suffix
     *            the new suffix
     */
    public void setSuffix(StringWithCustomTags suffix) {
        this.suffix = suffix;
    }

    /**
     * Sets the surname.
     *
     * @param surname
     *            the new surname
     */
    public void setSurname(String surname) {
        this.surname = surname == null ? null : new StringWithCustomTags(surname);
    }

    /**
     * Sets the surname.
     *
     * @param surname
     *            the new surname
     */
    public void setSurname(StringWithCustomTags surname) {
        this.surname = surname;
    }

    /**
     * Sets the surname prefix.
     *
     * @param surnamePrefix
     *            the new surname prefix
     */
    public void setSurnamePrefix(String surnamePrefix) {
        this.surnamePrefix = surnamePrefix == null ? null : new StringWithCustomTags(surnamePrefix);
    }

    /**
     * Sets the surname prefix.
     *
     * @param surnamePrefix
     *            the new surname prefix
     */
    public void setSurnamePrefix(StringWithCustomTags surnamePrefix) {
        this.surnamePrefix = surnamePrefix;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        if (surname != null || givenName != null) {
            return surname + ", " + givenName + (nickname == null ? "" : " \"" + nickname + "\"");
        }
        return basic;
    }

}
