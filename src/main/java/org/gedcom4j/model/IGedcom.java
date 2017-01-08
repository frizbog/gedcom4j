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
 * Represents an entire GEDCOM - some store of data of some kind. Could be an in-memory object graph, could be a database, could be
 * a folder full of serialized objects, whatever. The important part is that through this interface you can get to all the objects
 * (Individuals, Families, etc.) in the GEDCOM.
 */
public interface IGedcom extends HasCustomFacts {

    /**
     * Gets the families.
     *
     * @return the families
     */
    Map<String, Family> getFamilies();

    /**
     * Gets the header.
     *
     * @return the header
     */
    Header getHeader();

    /**
     * Gets the individuals.
     *
     * @return the individuals
     */
    Map<String, Individual> getIndividuals();

    /**
     * Gets the multimedia.
     *
     * @return the multimedia
     */
    Map<String, Multimedia> getMultimedia();

    /**
     * Gets the notes.
     *
     * @return the notes
     */
    Map<String, NoteRecord> getNotes();

    /**
     * Gets the repositories.
     *
     * @return the repositories
     */
    Map<String, Repository> getRepositories();

    /**
     * Gets the sources.
     *
     * @return the sources
     */
    Map<String, Source> getSources();

    /**
     * Gets the submission.
     *
     * @return the submission
     */
    Submission getSubmission();

    /**
     * Gets the submitters.
     *
     * @return the submitters
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