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
 * A reference to an multimedia, which may have custom facts on the reference that are not custom facts about the multimedia
 * themselves.
 * 
 * @author frizbog
 */
public class MultimediaReference extends AbstractElement {

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = -1855269504871281907L;

    /**
     * The multimedia referred to
     */
    Multimedia multimedia;

    /**
     * Default constructor
     */
    public MultimediaReference() {
        // Default constructor does nothing
    }

    /**
     * Create a new MultimediaReference object
     * 
     * @param multimedia
     *            the multimedia being referred to
     */
    public MultimediaReference(Multimedia multimedia) {
        this.multimedia = multimedia;
    }

    /**
     * Copy constructor.
     * 
     * @param other
     *            the other MultimediaReference being copied
     */
    public MultimediaReference(MultimediaReference other) {
        super(other);
        if (other.multimedia != null) {
            multimedia = new Multimedia(other.multimedia);
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
        if (!(obj instanceof MultimediaReference)) {
            return false;
        }
        MultimediaReference other = (MultimediaReference) obj;
        if (multimedia == null) {
            if (other.multimedia != null) {
                return false;
            }
        } else if (!multimedia.equals(other.multimedia)) {
            return false;
        }
        return true;
    }

    /**
     * Get the multimedia
     * 
     * @return the multimedia
     */
    public Multimedia getMultimedia() {
        return multimedia;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((multimedia == null) ? 0 : multimedia.hashCode());
        return result;
    }

    /**
     * Set the multimedia
     * 
     * @param multimedia
     *            the multimedia to set
     */
    public void setMultimedia(Multimedia multimedia) {
        this.multimedia = multimedia;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(25);
        builder.append("MultimediaReference [");
        if (multimedia != null) {
            builder.append("multimedia=");
            builder.append(multimedia);
            builder.append(", ");
        }
        if (customFacts != null) {
            builder.append("customFacts=");
            builder.append(customFacts);
        }
        builder.append("]");
        return builder.toString();
    }

}
