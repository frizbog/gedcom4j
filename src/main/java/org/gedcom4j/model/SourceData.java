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
 * Data in a Source structure. Corresponds to the set of fields under the DATA tag on a SOURCE_RECORD.
 * 
 * @author frizbog1
 */
public class SourceData extends AbstractNotesElement {
    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -5082188791769651553L;

    /**
     * The events recorded.
     */
    private List<EventRecorded> eventsRecorded = getEventsRecorded(Options.isCollectionInitializationEnabled());

    /**
     * The responsible agency.
     */
    private StringWithCustomTags respAgency;

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
        SourceData other = (SourceData) obj;
        if (eventsRecorded == null) {
            if (other.eventsRecorded != null) {
                return false;
            }
        } else if (!eventsRecorded.equals(other.eventsRecorded)) {
            return false;
        }
        if (respAgency == null) {
            if (other.respAgency != null) {
                return false;
            }
        } else if (!respAgency.equals(other.respAgency)) {
            return false;
        }
        return true;
    }

    /**
     * Gets the events recorded.
     *
     * @return the events recorded
     */
    public List<EventRecorded> getEventsRecorded() {
        return eventsRecorded;
    }

    /**
     * Get the events recorded
     * 
     * @param initializeIfNeeded
     *            initialize the collection, if needed?
     * @return the events recorded
     */
    public List<EventRecorded> getEventsRecorded(boolean initializeIfNeeded) {
        if (initializeIfNeeded && eventsRecorded == null) {
            eventsRecorded = new ArrayList<>(0);
        }
        return eventsRecorded;
    }

    /**
     * Gets the resp agency.
     *
     * @return the resp agency
     */
    public StringWithCustomTags getRespAgency() {
        return respAgency;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (eventsRecorded == null ? 0 : eventsRecorded.hashCode());
        result = prime * result + (respAgency == null ? 0 : respAgency.hashCode());
        return result;
    }

    /**
     * Sets the resp agency.
     *
     * @param respAgency
     *            the new resp agency
     */
    public void setRespAgency(StringWithCustomTags respAgency) {
        this.respAgency = respAgency;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(32);
        builder.append("SourceData [");
        if (eventsRecorded != null) {
            builder.append("eventsRecorded=");
            builder.append(eventsRecorded);
            builder.append(", ");
        }
        if (getNotes() != null) {
            builder.append("notes=");
            builder.append(getNotes());
            builder.append(", ");
        }
        if (respAgency != null) {
            builder.append("respAgency=");
            builder.append(respAgency);
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
