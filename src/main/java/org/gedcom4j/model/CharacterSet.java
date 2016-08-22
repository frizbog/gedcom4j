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

import org.gedcom4j.io.encoding.Encoding;

/**
 * A character set.
 * 
 * @author frizbog1
 */
public class CharacterSet extends AbstractElement {
    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 1745150676480256110L;

    /**
     * The name of a character set
     */
    private StringWithCustomTags characterSetName = new StringWithCustomTags(Encoding.ANSEL.toString());

    /**
     * A version number of the character set
     */
    private StringWithCustomTags versionNum;

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
        CharacterSet other = (CharacterSet) obj;
        if (characterSetName == null) {
            if (other.characterSetName != null) {
                return false;
            }
        } else if (!characterSetName.equals(other.characterSetName)) {
            return false;
        }
        if (versionNum == null) {
            if (other.versionNum != null) {
                return false;
            }
        } else if (!versionNum.equals(other.versionNum)) {
            return false;
        }
        return true;
    }

    /**
     * Gets the character set name.
     *
     * @return the character set name
     */
    public StringWithCustomTags getCharacterSetName() {
        return characterSetName;
    }

    /**
     * Gets the version num.
     *
     * @return the version num
     */
    public StringWithCustomTags getVersionNum() {
        return versionNum;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (characterSetName == null ? 0 : characterSetName.hashCode());
        result = prime * result + (versionNum == null ? 0 : versionNum.hashCode());
        return result;
    }

    /**
     * Sets the character set name.
     *
     * @param characterSetName
     *            the new character set name
     */
    public void setCharacterSetName(StringWithCustomTags characterSetName) {
        this.characterSetName = characterSetName;
    }

    /**
     * Sets the version num.
     *
     * @param versionNum
     *            the new version num
     */
    public void setVersionNum(StringWithCustomTags versionNum) {
        this.versionNum = versionNum;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(32);
        builder.append("CharacterSet [");
        if (characterSetName != null) {
            builder.append("characterSetName=");
            builder.append(characterSetName);
            builder.append(", ");
        }
        if (versionNum != null) {
            builder.append("versionNum=");
            builder.append(versionNum);
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
