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
 * A base class for LDS sealing/ordinance data
 * 
 * @author frizbog1
 * 
 */
public abstract class AbstractLdsOrdinance {

    /**
     * The status
     */
    public String status;
    /**
     * The date
     */
    public String date;
    /**
     * The temple code
     */
    public String temple;
    /**
     * The place
     */
    public String place;
    /**
     * The citations for this ordinance
     */
    public List<AbstractCitation> citations = new ArrayList<AbstractCitation>();
    /**
     * The notes for this ordinance
     */
    public List<Note> notes = new ArrayList<Note>();

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof AbstractLdsOrdinance)) {
            return false;
        }
        AbstractLdsOrdinance other = (AbstractLdsOrdinance) obj;
        if (citations == null) {
            if (other.citations != null) {
                return false;
            }
        } else if (!citations.equals(other.citations)) {
            return false;
        }
        if (date == null) {
            if (other.date != null) {
                return false;
            }
        } else if (!date.equals(other.date)) {
            return false;
        }
        if (notes == null) {
            if (other.notes != null) {
                return false;
            }
        } else if (!notes.equals(other.notes)) {
            return false;
        }
        if (place == null) {
            if (other.place != null) {
                return false;
            }
        } else if (!place.equals(other.place)) {
            return false;
        }
        if (status == null) {
            if (other.status != null) {
                return false;
            }
        } else if (!status.equals(other.status)) {
            return false;
        }
        if (temple == null) {
            if (other.temple != null) {
                return false;
            }
        } else if (!temple.equals(other.temple)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((citations == null) ? 0 : citations.hashCode());
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + ((notes == null) ? 0 : notes.hashCode());
        result = prime * result + ((place == null) ? 0 : place.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        result = prime * result + ((temple == null) ? 0 : temple.hashCode());
        return result;
    }

}
