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

/**
 * A submission of a gedcom transmission. Corresponds to SUBMISSION_RECORD in the GEDCOM standard.
 * 
 * @author frizbog1
 * 
 */
public class Submission extends AbstractElement {
    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -2881845846882881000L;

    /**
     * The number of ancestor generations
     */
    private StringWithCustomTags ancestorsCount;

    /**
     * The number of descendant generations
     */
    private StringWithCustomTags descendantsCount;

    /**
     * The name of the family file
     */
    private StringWithCustomTags nameOfFamilyFile;

    /**
     * The ordinance process flag
     */
    private StringWithCustomTags ordinanceProcessFlag;

    /**
     * The record ID number
     */
    private StringWithCustomTags recIdNumber;

    /**
     * The submitter of this submission
     */
    private Submitter submitter;

    /**
     * The temple code for this submission
     */
    private StringWithCustomTags templeCode;

    /**
     * The xref for this submitter
     */
    private String xref;

    /**
     * Constructor, takes required xref value
     * 
     * @param xref
     *            the xref id for this submission
     */
    public Submission(String xref) {
        this.xref = xref;
    }

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
        Submission other = (Submission) obj;
        if (ancestorsCount == null) {
            if (other.ancestorsCount != null) {
                return false;
            }
        } else if (!ancestorsCount.equals(other.ancestorsCount)) {
            return false;
        }
        if (descendantsCount == null) {
            if (other.descendantsCount != null) {
                return false;
            }
        } else if (!descendantsCount.equals(other.descendantsCount)) {
            return false;
        }
        if (nameOfFamilyFile == null) {
            if (other.nameOfFamilyFile != null) {
                return false;
            }
        } else if (!nameOfFamilyFile.equals(other.nameOfFamilyFile)) {
            return false;
        }
        if (ordinanceProcessFlag == null) {
            if (other.ordinanceProcessFlag != null) {
                return false;
            }
        } else if (!ordinanceProcessFlag.equals(other.ordinanceProcessFlag)) {
            return false;
        }
        if (recIdNumber == null) {
            if (other.recIdNumber != null) {
                return false;
            }
        } else if (!recIdNumber.equals(other.recIdNumber)) {
            return false;
        }
        if (submitter == null) {
            if (other.submitter != null) {
                return false;
            }
        } else if (!submitter.equals(other.submitter)) {
            return false;
        }
        if (templeCode == null) {
            if (other.templeCode != null) {
                return false;
            }
        } else if (!templeCode.equals(other.templeCode)) {
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

    /**
     * Get the ancestorsCount
     * 
     * @return the ancestorsCount
     */
    public StringWithCustomTags getAncestorsCount() {
        return ancestorsCount;
    }

    /**
     * Get the descendantsCount
     * 
     * @return the descendantsCount
     */
    public StringWithCustomTags getDescendantsCount() {
        return descendantsCount;
    }

    /**
     * Get the nameOfFamilyFile
     * 
     * @return the nameOfFamilyFile
     */
    public StringWithCustomTags getNameOfFamilyFile() {
        return nameOfFamilyFile;
    }

    /**
     * Get the ordinanceProcessFlag
     * 
     * @return the ordinanceProcessFlag
     */
    public StringWithCustomTags getOrdinanceProcessFlag() {
        return ordinanceProcessFlag;
    }

    /**
     * Get the recIdNumber
     * 
     * @return the recIdNumber
     */
    public StringWithCustomTags getRecIdNumber() {
        return recIdNumber;
    }

    /**
     * Get the submitter
     * 
     * @return the submitter
     */
    public Submitter getSubmitter() {
        return submitter;
    }

    /**
     * Get the templeCode
     * 
     * @return the templeCode
     */
    public StringWithCustomTags getTempleCode() {
        return templeCode;
    }

    /**
     * Get the xref
     * 
     * @return the xref
     */
    public String getXref() {
        return xref;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (ancestorsCount == null ? 0 : ancestorsCount.hashCode());
        result = prime * result + (descendantsCount == null ? 0 : descendantsCount.hashCode());
        result = prime * result + (nameOfFamilyFile == null ? 0 : nameOfFamilyFile.hashCode());
        result = prime * result + (ordinanceProcessFlag == null ? 0 : ordinanceProcessFlag.hashCode());
        result = prime * result + (recIdNumber == null ? 0 : recIdNumber.hashCode());
        result = prime * result + (submitter == null ? 0 : submitter.hashCode());
        result = prime * result + (templeCode == null ? 0 : templeCode.hashCode());
        result = prime * result + (xref == null ? 0 : xref.hashCode());
        return result;
    }

    /**
     * Set the ancestorsCount
     * 
     * @param ancestorsCount
     *            the ancestorsCount to set
     */
    public void setAncestorsCount(StringWithCustomTags ancestorsCount) {
        this.ancestorsCount = ancestorsCount;
    }

    /**
     * Set the descendantsCount
     * 
     * @param descendantsCount
     *            the descendantsCount to set
     */
    public void setDescendantsCount(StringWithCustomTags descendantsCount) {
        this.descendantsCount = descendantsCount;
    }

    /**
     * Set the nameOfFamilyFile
     * 
     * @param nameOfFamilyFile
     *            the nameOfFamilyFile to set
     */
    public void setNameOfFamilyFile(StringWithCustomTags nameOfFamilyFile) {
        this.nameOfFamilyFile = nameOfFamilyFile;
    }

    /**
     * Set the ordinanceProcessFlag
     * 
     * @param ordinanceProcessFlag
     *            the ordinanceProcessFlag to set
     */
    public void setOrdinanceProcessFlag(StringWithCustomTags ordinanceProcessFlag) {
        this.ordinanceProcessFlag = ordinanceProcessFlag;
    }

    /**
     * Set the recIdNumber
     * 
     * @param recIdNumber
     *            the recIdNumber to set
     */
    public void setRecIdNumber(StringWithCustomTags recIdNumber) {
        this.recIdNumber = recIdNumber;
    }

    /**
     * Set the submitter
     * 
     * @param submitter
     *            the submitter to set
     */
    public void setSubmitter(Submitter submitter) {
        this.submitter = submitter;
    }

    /**
     * Set the templeCode
     * 
     * @param templeCode
     *            the templeCode to set
     */
    public void setTempleCode(StringWithCustomTags templeCode) {
        this.templeCode = templeCode;
    }

    /**
     * Set the xref
     * 
     * @param xref
     *            the xref to set
     */
    public void setXref(String xref) {
        this.xref = xref;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Submission [");
        if (ancestorsCount != null) {
            builder.append("ancestorsCount=");
            builder.append(ancestorsCount);
            builder.append(", ");
        }
        if (descendantsCount != null) {
            builder.append("descendantsCount=");
            builder.append(descendantsCount);
            builder.append(", ");
        }
        if (nameOfFamilyFile != null) {
            builder.append("nameOfFamilyFile=");
            builder.append(nameOfFamilyFile);
            builder.append(", ");
        }
        if (ordinanceProcessFlag != null) {
            builder.append("ordinanceProcessFlag=");
            builder.append(ordinanceProcessFlag);
            builder.append(", ");
        }
        if (recIdNumber != null) {
            builder.append("recIdNumber=");
            builder.append(recIdNumber);
            builder.append(", ");
        }
        if (submitter != null) {
            builder.append("submitter=");
            builder.append(submitter);
            builder.append(", ");
        }
        if (templeCode != null) {
            builder.append("templeCode=");
            builder.append(templeCode);
            builder.append(", ");
        }
        if (xref != null) {
            builder.append("xref=");
            builder.append(xref);
            builder.append(", ");
        }
        if (customTags != null) {
            builder.append("customTags=");
            builder.append(customTags);
        }
        builder.append("]");
        return builder.toString();
    }
}
