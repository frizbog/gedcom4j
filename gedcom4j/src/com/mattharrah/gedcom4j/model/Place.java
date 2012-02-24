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

import java.util.ArrayList;
import java.util.List;

/**
 * A Place. Corresponds to PLACE_STRUCTURE in GEDCOM standard.
 * 
 * @author frizbog1
 * 
 */
public class Place {
    /**
     * The place name (value)
     */
    public String placeName;
    /**
     * The place format (hierarchy)
     */
    public String placeFormat;
    /**
     * Source citations for this place
     */
    public List<AbstractCitation> citations = new ArrayList<AbstractCitation>();
    /**
     * Notes for this place
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
        if (getClass() != obj.getClass()) {
            return false;
        }
        Place other = (Place) obj;
        if (citations == null) {
            if (other.citations != null) {
                return false;
            }
        } else if (!citations.equals(other.citations)) {
            return false;
        }
        if (notes == null) {
            if (other.notes != null) {
                return false;
            }
        } else if (!notes.equals(other.notes)) {
            return false;
        }
        if (placeFormat == null) {
            if (other.placeFormat != null) {
                return false;
            }
        } else if (!placeFormat.equals(other.placeFormat)) {
            return false;
        }
        if (placeName == null) {
            if (other.placeName != null) {
                return false;
            }
        } else if (!placeName.equals(other.placeName)) {
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
        result = prime * result + ((notes == null) ? 0 : notes.hashCode());
        result = prime * result
                + ((placeFormat == null) ? 0 : placeFormat.hashCode());
        result = prime * result
                + ((placeName == null) ? 0 : placeName.hashCode());
        return result;
    }
}
