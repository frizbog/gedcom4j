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
package com.mattharrah.gedcom4j.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Name for an individual. Corresponds to PERSONAL_NAME_STRUCTURE in the GEDCOM
 * standard.
 * 
 * @author frizbog1
 * 
 */
public class PersonalName {
    /**
     * The name in basic, unbroken-down format
     */
    public String basic;
    /**
     * The prefix for the name
     */
    public String prefix;
    /**
     * The given (aka "Christian" or "first") names
     */
    public String givenName;
    /**
     * Nickname
     */
    public String nickname;
    /**
     * Surname prefix
     */
    public String surnamePrefix;
    /**
     * The surname (aka "family" or "last" name)
     */
    public String surname;
    /**
     * The suffix
     */
    public String suffix;
    /**
     * Notes on this name
     */
    public List<Note> notes = new ArrayList<Note>();
    /**
     * Citations for this name
     */
    public List<AbstractCitation> citations = new ArrayList<AbstractCitation>();

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
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
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((basic == null) ? 0 : basic.hashCode());
        result = prime * result
                + ((citations == null) ? 0 : citations.hashCode());
        result = prime * result
                + ((givenName == null) ? 0 : givenName.hashCode());
        result = prime * result
                + ((nickname == null) ? 0 : nickname.hashCode());
        result = prime * result + ((notes == null) ? 0 : notes.hashCode());
        result = prime * result + ((prefix == null) ? 0 : prefix.hashCode());
        result = prime * result + ((suffix == null) ? 0 : suffix.hashCode());
        result = prime * result + ((surname == null) ? 0 : surname.hashCode());
        result = prime * result
                + ((surnamePrefix == null) ? 0 : surnamePrefix.hashCode());
        return result;
    }

    @Override
    public String toString() {
        if (surname != null || givenName != null) {
            return "" + surname + ", " + givenName
                    + (nickname == null ? "" : " \"" + nickname + "\"");
        }
        return basic;
    }
}
