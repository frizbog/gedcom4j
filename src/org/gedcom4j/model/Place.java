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
package org.gedcom4j.model;

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

    /**
     * Phonetic variations on the place name. New for GEDCOM 5.5.1.
     */
    public List<NameVariation> phonetic = new ArrayList<NameVariation>();

    /**
     * Romanized variations on the place name. New for GEDCOM 5.5.1.
     */
    public List<NameVariation> romanized = new ArrayList<NameVariation>();

    /**
     * Latitude. New for GEDCOM 5.5.1.
     */
    public String latitude;

    /**
     * Longitude. New for GEDCOM 5.5.1.
     */
    public String longitude;

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
        if (latitude == null) {
            if (other.latitude != null) {
                return false;
            }
        } else if (!latitude.equals(other.latitude)) {
            return false;
        }
        if (longitude == null) {
            if (other.longitude != null) {
                return false;
            }
        } else if (!longitude.equals(other.longitude)) {
            return false;
        }
        if (notes == null) {
            if (other.notes != null) {
                return false;
            }
        } else if (!notes.equals(other.notes)) {
            return false;
        }
        if (phonetic == null) {
            if (other.phonetic != null) {
                return false;
            }
        } else if (!phonetic.equals(other.phonetic)) {
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
        if (romanized == null) {
            if (other.romanized != null) {
                return false;
            }
        } else if (!romanized.equals(other.romanized)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (citations == null ? 0 : citations.hashCode());
        result = prime * result + (latitude == null ? 0 : latitude.hashCode());
        result = prime * result + (longitude == null ? 0 : longitude.hashCode());
        result = prime * result + (notes == null ? 0 : notes.hashCode());
        result = prime * result + (phonetic == null ? 0 : phonetic.hashCode());
        result = prime * result + (placeFormat == null ? 0 : placeFormat.hashCode());
        result = prime * result + (placeName == null ? 0 : placeName.hashCode());
        result = prime * result + (romanized == null ? 0 : romanized.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "Place [placeName=" + placeName + ", placeFormat=" + placeFormat + ", citations=" + citations + ", notes=" + notes + ", phonetic="
                + phonetic + ", romanized=" + romanized + ", latitude=" + latitude + ", longitude=" + longitude + "]";
    }
}
