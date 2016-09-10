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
 * A citation with a source. Corresponds to the first (preferred) form of the SOURCE_CITATION structure (which you'd do in Pascal
 * with a variant record, but here we use subclasses of a parent abstract class).
 * 
 * @author frizbog1
 * 
 */
public class CitationWithSource extends AbstractCitation {
    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 1886846774727359828L;

    /**
     * The quality of this citation. Supposed to be 0, 1, 2, or 3, but stored as a string since we're not doing math on it.
     */
    private StringWithCustomTags certainty;

    /**
     * A list of citation data entries
     */
    private List<CitationData> data = getData(Options.isCollectionInitializationEnabled());

    /**
     * The type of event or attribute cited from. Will be the tag from one of the the following three enum types:
     * {@link FamilyEventType}, {@link IndividualEventType}, {@link IndividualAttributeType}.
     */
    private StringWithCustomTags eventCited;

    /**
     * Multimedia links for this source citation
     */
    private List<Multimedia> multimedia = getMultimedia(Options.isCollectionInitializationEnabled());

    /**
     * The role in the event cited
     */
    private StringWithCustomTags roleInEvent;

    /**
     * A reference to the cited source
     */
    private Source source;

    /**
     * Where within the source is the information being cited
     */
    private StringWithCustomTags whereInSource;

    /** Default constructor */
    public CitationWithSource() {
        // Default constructor does nothing
    }

    /**
     * Copy constructor
     * 
     * @param other
     *            object being copied
     */
    public CitationWithSource(CitationWithSource other) {
        super(other);
        if (other.certainty != null) {
            certainty = new StringWithCustomTags(other.certainty);
        }
        if (other.data != null) {
            data = new ArrayList<>();
            for (CitationData d : other.data) {
                data.add(new CitationData(d));
            }
        }
        if (other.eventCited != null) {
            eventCited = new StringWithCustomTags(other.eventCited);
        }
        if (other.multimedia != null) {
            multimedia = new ArrayList<>();
            for (Multimedia m : other.multimedia) {
                multimedia.add(new Multimedia(m));
            }
        }
        if (other.roleInEvent != null) {
            roleInEvent = new StringWithCustomTags(other.roleInEvent);
        }
        if (other.source != null) {
            source = new Source(other.source);
        }
        if (other.whereInSource != null) {
            whereInSource = new StringWithCustomTags(other.whereInSource);
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
        CitationWithSource other = (CitationWithSource) obj;
        if (certainty == null) {
            if (other.certainty != null) {
                return false;
            }
        } else if (!certainty.equals(other.certainty)) {
            return false;
        }
        if (data == null) {
            if (other.data != null) {
                return false;
            }
        } else if (!data.equals(other.data)) {
            return false;
        }
        if (eventCited == null) {
            if (other.eventCited != null) {
                return false;
            }
        } else if (!eventCited.equals(other.eventCited)) {
            return false;
        }
        if (multimedia == null) {
            if (other.multimedia != null) {
                return false;
            }
        } else if (!multimedia.equals(other.multimedia)) {
            return false;
        }
        if (getNotes() == null) {
            if (other.getNotes() != null) {
                return false;
            }
        } else if (!getNotes().equals(other.getNotes())) {
            return false;
        }
        if (roleInEvent == null) {
            if (other.roleInEvent != null) {
                return false;
            }
        } else if (!roleInEvent.equals(other.roleInEvent)) {
            return false;
        }
        if (source == null) {
            if (other.source != null) {
                return false;
            }
        } else if (!source.equals(other.source)) {
            return false;
        }
        if (whereInSource == null) {
            if (other.whereInSource != null) {
                return false;
            }
        } else if (!whereInSource.equals(other.whereInSource)) {
            return false;
        }
        return true;
    }

    /**
     * Gets the certainty.
     *
     * @return the certainty
     */
    public StringWithCustomTags getCertainty() {
        return certainty;
    }

    /**
     * Gets the data.
     *
     * @return the data
     */
    public List<CitationData> getData() {
        return data;
    }

    /**
     * Get the data
     * 
     * @param initializeIfNeeded
     *            true if this collection should be created on-the-fly if it is currently null
     * @return the data
     */
    public List<CitationData> getData(boolean initializeIfNeeded) {
        if (initializeIfNeeded && data == null) {
            data = new ArrayList<>(0);
        }
        return data;
    }

    /**
     * Gets the event cited.
     *
     * @return the event cited
     */
    public StringWithCustomTags getEventCited() {
        return eventCited;
    }

    /**
     * Gets the multimedia.
     *
     * @return the multimedia
     */
    public List<Multimedia> getMultimedia() {
        return multimedia;
    }

    /**
     * Get the multimedia
     * 
     * @param initializeIfNeeded
     *            true if this collection should be created on-the-fly if it is currently null
     * @return the multimedia
     */
    public List<Multimedia> getMultimedia(boolean initializeIfNeeded) {
        if (initializeIfNeeded && multimedia == null) {
            multimedia = new ArrayList<>(0);
        }
        return multimedia;
    }

    /**
     * Gets the role in event.
     *
     * @return the role in event
     */
    public StringWithCustomTags getRoleInEvent() {
        return roleInEvent;
    }

    /**
     * Gets the source.
     *
     * @return the source
     */
    public Source getSource() {
        return source;
    }

    /**
     * Gets where in the source is being cited
     *
     * @return where in source
     */
    public StringWithCustomTags getWhereInSource() {
        return whereInSource;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (certainty == null ? 0 : certainty.hashCode());
        result = prime * result + (data == null ? 0 : data.hashCode());
        result = prime * result + (eventCited == null ? 0 : eventCited.hashCode());
        result = prime * result + (multimedia == null ? 0 : multimedia.hashCode());
        result = prime * result + (getNotes() == null ? 0 : getNotes().hashCode());
        result = prime * result + (roleInEvent == null ? 0 : roleInEvent.hashCode());
        result = prime * result + (source == null ? 0 : source.hashCode());
        result = prime * result + (whereInSource == null ? 0 : whereInSource.hashCode());
        return result;
    }

    /**
     * Sets the certainty.
     *
     * @param certainty
     *            the new certainty
     */
    public void setCertainty(StringWithCustomTags certainty) {
        this.certainty = certainty;
    }

    /**
     * Sets the event cited.
     *
     * @param eventCited
     *            the new event cited
     */
    public void setEventCited(StringWithCustomTags eventCited) {
        this.eventCited = eventCited;
    }

    /**
     * Sets the role in event.
     *
     * @param roleInEvent
     *            the new role in event
     */
    public void setRoleInEvent(StringWithCustomTags roleInEvent) {
        this.roleInEvent = roleInEvent;
    }

    /**
     * Sets the source.
     *
     * @param source
     *            the new source
     */
    public void setSource(Source source) {
        this.source = source;
    }

    /**
     * Sets where in the source is being cited
     *
     * @param whereInSource
     *            where in the source is being cited
     */
    public void setWhereInSource(StringWithCustomTags whereInSource) {
        this.whereInSource = whereInSource;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(64);
        builder.append("CitationWithSource [");
        if (certainty != null) {
            builder.append("certainty=");
            builder.append(certainty);
            builder.append(", ");
        }
        if (data != null) {
            builder.append("data=");
            builder.append(data);
            builder.append(", ");
        }
        if (eventCited != null) {
            builder.append("eventCited=");
            builder.append(eventCited);
            builder.append(", ");
        }
        if (multimedia != null) {
            builder.append("multimedia=");
            builder.append(multimedia);
            builder.append(", ");
        }
        if (roleInEvent != null) {
            builder.append("roleInEvent=");
            builder.append(roleInEvent);
            builder.append(", ");
        }
        if (source != null) {
            builder.append("source=");
            builder.append(source);
            builder.append(", ");
        }
        if (whereInSource != null) {
            builder.append("whereInSource=");
            builder.append(whereInSource);
            builder.append(", ");
        }
        if (getNotes() != null) {
            builder.append("notes=");
            builder.append(getNotes());
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
