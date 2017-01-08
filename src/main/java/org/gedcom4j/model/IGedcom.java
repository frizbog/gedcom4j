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

import java.util.Map;

/**
 * Represents an entire GEDCOM - some store of some kind for the data read from a GEDCOM. Could be an in-memory object graph (the
 * default), could be a database, could be a folder full of serialized objects, whatever. The important part is that through this
 * interface you can get to all the objects (Individuals, Families, etc.) in the GEDCOM.
 * 
 * @since v5.0.0
 */
public interface IGedcom extends HasCustomFacts {

    /**
     * Gets the families map.
     *
     * @return the families map
     */
    Map<String, Family> getFamilies();

    /**
     * Gets the header.
     *
     * @return the header
     */
    Header getHeader();

    /**
     * Gets the individuals map.
     *
     * @return the individuals map.
     */
    Map<String, Individual> getIndividuals();

    /**
     * Gets the multimedia map.
     *
     * @return the multimedia map
     */
    Map<String, Multimedia> getMultimedia();

    /**
     * Gets the notes map.
     *
     * @return the notes map
     */
    Map<String, NoteRecord> getNotes();

    /**
     * Gets the repositories map.
     *
     * @return the repositories map
     */
    Map<String, Repository> getRepositories();

    /**
     * Gets the sources map.
     *
     * @return the sources map
     */
    Map<String, Source> getSources();

    /**
     * Gets the submission.
     *
     * @return the submission
     */
    Submission getSubmission();

    /**
     * Gets the submitters map.
     *
     * @return the submitters map
     */
    Map<String, Submitter> getSubmitters();

    /**
     * Gets the trailer.
     *
     * @return the trailer
     */
    Trailer getTrailer();

    /**
     * Sets the header.
     *
     * @param header
     *            the new header
     */
    void setHeader(Header header);

    /**
     * Sets the submission.
     *
     * @param submission
     *            the new submission
     */
    void setSubmission(Submission submission);

    /**
     * Sets the trailer.
     *
     * @param trailer
     *            the new trailer
     */
    void setTrailer(Trailer trailer);

}