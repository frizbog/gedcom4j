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
package com.mattharrah.gedcom4j.model;

/**
 * A submission of a gedcom transmission. Corresponds to SUBMISSION_RECORD in
 * the GEDCOM standard.
 * 
 * @author frizbog1
 * 
 */
public class Submission {
    /**
     * The xref for this submission - required field
     */
    public String xref;
    /**
     * The submitter of this submission
     */
    public Submitter submitter;
    /**
     * The name of the family file
     */
    public String nameOfFamilyFile;
    /**
     * The temple code for this submission
     */
    public String templeCode;
    /**
     * The number of ancestor generations
     */
    public String ancestorsCount;
    /**
     * The number of descendant generations
     */
    public String descendantsCount;
    /**
     * The ordinance process flag
     */
    public String ordinanceProcessFlag;
    /**
     * The automated record id
     */
    public String recIdNumber;

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
        if (obj == null) {
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((ancestorsCount == null) ? 0 : ancestorsCount.hashCode());
        result = prime
                * result
                + ((descendantsCount == null) ? 0 : descendantsCount.hashCode());
        result = prime
                * result
                + ((nameOfFamilyFile == null) ? 0 : nameOfFamilyFile.hashCode());
        result = prime
                * result
                + ((ordinanceProcessFlag == null) ? 0 : ordinanceProcessFlag
                        .hashCode());
        result = prime * result
                + ((recIdNumber == null) ? 0 : recIdNumber.hashCode());
        result = prime * result
                + ((submitter == null) ? 0 : submitter.hashCode());
        result = prime * result
                + ((templeCode == null) ? 0 : templeCode.hashCode());
        result = prime * result + ((xref == null) ? 0 : xref.hashCode());
        return result;
    }
}
