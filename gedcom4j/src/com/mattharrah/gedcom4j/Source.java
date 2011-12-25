/*
 * Copyright (c) 2009-2011 Matthew R. Harrah
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
package com.mattharrah.gedcom4j;

import java.util.ArrayList;
import java.util.List;

/**
 * A source for citations.
 * 
 * @author frizbog1
 */
public class Source {
    /**
     * The registered file number
     */
    public String regFileNumber;
    /**
     * The record id number
     */
    public String recIdNumber;
    /**
     * Who filed the source
     */
    public String sourceFiledBy;
    /**
     * The title text
     */
    public List<String> title = new ArrayList<String>();
    /**
     * Notes on this source
     */
    public List<Note> notes = new ArrayList<Note>();
    /**
     * Publication facts on this source
     */
    public List<String> publicationFacts = new ArrayList<String>();
    /**
     * The originators/authors
     */
    public List<String> originatorsAuthors = new ArrayList<String>();
    /**
     * Multimedia for this source
     */
    public List<Multimedia> multimedia = new ArrayList<Multimedia>();
    /**
     * The change date for this source
     */
    public ChangeDate changeDate;
    /**
     * User references for this source
     */
    public List<UserReference> userReferences = new ArrayList<UserReference>();
    /**
     * Source data
     */
    public SourceData data;
    /**
     * Text from the source
     */
    public List<String> sourceText = new ArrayList<String>();
    /**
     * A repository Citation
     */
    public RepositoryCitation repositoryCitation;
    /**
     * The xref of this source
     */
    public String xref;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Source)) {
            return false;
        }
        Source other = (Source) obj;
        if (changeDate == null) {
            if (other.changeDate != null) {
                return false;
            }
        } else if (!changeDate.equals(other.changeDate)) {
            return false;
        }
        if (data == null) {
            if (other.data != null) {
                return false;
            }
        } else if (!data.equals(other.data)) {
            return false;
        }
        if (multimedia == null) {
            if (other.multimedia != null) {
                return false;
            }
        } else if (!multimedia.equals(other.multimedia)) {
            return false;
        }
        if (notes == null) {
            if (other.notes != null) {
                return false;
            }
        } else if (!notes.equals(other.notes)) {
            return false;
        }
        if (originatorsAuthors == null) {
            if (other.originatorsAuthors != null) {
                return false;
            }
        } else if (!originatorsAuthors.equals(other.originatorsAuthors)) {
            return false;
        }
        if (publicationFacts == null) {
            if (other.publicationFacts != null) {
                return false;
            }
        } else if (!publicationFacts.equals(other.publicationFacts)) {
            return false;
        }
        if (recIdNumber == null) {
            if (other.recIdNumber != null) {
                return false;
            }
        } else if (!recIdNumber.equals(other.recIdNumber)) {
            return false;
        }
        if (regFileNumber == null) {
            if (other.regFileNumber != null) {
                return false;
            }
        } else if (!regFileNumber.equals(other.regFileNumber)) {
            return false;
        }
        if (repositoryCitation == null) {
            if (other.repositoryCitation != null) {
                return false;
            }
        } else if (!repositoryCitation.equals(other.repositoryCitation)) {
            return false;
        }
        if (sourceFiledBy == null) {
            if (other.sourceFiledBy != null) {
                return false;
            }
        } else if (!sourceFiledBy.equals(other.sourceFiledBy)) {
            return false;
        }
        if (sourceText == null) {
            if (other.sourceText != null) {
                return false;
            }
        } else if (!sourceText.equals(other.sourceText)) {
            return false;
        }
        if (title == null) {
            if (other.title != null) {
                return false;
            }
        } else if (!title.equals(other.title)) {
            return false;
        }
        if (userReferences == null) {
            if (other.userReferences != null) {
                return false;
            }
        } else if (!userReferences.equals(other.userReferences)) {
            return false;
        }
        if (xref == null) {
            if (other.xref != null) {
                return false;
            }
        } else if (!xref.equals(other.xref)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((changeDate == null) ? 0 : changeDate.hashCode());
        result = prime * result + ((data == null) ? 0 : data.hashCode());
        result = prime * result
                + ((multimedia == null) ? 0 : multimedia.hashCode());
        result = prime * result + ((notes == null) ? 0 : notes.hashCode());
        result = prime
                * result
                + ((originatorsAuthors == null) ? 0 : originatorsAuthors
                        .hashCode());
        result = prime
                * result
                + ((publicationFacts == null) ? 0 : publicationFacts.hashCode());
        result = prime * result
                + ((recIdNumber == null) ? 0 : recIdNumber.hashCode());
        result = prime * result
                + ((regFileNumber == null) ? 0 : regFileNumber.hashCode());
        result = prime
                * result
                + ((repositoryCitation == null) ? 0 : repositoryCitation
                        .hashCode());
        result = prime * result
                + ((sourceFiledBy == null) ? 0 : sourceFiledBy.hashCode());
        result = prime * result
                + ((sourceText == null) ? 0 : sourceText.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        result = prime * result
                + ((userReferences == null) ? 0 : userReferences.hashCode());
        result = prime * result + ((xref == null) ? 0 : xref.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "Source [xref=" + xref + ", regFileNumber=" + regFileNumber
                + ", recIdNumber=" + recIdNumber + ", sourceFiledBy="
                + sourceFiledBy + ", title=" + title + ", notes=" + notes
                + ", publicationFacts=" + publicationFacts
                + ", originatorsAuthors=" + originatorsAuthors
                + ", multimedia=" + multimedia + ", changeDate=" + changeDate
                + ", userReferences=" + userReferences + ", data=" + data
                + ", sourceText=" + sourceText + ", repositoryCitation="
                + repositoryCitation + "]";
    }

}
