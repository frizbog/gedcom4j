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
 * An abstract base class for other source citations (both with and without source)
 * 
 * @author frizbog1
 */
public abstract class AbstractCitation extends AbstractNotesElement {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 5842672179681957413L;
    /**
     * The quality of this citation. Supposed to be 0, 1, 2, or 3, but stored as a string since we're not doing math on it.
     */
    protected StringWithCustomFacts certainty;
    /**
     * Multimedia links for this source citation
     */
    protected List<MultimediaReference> multimedia = getMultimedia(Options.isCollectionInitializationEnabled());

    /** Default constructor */
    public AbstractCitation() {
        // Default constructor does nothing
    }

    /**
     * Copy constructor
     * 
     * @param other
     *            object being copied
     */
    public AbstractCitation(AbstractCitation other) {
        super(other);
    }

    /**
     * Gets the certainty.
     *
     * @return the certainty
     */
    public StringWithCustomFacts getCertainty() {
        return certainty;
    }

    /**
     * Sets the certainty.
     *
     * @param certainty
     *            the new certainty
     */
    public void setCertainty(String certainty) {
        this.certainty = certainty == null ? null : new StringWithCustomFacts(certainty);
    }

    /**
     * Sets the certainty.
     *
     * @param certainty
     *            the new certainty
     */
    public void setCertainty(StringWithCustomFacts certainty) {
        this.certainty = certainty;
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
}
