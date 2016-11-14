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
@SuppressWarnings("PMD.GodClass")
public class Source extends AbstractNotesElement implements HasXref {
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
    private List<MultimediaReference> multimedia = getMultimedia(Options.isCollectionInitializationEnabled());

    /**
     * The originators/authors
     */
    private MultiStringWithCustomFacts originatorsAuthors;

    /**
     * Publication facts on this source
     */
    private MultiStringWithCustomFacts publicationFacts;

    /**
     * The record ID number
     */
    private StringWithCustomFacts recIdNumber;

    /**
     * A repository Citation
     */
    private RepositoryCitation repositoryCitation;

    /**
     * Who filed the source
     */
    private StringWithCustomFacts sourceFiledBy;

    /**
     * Text from the source
     */
    private MultiStringWithCustomFacts sourceText;

    /**
     * The title text
     */
    private MultiStringWithCustomFacts title;

    /**
     * The user references for this submitter
     */
    private List<UserReference> userReferences = getUserReferences(Options.isCollectionInitializationEnabled());

    /**
     * The xref for this submitter
     */
    private String xref;

    /** Default constructor */
    public Source() {
        // Default constructor does nothing
    }

    /**
     * Copy constructor
     * 
     * @param other
     *            object being copied
     */
    public Source(Source other) {
        super(other);
        if (other.changeDate != null) {
            changeDate = new ChangeDate(other.changeDate);
        }
        if (other.data != null) {
            data = new SourceData(other.data);
        }
        if (other.multimedia != null) {
            multimedia = new ArrayList<>();
            for (MultimediaReference m : other.multimedia) {
                multimedia.add(new MultimediaReference(m));
            }
        }
        if (other.originatorsAuthors != null) {
            originatorsAuthors = new MultiStringWithCustomFacts(other.originatorsAuthors);
        }
        if (other.publicationFacts != null) {
            publicationFacts = new MultiStringWithCustomFacts(other.publicationFacts);
        }

        if (other.recIdNumber != null) {
            recIdNumber = new StringWithCustomFacts(other.recIdNumber);
        }
        if (other.repositoryCitation != null) {
            repositoryCitation = new RepositoryCitation(other.repositoryCitation);
        }
        if (other.sourceFiledBy != null) {
            sourceFiledBy = new StringWithCustomFacts(other.sourceFiledBy);
        }
        if (other.sourceText != null) {
            sourceText = new MultiStringWithCustomFacts(other.sourceText);
        }

        if (other.title != null) {
            title = new MultiStringWithCustomFacts(other.title);
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
    public List<MultimediaReference> getMultimedia() {
        return multimedia;
    }

    /**
     * Get the multimedia
     * 
     * @param initializeIfNeeded
     *            true if this collection should be created on-the-fly if it is currently null
     * @return the multimedia
     */
    public List<MultimediaReference> getMultimedia(boolean initializeIfNeeded) {
        if (initializeIfNeeded && multimedia == null) {
            multimedia = new ArrayList<>(0);
        }
        return multimedia;
    }

    /**
     * Gets the originators authors.
     *
     * @return the originators authors
     */
    public MultiStringWithCustomFacts getOriginatorsAuthors() {
        return originatorsAuthors;
    }

    /**
     * Gets the publication facts.
     *
     * @return the publication facts
     */
    public MultiStringWithCustomFacts getPublicationFacts() {
        return publicationFacts;
    }

    /**
     * Gets the rec id number.
     *
     * @return the rec id number
     */
    public StringWithCustomFacts getRecIdNumber() {
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
    public StringWithCustomFacts getSourceFiledBy() {
        return sourceFiledBy;
    }

    /**
     * Gets the source text.
     *
     * @return the source text
     */
    public MultiStringWithCustomFacts getSourceText() {
        return sourceText;
    }

    /**
     * Gets the title.
     *
     * @return the title
     */
    public MultiStringWithCustomFacts getTitle() {
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
        result = prime * result + (changeDate == null ? 0 : changeDate.hashCode());
        result = prime * result + (data == null ? 0 : data.hashCode());
        result = prime * result + (multimedia == null ? 0 : multimedia.hashCode());
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
     * Set the originatorsAuthors
     * 
     * @param originatorsAuthors
     *            the originatorsAuthors to set
     */
    public void setOriginatorsAuthors(MultiStringWithCustomFacts originatorsAuthors) {
        this.originatorsAuthors = originatorsAuthors;
    }

    /**
     * Set the publicationFacts
     * 
     * @param publicationFacts
     *            the publicationFacts to set
     */
    public void setPublicationFacts(MultiStringWithCustomFacts publicationFacts) {
        this.publicationFacts = publicationFacts;
    }

    /**
     * Sets the rec id number.
     *
     * @param recIdNumber
     *            the new rec id number
     */
    public void setRecIdNumber(String recIdNumber) {
        this.recIdNumber = recIdNumber == null ? null : new StringWithCustomFacts(recIdNumber);
    }

    /**
     * Sets the rec id number.
     *
     * @param recIdNumber
     *            the new rec id number
     */
    public void setRecIdNumber(StringWithCustomFacts recIdNumber) {
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
    public void setSourceFiledBy(String sourceFiledBy) {
        this.sourceFiledBy = sourceFiledBy == null ? null : new StringWithCustomFacts(sourceFiledBy);
    }

    /**
     * Sets the source filed by.
     *
     * @param sourceFiledBy
     *            the new source filed by
     */
    public void setSourceFiledBy(StringWithCustomFacts sourceFiledBy) {
        this.sourceFiledBy = sourceFiledBy;
    }

    /**
     * Set the sourceText
     * 
     * @param sourceText
     *            the sourceText to set
     */
    public void setSourceText(MultiStringWithCustomFacts sourceText) {
        this.sourceText = sourceText;
    }

    /**
     * Set the title
     * 
     * @param title
     *            the title to set
     */
    public void setTitle(MultiStringWithCustomFacts title) {
        this.title = title;
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
        if (getNoteStructures() != null) {
            builder.append("noteStructures=");
            builder.append(getNoteStructures());
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
        if (getCustomFacts() != null) {
            builder.append("customFacts=");
            builder.append(getCustomFacts());
        }
        builder.append("]");
        return builder.toString();
    }
}
