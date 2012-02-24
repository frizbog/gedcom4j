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
