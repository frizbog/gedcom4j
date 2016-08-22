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

import java.util.ArrayList;
import java.util.List;

import org.gedcom4j.Options;

/**
 * A base class for LDS sealing/ordinance data
 * 
 * @author frizbog1
 * 
 */
public abstract class AbstractLdsOrdinance extends AbstractNotesElement implements HasCitations {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -5525451103364917970L;

    /**
     * The citations for this ordinance
     */
    protected List<AbstractCitation> citations = getCitations(Options.isCollectionInitializationEnabled());

    /**
     * The date
     */
    protected StringWithCustomTags date;

    /**
     * The place
     */
    protected StringWithCustomTags place;

    /**
     * The status
     */
    protected StringWithCustomTags status;

    /**
     * The temple code
     */
    protected StringWithCustomTags temple;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof AbstractLdsOrdinance)) {
            return false;
        }
        AbstractLdsOrdinance other = (AbstractLdsOrdinance) obj;
        if (getCitations() == null) {
            if (other.getCitations() != null) {
                return false;
            }
        } else if (!getCitations().equals(other.getCitations())) {
            return false;
        }
        if (date == null) {
            if (other.date != null) {
                return false;
            }
        } else if (!date.equals(other.date)) {
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

    /**
     * Gets the citations.
     *
     * @return the citations
     */
    public List<AbstractCitation> getCitations() {
        return citations;
    }

    /**
     * Get the citations
     * 
     * @param initializeIfNeeded
     *            initialize the collection if needed
     * @return the citations
     */
    public List<AbstractCitation> getCitations(boolean initializeIfNeeded) {
        if (initializeIfNeeded && citations == null) {
            citations = new ArrayList<AbstractCitation>();
        }
        return citations;
    }

    /**
     * Gets the date.
     *
     * @return the date
     */
    public StringWithCustomTags getDate() {
        return date;
    }

    /**
     * Gets the place.
     *
     * @return the place
     */
    public StringWithCustomTags getPlace() {
        return place;
    }

    /**
     * Gets the status.
     *
     * @return the status
     */
    public StringWithCustomTags getStatus() {
        return status;
    }

    /**
     * Gets the temple.
     *
     * @return the temple
     */
    public StringWithCustomTags getTemple() {
        return temple;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (getCitations() == null ? 0 : getCitations().hashCode());
        result = prime * result + (date == null ? 0 : date.hashCode());
        result = prime * result + (place == null ? 0 : place.hashCode());
        result = prime * result + (status == null ? 0 : status.hashCode());
        result = prime * result + (temple == null ? 0 : temple.hashCode());
        return result;
    }

    /**
     * Sets the date.
     *
     * @param date
     *            the new date
     */
    public void setDate(StringWithCustomTags date) {
        this.date = date;
    }

    /**
     * Sets the place.
     *
     * @param place
     *            the new place
     */
    public void setPlace(StringWithCustomTags place) {
        this.place = place;
    }

    /**
     * Sets the status.
     *
     * @param status
     *            the new status
     */
    public void setStatus(StringWithCustomTags status) {
        this.status = status;
    }

    /**
     * Sets the temple.
     *
     * @param temple
     *            the new temple
     */
    public void setTemple(StringWithCustomTags temple) {
        this.temple = temple;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(64);
        builder.append("AbstractLdsOrdinance [");
        if (citations != null) {
            builder.append("citations=");
            builder.append(citations);
            builder.append(", ");
        }
        if (date != null) {
            builder.append("date=");
            builder.append(date);
            builder.append(", ");
        }
        if (getNotes() != null) {
            builder.append("notes=");
            builder.append(getNotes());
            builder.append(", ");
        }
        if (place != null) {
            builder.append("place=");
            builder.append(place);
            builder.append(", ");
        }
        if (status != null) {
            builder.append("status=");
            builder.append(status);
            builder.append(", ");
        }
        if (temple != null) {
            builder.append("temple=");
            builder.append(temple);
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
