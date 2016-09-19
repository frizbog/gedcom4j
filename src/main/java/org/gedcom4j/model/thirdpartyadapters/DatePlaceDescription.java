package org.gedcom4j.model.thirdpartyadapters;

import org.gedcom4j.model.AbstractElement;

/**
 * A generic object with a date (string), a place, and a description.
 */
public class DatePlaceDescription extends AbstractElement {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -4700046774992424771L;

    /** The date. */
    private String date;

    /** The place. */
    private String place;

    /** The description. */
    private String description;

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
        if (!(obj instanceof DatePlaceDescription)) {
            return false;
        }
        DatePlaceDescription other = (DatePlaceDescription) obj;
        if (date == null) {
            if (other.date != null) {
                return false;
            }
        } else if (!date.equals(other.date)) {
            return false;
        }
        if (description == null) {
            if (other.description != null) {
                return false;
            }
        } else if (!description.equals(other.description)) {
            return false;
        }
        if (place == null) {
            if (other.place != null) {
                return false;
            }
        } else if (!place.equals(other.place)) {
            return false;
        }
        return true;
    }

    /**
     * Get the date
     * 
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * Get the description
     * 
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the place
     * 
     * @return the place
     */
    public String getPlace() {
        return place;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((place == null) ? 0 : place.hashCode());
        return result;
    }

    /**
     * Set the date
     * 
     * @param date
     *            the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Set the description
     * 
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Set the place
     * 
     * @param place
     *            the place to set
     */
    public void setPlace(String place) {
        this.place = place;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(30);
        builder.append("DatePlaceDescription [");
        if (date != null) {
            builder.append("date=");
            builder.append(date);
            builder.append(", ");
        }
        if (place != null) {
            builder.append("place=");
            builder.append(place);
            builder.append(", ");
        }
        if (description != null) {
            builder.append("description=");
            builder.append(description);
        }
        builder.append("]");
        return builder.toString();
    }

}
