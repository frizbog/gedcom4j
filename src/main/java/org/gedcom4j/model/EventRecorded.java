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

/**
 * A event that was recorded in a source. Corresponds to a single instance of the Events Recorded multi-block in the Data Model
 * Chart at the end of the GEDCOM spec.
 * 
 * @author frizbog1
 */
public class EventRecorded extends AbstractElement {
    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -6640977866006853891L;

    /**
     * The date period covered in the source
     */
    private StringWithCustomFacts datePeriod;

    /**
     * The event type (tag)
     */
    private String eventType;

    /**
     * The jurisdiction of the source. Corresponds to SOURCE_JURISDICTION_PLACE in the GEDCOM spec.
     */
    private StringWithCustomFacts jurisdiction;

    /** Default constructor */
    public EventRecorded() {
        // Default constructor does nothing
    }

    /**
     * Copy constructor
     * 
     * @param other
     *            object being copied
     */
    public EventRecorded(EventRecorded other) {
        super(other);
        if (other.datePeriod != null) {
            datePeriod = new StringWithCustomFacts(other.datePeriod);
        }
        eventType = other.eventType;
        if (other.jurisdiction != null) {
            jurisdiction = new StringWithCustomFacts(other.jurisdiction);
        }
    }

    /**
     * {@inheritDoc}
     */
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
        EventRecorded other = (EventRecorded) obj;
        if (datePeriod == null) {
            if (other.datePeriod != null) {
                return false;
            }
        } else if (!datePeriod.equals(other.datePeriod)) {
            return false;
        }
        if (eventType == null) {
            if (other.eventType != null) {
                return false;
            }
        } else if (!eventType.equals(other.eventType)) {
            return false;
        }
        if (jurisdiction == null) {
            if (other.jurisdiction != null) {
                return false;
            }
        } else if (!jurisdiction.equals(other.jurisdiction)) {
            return false;
        }
        return true;
    }

    /**
     * Gets the date period.
     *
     * @return the date period
     */
    public StringWithCustomFacts getDatePeriod() {
        return datePeriod;
    }

    /**
     * Gets the event type.
     *
     * @return the event type
     */
    public String getEventType() {
        return eventType;
    }

    /**
     * Gets the jurisdiction.
     *
     * @return the jurisdiction
     */
    public StringWithCustomFacts getJurisdiction() {
        return jurisdiction;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (datePeriod == null ? 0 : datePeriod.hashCode());
        result = prime * result + (eventType == null ? 0 : eventType.hashCode());
        result = prime * result + (jurisdiction == null ? 0 : jurisdiction.hashCode());
        return result;
    }

    /**
     * Sets the date period.
     *
     * @param datePeriod
     *            the new date period
     */
    public void setDatePeriod(String datePeriod) {
        this.datePeriod = datePeriod == null ? null : new StringWithCustomFacts(datePeriod);
    }

    /**
     * Sets the date period.
     *
     * @param datePeriod
     *            the new date period
     */
    public void setDatePeriod(StringWithCustomFacts datePeriod) {
        this.datePeriod = datePeriod;
    }

    /**
     * Sets the event type.
     *
     * @param eventType
     *            the new event type
     */
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    /**
     * Sets the jurisdiction.
     *
     * @param jurisdiction
     *            the new jurisdiction
     */
    public void setJurisdiction(String jurisdiction) {
        this.jurisdiction = jurisdiction == null ? null : new StringWithCustomFacts(jurisdiction);
    }

    /**
     * Sets the jurisdiction.
     *
     * @param jurisdiction
     *            the new jurisdiction
     */
    public void setJurisdiction(StringWithCustomFacts jurisdiction) {
        this.jurisdiction = jurisdiction;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(32);
        builder.append("EventRecorded [");
        if (datePeriod != null) {
            builder.append("datePeriod=");
            builder.append(datePeriod);
            builder.append(", ");
        }
        if (eventType != null) {
            builder.append("eventType=");
            builder.append(eventType);
            builder.append(", ");
        }
        if (jurisdiction != null) {
            builder.append("jurisdiction=");
            builder.append(jurisdiction);
            builder.append(", ");
        }
        if (getCustomFacts() != null) {
            builder.append("customFacts=");
            builder.append(getCustomFacts());
        }
        builder.append("]");
        return builder.toString();
    }
}
