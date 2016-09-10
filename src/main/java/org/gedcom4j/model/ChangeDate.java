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
 * A date/time that a change was made.
 * 
 * @author frizbog1
 */
public class ChangeDate extends AbstractNotesElement {
    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 6134688970119877487L;

    /**
     * The date (as a string)
     */
    private StringWithCustomTags date;

    /**
     * The time (as a string)
     */
    private StringWithCustomTags time;

    /** Default constructor */
    public ChangeDate() {
        // Default constructor does nothing
    }

    /**
     * Copy constructor
     * 
     * @param other
     *            the other object to copy
     */
    public ChangeDate(ChangeDate other) {
        super(other);
        if (other.getDate() != null) {
            date = new StringWithCustomTags(other.date);
        }
        if (other.getTime() != null) {
            time = new StringWithCustomTags(other.time);
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
        ChangeDate other = (ChangeDate) obj;
        if (date == null) {
            if (other.date != null) {
                return false;
            }
        } else if (!date.equals(other.date)) {
            return false;
        }
        if (time == null) {
            if (other.time != null) {
                return false;
            }
        } else if (!time.equals(other.time)) {
            return false;
        }
        return true;
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
     * Gets the time.
     *
     * @return the time
     */
    public StringWithCustomTags getTime() {
        return time;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (date == null ? 0 : date.hashCode());
        result = prime * result + (time == null ? 0 : time.hashCode());
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
     * Sets the time.
     *
     * @param time
     *            the new time
     */
    public void setTime(StringWithCustomTags time) {
        this.time = time;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(32);
        builder.append("ChangeDate [");
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
        if (time != null) {
            builder.append("time=");
            builder.append(time);
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
