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

/**
 * An LDS Ordinance for an individual
 * 
 * @author frizbog1
 */
public class LdsIndividualOrdinance extends AbstractLdsOrdinance {
    /**
     * The family in which the individual was a child - used for SLGC types
     */
    public FamilyChild familyWhereChild;
    /**
     * The type
     */
    public LdsIndividualOrdinanceType type;

    /**
     * Allows for a Y or null to be processed after the type. Not strictly part
     * of the GEDCOM, but allows for flexibility
     */
    public String yNull;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof LdsIndividualOrdinance)) {
            return false;
        }
        LdsIndividualOrdinance other = (LdsIndividualOrdinance) obj;
        if (familyWhereChild == null) {
            if (other.familyWhereChild != null) {
                return false;
            }
        } else if (!familyWhereChild.equals(other.familyWhereChild)) {
            return false;
        }
        if (type != other.type) {
            return false;
        }
        if (yNull == null) {
            if (other.yNull != null) {
                return false;
            }
        } else if (!yNull.equals(other.yNull)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime
                * result
                + ((familyWhereChild == null) ? 0 : familyWhereChild.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((yNull == null) ? 0 : yNull.hashCode());
        return result;
    }
}
