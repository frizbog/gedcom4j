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

import java.util.ArrayList;
import java.util.List;

/**
 * Header information about the GEDCOM file
 * 
 * @author frizbog1
 * 
 */
public class Header extends AbstractElement {
    /**
     * The character set in use in the GEDCOM file
     */
    public CharacterSet characterSet = new CharacterSet();

    /**
     * Copyright information for the GEDCOM file.
     */
    public List<String> copyrightData = new ArrayList<String>();

    /**
     * The date of the GEDCOM file
     */
    public StringWithCustomTags date;

    /**
     * The destination system for the GEDCOM file.
     */
    public StringWithCustomTags destinationSystem;

    /**
     * The filename for the GEDCOM file
     */
    public StringWithCustomTags fileName;

    /**
     * The version information for the GEDCOM file
     */
    public GedcomVersion gedcomVersion = new GedcomVersion();

    /**
     * The place structure for the file
     */
    public StringWithCustomTags placeHierarchy;

    /**
     * The source system for the GEDCOM file
     */
    public SourceSystem sourceSystem;

    /**
     * Information about the file submission
     */
    public Submission submission;

    /**
     * Information about the submitter of the file
     */
    public Submitter submitter;

    /**
     * The time of the file
     */
    public StringWithCustomTags time;

    /**
     * The language for the file
     */
    public StringWithCustomTags language;

    /**
     * A bunch of textual notes
     */
    public List<String> notes = new ArrayList<String>();

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
        Header other = (Header) obj;
        if (characterSet == null) {
            if (other.characterSet != null) {
                return false;
            }
        } else if (!characterSet.equals(other.characterSet)) {
            return false;
        }
        if (copyrightData == null) {
            if (other.copyrightData != null) {
                return false;
            }
        } else if (!copyrightData.equals(other.copyrightData)) {
            return false;
        }
        if (date == null) {
            if (other.date != null) {
                return false;
            }
        } else if (!date.equals(other.date)) {
            return false;
        }
        if (destinationSystem == null) {
            if (other.destinationSystem != null) {
                return false;
            }
        } else if (!destinationSystem.equals(other.destinationSystem)) {
            return false;
        }
        if (fileName == null) {
            if (other.fileName != null) {
                return false;
            }
        } else if (!fileName.equals(other.fileName)) {
            return false;
        }
        if (gedcomVersion == null) {
            if (other.gedcomVersion != null) {
                return false;
            }
        } else if (!gedcomVersion.equals(other.gedcomVersion)) {
            return false;
        }
        if (language == null) {
            if (other.language != null) {
                return false;
            }
        } else if (!language.equals(other.language)) {
            return false;
        }
        if (notes == null) {
            if (other.notes != null) {
                return false;
            }
        } else if (!notes.equals(other.notes)) {
            return false;
        }
        if (placeHierarchy == null) {
            if (other.placeHierarchy != null) {
                return false;
            }
        } else if (!placeHierarchy.equals(other.placeHierarchy)) {
            return false;
        }
        if (sourceSystem == null) {
            if (other.sourceSystem != null) {
                return false;
            }
        } else if (!sourceSystem.equals(other.sourceSystem)) {
            return false;
        }
        if (submission == null) {
            if (other.submission != null) {
                return false;
            }
        } else if (!submission.equals(other.submission)) {
            return false;
        }
        if (submitter == null) {
            if (other.submitter != null) {
                return false;
            }
        } else if (!submitter.equals(other.submitter)) {
            return false;
        }
        if (time == null) {
            if (other.time != null) {
                return false;
            }
        } else if (!time.equals(other.time)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (characterSet == null ? 0 : characterSet.hashCode());
        result = prime * result + (copyrightData == null ? 0 : copyrightData.hashCode());
        result = prime * result + (date == null ? 0 : date.hashCode());
        result = prime * result + (destinationSystem == null ? 0 : destinationSystem.hashCode());
        result = prime * result + (fileName == null ? 0 : fileName.hashCode());
        result = prime * result + (gedcomVersion == null ? 0 : gedcomVersion.hashCode());
        result = prime * result + (language == null ? 0 : language.hashCode());
        result = prime * result + (notes == null ? 0 : notes.hashCode());
        result = prime * result + (placeHierarchy == null ? 0 : placeHierarchy.hashCode());
        result = prime * result + (sourceSystem == null ? 0 : sourceSystem.hashCode());
        result = prime * result + (submission == null ? 0 : submission.hashCode());
        result = prime * result + (submitter == null ? 0 : submitter.hashCode());
        result = prime * result + (time == null ? 0 : time.hashCode());

        return result;
    }

    @Override
    public String toString() {
        return "Header [characterSet=" + characterSet + ", copyrightData=" + copyrightData + ", date=" + date
                + ", destinationSystem=" + destinationSystem + ", fileName=" + fileName + ", gedcomVersion="
                + gedcomVersion + ", placeHierarchy=" + placeHierarchy + ", sourceSystem=" + sourceSystem
                + ", submission=" + submission + ", submitter=" + submitter + ", time=" + time + ", language="
                + language + ", notes=" + notes + ", customTags=" + customTags + "]";
    }

}
