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
 * A source for citations.
 * 
 * @author frizbog1
 */
public class Source extends AbstractElement {
    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 5580720679037154352L;

    /**
     * The change date for this source
     */
    private ChangeDate changeDate;

    /**
     * Source data
     */
    private SourceData data;

    /**
     * Multimedia links for this source citation
     */
    private List<Multimedia> multimedia = getMultimedia(Options.isCollectionInitializationEnabled());

    /**
     * Notes about this object
     */
    private List<Note> notes = getNotes(Options.isCollectionInitializationEnabled());

    /**
     * The originators/authors
     */
    private List<String> originatorsAuthors = getOriginatorsAuthors(Options.isCollectionInitializationEnabled());

    /**
     * Publication facts on this source
     */
    private List<String> publicationFacts = getPublicationFacts(Options.isCollectionInitializationEnabled());

    /**
     * The record ID number
     */
    private StringWithCustomTags recIdNumber;

    /**
     * A repository Citation
     */
    private RepositoryCitation repositoryCitation;

    /**
     * Who filed the source
     */
    private StringWithCustomTags sourceFiledBy;

    /**
     * Text from the source
     */
    private List<String> sourceText = getSourceText(Options.isCollectionInitializationEnabled());

    /**
     * The title text
     */
    private List<String> title = getTitle(Options.isCollectionInitializationEnabled());

    /**
     * The user references for this submitter
     */
    private List<UserReference> userReferences = getUserReferences(Options.isCollectionInitializationEnabled());

    /**
     * The xref for this submitter
     */
    private String xref;

    /**
     * Constructor, takes required xref value
     * 
     * @param xref
     *            the xref
     */
    public Source(String xref) {
        if (xref != null) {
            this.xref = xref;
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("PMD.ExcessiveMethodLength")
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof Source)) {
            return false;
        }
        Source other = (Source) obj;
        if (changeDate == null) {
            if (other.changeDate != null) {
                return false;
            }
        } else if (!changeDate.equals(other.changeDate)) {
            return false;
        }
        if (data == null) {
            if (other.data != null) {
                return false;
            }
        } else if (!data.equals(other.data)) {
            return false;
        }
        if (multimedia == null) {
            if (other.multimedia != null) {
                return false;
            }
        } else if (!multimedia.equals(other.multimedia)) {
            return false;
        }
        if (notes == null) {
            if (other.notes != null) {
                return false;
            }
        } else if (!notes.equals(other.notes)) {
            return false;
        }
        if (originatorsAuthors == null) {
            if (other.originatorsAuthors != null) {
                return false;
            }
        } else if (!originatorsAuthors.equals(other.originatorsAuthors)) {
            return false;
        }
        if (publicationFacts == null) {
            if (other.publicationFacts != null) {
                return false;
            }
        } else if (!publicationFacts.equals(other.publicationFacts)) {
            return false;
        }
        if (recIdNumber == null) {
            if (other.recIdNumber != null) {
                return false;
            }
        } else if (!recIdNumber.equals(other.recIdNumber)) {
            return false;
        }
        if (repositoryCitation == null) {
            if (other.repositoryCitation != null) {
                return false;
            }
        } else if (!repositoryCitation.equals(other.repositoryCitation)) {
            return false;
        }
        if (sourceFiledBy == null) {
            if (other.sourceFiledBy != null) {
                return false;
            }
        } else if (!sourceFiledBy.equals(other.sourceFiledBy)) {
            return false;
        }
        if (sourceText == null) {
            if (other.sourceText != null) {
                return false;
            }
        } else if (!sourceText.equals(other.sourceText)) {
            return false;
        }
        if (title == null) {
            if (other.title != null) {
                return false;
            }
        } else if (!title.equals(other.title)) {
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
     * Gets the change date.
     *
     * @return the change date
     */
    public ChangeDate getChangeDate() {
        return changeDate;
    }

    /**
     * Gets the data.
     *
     * @return the data
     */
    public SourceData getData() {
        return data;
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
            multimedia = new ArrayList<Multimedia>(0);
        }
        return multimedia;
    }

    /**
     * Gets the notes.
     *
     * @return the notes
     */
    public List<Note> getNotes() {
        return notes;
    }

    /**
     * Get the notes
     * 
     * @param initializeIfNeeded
     *            initialize the collection if needed?
     * 
     * @return the notes
     */
    public List<Note> getNotes(boolean initializeIfNeeded) {
        if (initializeIfNeeded && notes == null) {
            notes = new ArrayList<Note>(0);
        }
        return notes;
    }

    /**
     * Gets the originators authors.
     *
     * @return the originators authors
     */
    public List<String> getOriginatorsAuthors() {
        return originatorsAuthors;
    }

    /**
     * Get the originators authors
     * 
     * @param initializeIfNeeded
     *            initialize the collection, if needed?
     * @return the originators authors
     */
    public List<String> getOriginatorsAuthors(boolean initializeIfNeeded) {
        if (initializeIfNeeded && originatorsAuthors == null) {
            originatorsAuthors = new ArrayList<String>(0);
        }
        return originatorsAuthors;
    }

    /**
     * Gets the publication facts.
     *
     * @return the publication facts
     */
    public List<String> getPublicationFacts() {
        return publicationFacts;
    }

    /**
     * Get the publication facts
     * 
     * @param initializeIfNeeded
     *            initialize the collection, if needed?
     * @return the publication facts
     */
    public List<String> getPublicationFacts(boolean initializeIfNeeded) {
        if (initializeIfNeeded && publicationFacts == null) {
            publicationFacts = new ArrayList<String>(0);
        }
        return publicationFacts;
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
     * Gets the repository citation.
     *
     * @return the repository citation
     */
    public RepositoryCitation getRepositoryCitation() {
        return repositoryCitation;
    }

    /**
     * Gets the source filed by.
     *
     * @return the source filed by
     */
    public StringWithCustomTags getSourceFiledBy() {
        return sourceFiledBy;
    }

    /**
     * Gets the source text.
     *
     * @return the source text
     */
    public List<String> getSourceText() {
        return sourceText;
    }

    /**
     * Get the source text
     * 
     * @param initializeIfNeeded
     *            initialize the collection, if needed?
     * @return the source text
     */
    public List<String> getSourceText(boolean initializeIfNeeded) {
        if (initializeIfNeeded && sourceText == null) {
            sourceText = new ArrayList<String>(0);
        }
        return sourceText;
    }

    /**
     * Gets the title.
     *
     * @return the title
     */
    public List<String> getTitle() {
        return title;
    }

    /**
     * Get the title
     * 
     * @param initializeIfNeeded
     *            initialize the collection, if needed?
     * @return the title
     */
    public List<String> getTitle(boolean initializeIfNeeded) {
        if (initializeIfNeeded && title == null) {
            title = new ArrayList<String>(0);
        }
        return title;
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
            userReferences = new ArrayList<UserReference>(0);
        }
        return userReferences;
    }

    /**
     * Gets the xref.
     *
     * @return the xref
     */
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
        result = prime * result + (changeDate == null ? 0 : changeDate.hashCode());
        result = prime * result + (data == null ? 0 : data.hashCode());
        result = prime * result + (multimedia == null ? 0 : multimedia.hashCode());
        result = prime * result + (notes == null ? 0 : notes.hashCode());
        result = prime * result + (originatorsAuthors == null ? 0 : originatorsAuthors.hashCode());
        result = prime * result + (publicationFacts == null ? 0 : publicationFacts.hashCode());
        result = prime * result + (recIdNumber == null ? 0 : recIdNumber.hashCode());
        result = prime * result + (repositoryCitation == null ? 0 : repositoryCitation.hashCode());
        result = prime * result + (sourceFiledBy == null ? 0 : sourceFiledBy.hashCode());
        result = prime * result + (sourceText == null ? 0 : sourceText.hashCode());
        result = prime * result + (title == null ? 0 : title.hashCode());
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
     * Sets the data.
     *
     * @param data
     *            the new data
     */
    public void setData(SourceData data) {
        this.data = data;
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
     * Sets the repository citation.
     *
     * @param repositoryCitation
     *            the new repository citation
     */
    public void setRepositoryCitation(RepositoryCitation repositoryCitation) {
        this.repositoryCitation = repositoryCitation;
    }

    /**
     * Sets the source filed by.
     *
     * @param sourceFiledBy
     *            the new source filed by
     */
    public void setSourceFiledBy(StringWithCustomTags sourceFiledBy) {
        this.sourceFiledBy = sourceFiledBy;
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
        StringBuilder builder = new StringBuilder();
        builder.append("Source [");
        if (changeDate != null) {
            builder.append("changeDate=");
            builder.append(changeDate);
            builder.append(", ");
        }
        if (data != null) {
            builder.append("data=");
            builder.append(data);
            builder.append(", ");
        }
        if (multimedia != null) {
            builder.append("multimedia=");
            builder.append(multimedia);
            builder.append(", ");
        }
        if (notes != null) {
            builder.append("notes=");
            builder.append(notes);
            builder.append(", ");
        }
        if (originatorsAuthors != null) {
            builder.append("originatorsAuthors=");
            builder.append(originatorsAuthors);
            builder.append(", ");
        }
        if (publicationFacts != null) {
            builder.append("publicationFacts=");
            builder.append(publicationFacts);
            builder.append(", ");
        }
        if (recIdNumber != null) {
            builder.append("recIdNumber=");
            builder.append(recIdNumber);
            builder.append(", ");
        }
        if (repositoryCitation != null) {
            builder.append("repositoryCitation=");
            builder.append(repositoryCitation);
            builder.append(", ");
        }
        if (sourceFiledBy != null) {
            builder.append("sourceFiledBy=");
            builder.append(sourceFiledBy);
            builder.append(", ");
        }
        if (sourceText != null) {
            builder.append("sourceText=");
            builder.append(sourceText);
            builder.append(", ");
        }
        if (title != null) {
            builder.append("title=");
            builder.append(title);
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
        if (customTags != null) {
            builder.append("customTags=");
            builder.append(customTags);
        }
        builder.append("]");
        return builder.toString();
    }
}
