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
 * Represents an Association between two individuals. Corresponds to ASSOCIATION_STRUCTURE in the GEDCOM spec.
 * 
 * @author frizbog1
 */
public class Association extends AbstractNotesElement {
    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -4189651960132766112L;

    /**
     * The type of the associated entity
     */
    private StringWithCustomFacts associatedEntityType;

    /**
     * The XREF to the associated entity
     */
    private String associatedEntityXref;

    /**
     * The citations for this object
     */
    private List<AbstractCitation> citations = getCitations(Options.isCollectionInitializationEnabled());

    /**
     * Relationship description
     */
    private StringWithCustomFacts relationship;

    /** Default constructor */
    public Association() {
        // Default constructor does nothing
    }

    /**
     * Copy constructor
     * 
     * @param other
     *            object being copied
     */
    public Association(Association other) {
        super(other);
        if (other.associatedEntityType != null) {
            associatedEntityType = new StringWithCustomFacts(other.associatedEntityType);
        }
        associatedEntityXref = other.associatedEntityXref;
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
        if (other.relationship != null) {
            relationship = new StringWithCustomFacts(other.relationship);
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
        if (!(obj instanceof Association)) {
            return false;
        }
        Association other = (Association) obj;
        if (associatedEntityType == null) {
            if (other.associatedEntityType != null) {
                return false;
            }
        } else if (!associatedEntityType.equals(other.associatedEntityType)) {
            return false;
        }
        if (associatedEntityXref == null) {
            if (other.associatedEntityXref != null) {
                return false;
            }
        } else if (!associatedEntityXref.equals(other.associatedEntityXref)) {
            return false;
        }
        if (citations == null) {
            if (other.citations != null) {
                return false;
            }
        } else if (!citations.equals(other.citations)) {
            return false;
        }
        if (relationship == null) {
            if (other.relationship != null) {
                return false;
            }
        } else if (!relationship.equals(other.relationship)) {
            return false;
        }
        return true;
    }

    /**
     * Gets the associated entity type.
     *
     * @return the associated entity type
     */
    public StringWithCustomFacts getAssociatedEntityType() {
        return associatedEntityType;
    }

    /**
     * Gets the associated entity xref.
     *
     * @return the associated entity xref
     */
    public String getAssociatedEntityXref() {
        return associatedEntityXref;
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
     * Gets the relationship.
     *
     * @return the relationship
     */
    public StringWithCustomFacts getRelationship() {
        return relationship;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (associatedEntityType == null ? 0 : associatedEntityType.hashCode());
        result = prime * result + (associatedEntityXref == null ? 0 : associatedEntityXref.hashCode());
        result = prime * result + (citations == null ? 0 : citations.hashCode());
        result = prime * result + (relationship == null ? 0 : relationship.hashCode());
        return result;
    }

    /**
     * Sets the associated entity type.
     *
     * @param associatedEntityType
     *            the new associated entity type
     */
    public void setAssociatedEntityType(String associatedEntityType) {
        this.associatedEntityType = associatedEntityType == null ? null : new StringWithCustomFacts(associatedEntityType);
    }

    /**
     * Sets the associated entity type.
     *
     * @param associatedEntityType
     *            the new associated entity type
     */
    public void setAssociatedEntityType(StringWithCustomFacts associatedEntityType) {
        this.associatedEntityType = associatedEntityType;
    }

    /**
     * Sets the associated entity xref.
     *
     * @param associatedEntityXref
     *            the new associated entity xref
     */
    public void setAssociatedEntityXref(String associatedEntityXref) {
        this.associatedEntityXref = associatedEntityXref;
    }

    /**
     * Sets the relationship.
     *
     * @param relationship
     *            the new relationship
     */
    public void setRelationship(String relationship) {
        this.relationship = relationship == null ? null : new StringWithCustomFacts(relationship);
    }

    /**
     * Sets the relationship.
     *
     * @param relationship
     *            the new relationship
     */
    public void setRelationship(StringWithCustomFacts relationship) {
        this.relationship = relationship;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(32);
        builder.append("Association [");
        if (associatedEntityType != null) {
            builder.append("associatedEntityType=");
            builder.append(associatedEntityType);
            builder.append(", ");
        }
        if (associatedEntityXref != null) {
            builder.append("associatedEntityXref=");
            builder.append(associatedEntityXref);
            builder.append(", ");
        }
        if (citations != null) {
            builder.append("citations=");
            builder.append(citations);
            builder.append(", ");
        }
        if (relationship != null) {
            builder.append("relationship=");
            builder.append(relationship);
            builder.append(", ");
        }
        if (getCustomFacts() != null) {
            builder.append("customTags=");
            builder.append(getCustomFacts());
        }
        builder.append("]");
        return builder.toString();
    }
}
