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
 * A Place. Corresponds to PLACE_STRUCTURE in GEDCOM standard.
 * 
 * @author frizbog1
 */
public class Place extends AbstractNotesElement implements HasCitations {
    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 7023491338742765865L;

    /**
     * The citations for this object
     */
    private List<AbstractCitation> citations = getCitations(Options.isCollectionInitializationEnabled());

    /**
     * Latitude. New for GEDCOM 5.5.1.
     */
    private StringWithCustomTags latitude;

    /**
     * Longitude. New for GEDCOM 5.5.1.
     */
    private StringWithCustomTags longitude;

    /**
     * Phonetic variations on the place name. New for GEDCOM 5.5.1.
     */
    private List<AbstractNameVariation> phonetic = getPhonetic(Options.isCollectionInitializationEnabled());

    /**
     * The place format (hierarchy)
     */
    private StringWithCustomTags placeFormat;

    /**
     * The place name (value)
     */
    private String placeName;

    /**
     * Romanized variations on the place name. New for GEDCOM 5.5.1.
     */
    private List<AbstractNameVariation> romanized = getRomanized(Options.isCollectionInitializationEnabled());

    /** Default constructor */
    public Place() {
        // Default constructor does nothing
    }

    /**
     * Copy constructor
     * 
     * @param other
     *            object being copied
     */
    public Place(Place other) {
        super(other);
        if (other.citations != null) {
            citations = new ArrayList<>();
            for (AbstractCitation ac : other.citations) {
                if (ac instanceof CitationWithoutSource) {
                    citations.add(new CitationWithoutSource((CitationWithoutSource) ac));
                } else if (ac instanceof CitationWithSource) {
                    citations.add(new CitationWithSource((CitationWithSource) ac));
                }
            }
        }
        if (other.latitude != null) {
            latitude = new StringWithCustomTags(other.latitude);
        }
        if (other.longitude != null) {
            longitude = new StringWithCustomTags(other.longitude);
        }
        if (other.phonetic != null) {
            phonetic = new ArrayList<>();
            for (AbstractNameVariation ph : other.phonetic) {
                phonetic.add(new PlaceNameVariation((PlaceNameVariation) ph));
            }
        }
        if (other.placeFormat != null) {
            placeFormat = new StringWithCustomTags(other.placeFormat);
        }
        placeName = other.placeName;
        if (other.romanized != null) {
            romanized = new ArrayList<>();
            for (AbstractNameVariation ph : other.romanized) {
                romanized.add(new PlaceNameVariation((PlaceNameVariation) ph));
            }
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
     *            initialize the collection if needed?
     * 
     * @return the citations
     */
    public List<AbstractCitation> getCitations(boolean initializeIfNeeded) {
        if (initializeIfNeeded && citations == null) {
            citations = new ArrayList<>(0);
        }
        return citations;
    }

    /**
     * Gets the latitude.
     *
     * @return the latitude
     */
    public StringWithCustomTags getLatitude() {
        return latitude;
    }

    /**
     * Gets the longitude.
     *
     * @return the longitude
     */
    public StringWithCustomTags getLongitude() {
        return longitude;
    }

    /**
     * Gets the phonetic variation(s)
     *
     * @return the phonetic variation(s)
     */
    public List<AbstractNameVariation> getPhonetic() {
        return phonetic;
    }

    /**
     * Get the phonetic variation(s)
     * 
     * @param initializeIfNeeded
     *            initilize the collection, if needed?
     * @return the phonetic variation(s)
     */
    public List<AbstractNameVariation> getPhonetic(boolean initializeIfNeeded) {
        if (initializeIfNeeded && phonetic == null) {
            phonetic = new ArrayList<>(0);
        }
        return phonetic;
    }

    /**
     * Gets the place format.
     *
     * @return the place format
     */
    public StringWithCustomTags getPlaceFormat() {
        return placeFormat;
    }

    /**
     * Gets the place name.
     *
     * @return the place name
     */
    public String getPlaceName() {
        return placeName;
    }

    /**
     * Gets the romanized variation(s)
     *
     * @return the romanized variation(s)
     */
    public List<AbstractNameVariation> getRomanized() {
        return romanized;
    }

    /**
     * Get the romanized variation(s)
     * 
     * @param initializeIfNeeded
     *            initialize the collection, if needed?
     * @return the romanized variation(s)
     */
    public List<AbstractNameVariation> getRomanized(boolean initializeIfNeeded) {
        if (initializeIfNeeded && romanized == null) {
            romanized = new ArrayList<>(0);
        }
        return romanized;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (citations == null ? 0 : citations.hashCode());
        result = prime * result + (latitude == null ? 0 : latitude.hashCode());
        result = prime * result + (longitude == null ? 0 : longitude.hashCode());
        result = prime * result + (phonetic == null ? 0 : phonetic.hashCode());
        result = prime * result + (placeFormat == null ? 0 : placeFormat.hashCode());
        result = prime * result + (placeName == null ? 0 : placeName.hashCode());
        result = prime * result + (romanized == null ? 0 : romanized.hashCode());
        return result;
    }

    /**
     * Sets the latitude.
     *
     * @param latitude
     *            the new latitude
     */
    public void setLatitude(StringWithCustomTags latitude) {
        this.latitude = latitude;
    }

    /**
     * Sets the longitude.
     *
     * @param longitude
     *            the new longitude
     */
    public void setLongitude(StringWithCustomTags longitude) {
        this.longitude = longitude;
    }

    /**
     * Sets the place format.
     *
     * @param placeFormat
     *            the new place format
     */
    public void setPlaceFormat(StringWithCustomTags placeFormat) {
        this.placeFormat = placeFormat;
    }

    /**
     * Sets the place name.
     *
     * @param placeName
     *            the new place name
     */
    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(64);
        builder.append("Place [");
        if (citations != null) {
            builder.append("citations=");
            builder.append(citations);
            builder.append(", ");
        }
        if (latitude != null) {
            builder.append("latitude=");
            builder.append(latitude);
            builder.append(", ");
        }
        if (longitude != null) {
            builder.append("longitude=");
            builder.append(longitude);
            builder.append(", ");
        }
        if (getNotes() != null) {
            builder.append("notes=");
            builder.append(getNotes());
            builder.append(", ");
        }
        if (phonetic != null) {
            builder.append("phonetic=");
            builder.append(phonetic);
            builder.append(", ");
        }
        if (placeFormat != null) {
            builder.append("placeFormat=");
            builder.append(placeFormat);
            builder.append(", ");
        }
        if (placeName != null) {
            builder.append("placeName=");
            builder.append(placeName);
            builder.append(", ");
        }
        if (romanized != null) {
            builder.append("romanized=");
            builder.append(romanized);
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
