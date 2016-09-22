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
 * <p>
 * Information about the header source data.
 * </p>
 * <p>
 * If instantiating one of these programmatically rather than through parsing an existing GEDCOM file, you will probably want to
 * change the value of the {@link HeaderSourceData#name} field.
 * </p>
 * 
 * @author frizbog1
 * 
 */
public class HeaderSourceData extends AbstractElement {
    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 7952401381182039454L;

    /**
     * Copyright information
     */
    private StringWithCustomFacts copyright;

    /**
     * The name of the source data. This field must be valued to pass validation, so the default value is "UNSPECIFIED".
     */
    private String name = "UNSPECIFIED";

    /**
     * The publish date
     */
    private StringWithCustomFacts publishDate;

    /** Default constructor */
    public HeaderSourceData() {
        // Default constructor does nothing
    }

    /**
     * Copy constructor
     * 
     * @param other
     *            object being copied
     */
    public HeaderSourceData(HeaderSourceData other) {
        super(other);
        if (other.copyright != null) {
            copyright = new StringWithCustomFacts(other.copyright);
        }
        name = other.name;
        if (other.publishDate != null) {
            publishDate = new StringWithCustomFacts(other.publishDate);
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
        HeaderSourceData other = (HeaderSourceData) obj;
        if (copyright == null) {
            if (other.copyright != null) {
                return false;
            }
        } else if (!copyright.equals(other.copyright)) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (publishDate == null) {
            if (other.publishDate != null) {
                return false;
            }
        } else if (!publishDate.equals(other.publishDate)) {
            return false;
        }
        return true;
    }

    /**
     * Gets the copyright.
     *
     * @return the copyright
     */
    public StringWithCustomFacts getCopyright() {
        return copyright;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the publish date.
     *
     * @return the publish date
     */
    public StringWithCustomFacts getPublishDate() {
        return publishDate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (copyright == null ? 0 : copyright.hashCode());
        result = prime * result + (name == null ? 0 : name.hashCode());
        result = prime * result + (publishDate == null ? 0 : publishDate.hashCode());
        return result;
    }

    /**
     * Sets the copyright.
     *
     * @param copyright
     *            the new copyright
     */
    public void setCopyright(String copyright) {
        this.copyright = copyright == null ? null : new StringWithCustomFacts(copyright);
    }

    /**
     * Sets the copyright.
     *
     * @param copyright
     *            the new copyright
     */
    public void setCopyright(StringWithCustomFacts copyright) {
        this.copyright = copyright;
    }

    /**
     * Sets the name.
     *
     * @param name
     *            the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the publish date.
     *
     * @param publishDate
     *            the new publish date
     */
    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate == null ? null : new StringWithCustomFacts(publishDate);
    }

    /**
     * Sets the publish date.
     *
     * @param publishDate
     *            the new publish date
     */
    public void setPublishDate(StringWithCustomFacts publishDate) {
        this.publishDate = publishDate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(64);
        builder.append("HeaderSourceData [");
        if (copyright != null) {
            builder.append("copyright=");
            builder.append(copyright);
            builder.append(", ");
        }
        if (name != null) {
            builder.append("name=");
            builder.append(name);
            builder.append(", ");
        }
        if (publishDate != null) {
            builder.append("publishDate=");
            builder.append(publishDate);
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
