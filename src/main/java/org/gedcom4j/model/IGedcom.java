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
 * <p>
 * Represents an entire GEDCOM - some store of some kind for the data read from a GEDCOM. Could be an in-memory object graph (the
 * default), could be a database, could be a folder full of serialized objects, whatever. The important part is that through this
 * interface you can get to all the objects (Individuals, Families, etc.) in the GEDCOM.
 * </p>
 * 
 * <p>
 * Note that if you are creating a InMemoryGedcom object graph programmatically from scratch (as opposed to by parsing a GEDCOM file), you
 * will (probably) want to do the following things. Some are required for the structure to pass validation, and the results of
 * autorepair (if enabled) may not be what you want - see {@link org.gedcom4j.validate.Validator}.
 * </p>
 * <ol type="1">
 * <li>Define a {@link Submitter} and add it to the {@link #getSubmitters()} map. Autorepair will make a fake submitter record with
 * a name of "UNSPECIFIED" and add it to the map during validation if validation is turned on, but this submitter record may not be
 * what you want.</li>
 * <li>Specify which Submitter in the submitters map is the primary submitter and set the {@link Header#getSubmitterReference()} to
 * that instance. If no primary submitter is specified in the header, auto-repair will select the first value in the submitters map
 * and use that.</li>
 * <li>Override default values for the Source System and its components in {@link Header#sourceSystem}
 * <ol type="a">
 * <li>Specify, or override the default value of the {@link SourceSystem#systemId} field to an application-specific value. If it is
 * missing or blank, autorepair during validation will set it to the default value of "UNSPECIFIED" which is probably not
 * desirable.</li>
 * <li>If specifying a corporation, specify, or override the default value of the {@link Corporation#businessName} field to an
 * application-specific value (probably your company/org name). If it is missing or blank, autorepair during validation will set it
 * to the default value of "UNSPECIFIED" which is probably not desirable.</li>
 * <li>If specifying the source data for the source system, specify, or override the default value of the
 * {@link HeaderSourceData#name} field to an application-specific value. If it is missing or blank, autorepair during validation
 * will set it to the default value of "UNSPECIFIED" which is probably not desirable.</li>
 * </ol>
 * </li>
 * </ol>
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
     * Clear out the entire IGedcom - reset all state to that of a new object.
     */
    void reset();

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