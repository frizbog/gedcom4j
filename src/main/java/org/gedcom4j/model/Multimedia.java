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
 * <p>
 * A class for representing multimedia items. Corresponds to MULTIMEDIA_RECORD in the GEDCOM standard.
 * </p>
 * <p>
 * Please note that when gedcom4j v1.1.0 was released to include GEDCOM 5.5.1 support, the data model got a bit more complicated.
 * The changes to the multimedia specs were among the most drastic and most difficult to deal with. Not only did version 5.5 do away
 * with embedded multimedia support (i.e., the `BLOB` tag), it also changed cardinalities (multiple file references per
 * MULTIMEDIA_RECORD in 5.5.1, where 5.5 only allowed one), and moved tags to become children of other tags (i.e., the `FORM` tag is
 * now a child of the new `FILE` tag in 5.5.1).
 * </p>
 * <p>
 * Users who plan to read files produced by other systems and rewrite them with gedcom4j should pay special attention to the
 * multimedia section and ensure that the data in the model is compliant with the version of GEDCOM being used, and making
 * adjustments as needed.
 * </p>
 * 
 * @author frizbog1
 * 
 */
public class Multimedia extends AbstractNotesElement implements HasCitations, HasXref {
    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 9046705221369603960L;

    /**
     * The binary (blob) for this multimedia item. Encoded as string data. This field should always be an empty list for 5.5.1
     * files.
     */
    private List<String> blob = getBlob(Options.isCollectionInitializationEnabled());

    /**
     * The change date for this multimedia item
     */
    private ChangeDate changeDate;

    /**
     * The citations for this object
     */
    private List<AbstractCitation> citations = getCitations(Options.isCollectionInitializationEnabled());

    /**
     * The next object in the chain holding binary data if it needs to be continued due to size. This field should always be null
     * for 5.5.1 files.
     */
    private Multimedia continuedObject;

    /**
     * The format of the multimedia object - only for 5.5 style multimedia files, and should be null for 5.5.1 files.
     */
    private StringWithCustomTags embeddedMediaFormat;

    /**
     * The title of this multimedia item. This field should ONLY be used when the spec is 5.5 and should be null for 5.5.1 files.
     */
    private StringWithCustomTags embeddedTitle;

    /**
     * The file reference for this multimedia item
     */
    private List<FileReference> fileReferences = getFileReferences(Options.isCollectionInitializationEnabled());

    /**
     * The record ID number
     */
    private StringWithCustomTags recIdNumber;

    /**
     * The user references for this submitter
     */
    private List<UserReference> userReferences = getUserReferences(Options.isCollectionInitializationEnabled());

    /**
     * The xref for this submitter
     */
    private String xref;

    /** Default constructor */
    public Multimedia() {
        // Default constructor does nothing
    }

    /**
     * Copy constructor
     * 
     * @param other
     *            object being copied
     */
    public Multimedia(Multimedia other) {
        super(other);
        if (other.blob != null) {
            blob = new ArrayList<>(other.blob);
        }
        if (other.changeDate != null) {
            changeDate = new ChangeDate(other.changeDate);
        }
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
        if (other.continuedObject != null) {
            continuedObject = new Multimedia(other.continuedObject);
        }
        if (other.embeddedMediaFormat != null) {
            embeddedMediaFormat = new StringWithCustomTags(other.embeddedMediaFormat);
        }
        if (other.embeddedTitle != null) {
            embeddedTitle = new StringWithCustomTags(other.embeddedTitle);
        }
        if (other.fileReferences != null) {
            fileReferences = new ArrayList<>();
            for (FileReference fr : other.fileReferences) {
                fileReferences.add(new FileReference(fr));
            }
        }
        if (other.recIdNumber != null) {
            recIdNumber = new StringWithCustomTags(other.recIdNumber);
        }
        if (other.userReferences != null) {
            userReferences = new ArrayList<>();
            for (UserReference ur : other.userReferences) {
                userReferences.add(new UserReference(ur));
            }
        }
        xref = other.xref;
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
        Multimedia other = (Multimedia) obj;
        if (blob == null) {
            if (other.blob != null) {
                return false;
            }
        } else if (!blob.equals(other.blob)) {
            return false;
        }
        if (embeddedMediaFormat == null) {
            if (other.embeddedMediaFormat != null) {
                return false;
            }
        } else if (!embeddedMediaFormat.equals(other.embeddedMediaFormat)) {
            return false;
        }
        if (changeDate == null) {
            if (other.changeDate != null) {
                return false;
            }
        } else if (!changeDate.equals(other.changeDate)) {
            return false;
        }
        if (citations == null) {
            if (other.citations != null) {
                return false;
            }
        } else if (!citations.equals(other.citations)) {
            return false;
        }
        if (continuedObject == null) {
            if (other.continuedObject != null) {
                return false;
            }
        } else if (!continuedObject.equals(other.continuedObject)) {
            return false;
        }
        if (fileReferences == null) {
            if (other.fileReferences != null) {
                return false;
            }
        } else if (!fileReferences.equals(other.fileReferences)) {
            return false;
        }
        if (recIdNumber == null) {
            if (other.recIdNumber != null) {
                return false;
            }
        } else if (!recIdNumber.equals(other.recIdNumber)) {
            return false;
        }
        if (embeddedTitle == null) {
            if (other.embeddedTitle != null) {
                return false;
            }
        } else if (!embeddedTitle.equals(other.embeddedTitle)) {
            return false;
        }
        if (userReferences == null) {
            if (other.userReferences != null) {
                return false;
            }
        } else if (!userReferences.equals(other.userReferences)) {
            return false;
        }
        if (xref == null) {
            if (other.xref != null) {
                return false;
            }
        } else if (!xref.equals(other.xref)) {
            return false;
        }
        return true;
    }

    /**
     * Gets the blob.
     *
     * @return the blob
     */
    public List<String> getBlob() {
        return blob;
    }

    /**
     * Get the blob
     * 
     * @param initializeIfNeeded
     *            initialize the collection if needed
     * @return the blob
     */
    public List<String> getBlob(boolean initializeIfNeeded) {
        if (initializeIfNeeded && blob == null) {
            blob = new ArrayList<>(0);
        }
        return blob;
    }

    /**
     * Gets the change date.
     *
     * @return the change date
     */
    public ChangeDate getChangeDate() {
        return changeDate;
    }

    /**
     * Gets the citations.
     *
     * @return the citations
     */
    @Override
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
    @Override
    public List<AbstractCitation> getCitations(boolean initializeIfNeeded) {
        if (initializeIfNeeded && citations == null) {
            citations = new ArrayList<>(0);
        }
        return citations;
    }

    /**
     * Gets the continued object.
     *
     * @return the continued object
     */
    public Multimedia getContinuedObject() {
        return continuedObject;
    }

    /**
     * Gets the embedded media format.
     *
     * @return the embedded media format
     */
    public StringWithCustomTags getEmbeddedMediaFormat() {
        return embeddedMediaFormat;
    }

    /**
     * Gets the embedded title.
     *
     * @return the embedded title
     */
    public StringWithCustomTags getEmbeddedTitle() {
        return embeddedTitle;
    }

    /**
     * Gets the file references.
     *
     * @return the file references
     */
    public List<FileReference> getFileReferences() {
        return fileReferences;
    }

    /**
     * Get the file references
     * 
     * @param initializeIfNeeded
     *            initialize the collection if needed
     * @return the file references
     */
    public List<FileReference> getFileReferences(boolean initializeIfNeeded) {
        if (initializeIfNeeded && fileReferences == null) {
            fileReferences = new ArrayList<>(0);
        }
        return fileReferences;
    }

    /**
     * Gets the rec id number.
     *
     * @return the rec id number
     */
    public StringWithCustomTags getRecIdNumber() {
        return recIdNumber;
    }

    /**
     * Gets the user references.
     *
     * @return the user references
     */
    public List<UserReference> getUserReferences() {
        return userReferences;
    }

    /**
     * Get the user references
     * 
     * @param initializeIfNeeded
     *            initialize the collection, if needed?
     * @return the user references
     */
    public List<UserReference> getUserReferences(boolean initializeIfNeeded) {
        if (initializeIfNeeded && userReferences == null) {
            userReferences = new ArrayList<>(0);
        }
        return userReferences;
    }

    /**
     * Gets the xref.
     *
     * @return the xref
     */
    @Override
    public String getXref() {
        return xref;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (blob == null ? 0 : blob.hashCode());
        result = prime * result + (embeddedMediaFormat == null ? 0 : embeddedMediaFormat.hashCode());
        result = prime * result + (changeDate == null ? 0 : changeDate.hashCode());
        result = prime * result + (citations == null ? 0 : citations.hashCode());
        result = prime * result + (continuedObject == null ? 0 : continuedObject.hashCode());
        result = prime * result + (fileReferences == null ? 0 : fileReferences.hashCode());
        result = prime * result + (recIdNumber == null ? 0 : recIdNumber.hashCode());
        result = prime * result + (embeddedTitle == null ? 0 : embeddedTitle.hashCode());
        result = prime * result + (userReferences == null ? 0 : userReferences.hashCode());
        result = prime * result + (xref == null ? 0 : xref.hashCode());
        return result;
    }

    /**
     * Sets the change date.
     *
     * @param changeDate
     *            the new change date
     */
    public void setChangeDate(ChangeDate changeDate) {
        this.changeDate = changeDate;
    }

    /**
     * Sets the continued object.
     *
     * @param continuedObject
     *            the new continued object
     */
    public void setContinuedObject(Multimedia continuedObject) {
        this.continuedObject = continuedObject;
    }

    /**
     * Sets the embedded media format.
     *
     * @param embeddedMediaFormat
     *            the new embedded media format
     */
    public void setEmbeddedMediaFormat(String embeddedMediaFormat) {
        this.embeddedMediaFormat = embeddedMediaFormat == null ? null : new StringWithCustomTags(embeddedMediaFormat);
    }

    /**
     * Sets the embedded media format.
     *
     * @param embeddedMediaFormat
     *            the new embedded media format
     */
    public void setEmbeddedMediaFormat(StringWithCustomTags embeddedMediaFormat) {
        this.embeddedMediaFormat = embeddedMediaFormat;
    }

    /**
     * Sets the embedded title.
     *
     * @param embeddedTitle
     *            the new embedded title
     */
    public void setEmbeddedTitle(String embeddedTitle) {
        this.embeddedTitle = embeddedTitle == null ? null : new StringWithCustomTags(embeddedTitle);
    }

    /**
     * Sets the embedded title.
     *
     * @param embeddedTitle
     *            the new embedded title
     */
    public void setEmbeddedTitle(StringWithCustomTags embeddedTitle) {
        this.embeddedTitle = embeddedTitle;
    }

    /**
     * Sets the rec id number.
     *
     * @param recIdNumber
     *            the new rec id number
     */
    public void setRecIdNumber(String recIdNumber) {
        this.recIdNumber = recIdNumber == null ? null : new StringWithCustomTags(recIdNumber);
    }

    /**
     * Sets the rec id number.
     *
     * @param recIdNumber
     *            the new rec id number
     */
    public void setRecIdNumber(StringWithCustomTags recIdNumber) {
        this.recIdNumber = recIdNumber;
    }

    /**
     * Sets the xref.
     *
     * @param xref
     *            the new xref
     */
    public void setXref(String xref) {
        this.xref = xref;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(64);
        builder.append("Multimedia [");
        if (blob != null) {
            builder.append("blob=");
            builder.append(blob);
            builder.append(", ");
        }
        if (changeDate != null) {
            builder.append("changeDate=");
            builder.append(changeDate);
            builder.append(", ");
        }
        if (citations != null) {
            builder.append("citations=");
            builder.append(citations);
            builder.append(", ");
        }
        if (continuedObject != null) {
            builder.append("continuedObject=");
            builder.append(continuedObject);
            builder.append(", ");
        }
        if (embeddedMediaFormat != null) {
            builder.append("embeddedMediaFormat=");
            builder.append(embeddedMediaFormat);
            builder.append(", ");
        }
        if (embeddedTitle != null) {
            builder.append("embeddedTitle=");
            builder.append(embeddedTitle);
            builder.append(", ");
        }
        if (fileReferences != null) {
            builder.append("fileReferences=");
            builder.append(fileReferences);
            builder.append(", ");
        }
        if (getNotes() != null) {
            builder.append("notes=");
            builder.append(getNotes());
            builder.append(", ");
        }
        if (recIdNumber != null) {
            builder.append("recIdNumber=");
            builder.append(recIdNumber);
            builder.append(", ");
        }
        if (userReferences != null) {
            builder.append("userReferences=");
            builder.append(userReferences);
            builder.append(", ");
        }
        if (xref != null) {
            builder.append("xref=");
            builder.append(xref);
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
