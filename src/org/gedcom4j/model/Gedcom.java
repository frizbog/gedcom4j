/*
 * Copyright (c) 2009-2014 Matthew R. Harrah
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.gedcom4j.model;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Main (root) class for an entire GEDCOM file.
 * </p>
 * 
 * <p>
 * Note that if you are creating a Gedcom object graph programmatically from
 * scratch (as opposed to by parsing a GEDCOM file), you will (probably) want to
 * do the following things...some are required for the structure to pass
 * validation, and the results of autorepair (if enabled) may not be what you
 * want (see {@link org.gedcom4j.validate.GedcomValidator}).
 * <ol type="1">
 * <li>Define a {@link Submitter} and add it to the {@link Gedcom#submitters}
 * map. Autorepair will make a fake submitter record with a name of
 * "UNSPECIFIED" and add it to the map during validation if validation is turned
 * on, but this submitter record may not be what you want.</li>
 * <li>Specify which Submitter in the submitters map is the primary submitter
 * and set the {@link Header#submitter} reference to that instance. If no
 * primary submitter is specified in the header, auto-repair will select the
 * first value in the submitters map and use that.</li>
 * <li>Override default values for the Source System and its components in
 * {@link Header#sourceSystem}
 * <ol type="a">
 * <li>Specify, or override the default value of the
 * {@link SourceSystem#systemId} field to an application-specific value. If it
 * is missing or blank, autorepair during validation will set it to the default
 * value of "UNSPECIFIED" which is probably not desirable.</li>
 * <li>If specifying a corporation, specify, or override the default value of
 * the {@link Corporation#businessName} field to an application-specific value
 * (probably your company/org name). If it is missing or blank, autorepair
 * during validation will set it to the default value of "UNSPECIFIED" which is
 * probably not desirable.</li>
 * <li>If specifying the source data for the source system, specify, or override
 * the default value of the {@link HeaderSourceData#name} field to an
 * application-specific value. If it is missing or blank, autorepair during
 * validation will set it to the default value of "UNSPECIFIED" which is
 * probably not desirable.</li>
 * </ol>
 * </li>
 * </ol>
 * </p>
 * 
 * @author frizbog1
 */
public class Gedcom extends AbstractElement {
    /**
     * A map of all the families in the GEDCOM file. The map is keyed on family
     * cross-reference numbers, and the families themselves are in the value
     * set.
     */
    public Map<String, Family> families = new HashMap<String, Family>();

    /**
     * Header information about the GEDCOM
     */
    public Header header = new Header();

    /**
     * A map of all the individuals in the GEDCOM file. The map is keyed on the
     * individual cross-reference numbers and the individuals themselves are in
     * the value set.
     */
    public Map<String, Individual> individuals = new HashMap<String, Individual>();

    /**
     * A map of all the multimedia items in the GEDCOM file. The map is keyed by
     * the multimedia cross-reference numbers, and the multimedia items
     * themselves (well, the metadata about them for 5.5.1) are in the value
     * set. Remember, GEDCOM 5.5.1 multimedia is not embedded in the GEDCOM, but
     * the GEDCOM contains metadata about the multimedia.
     */
    public Map<String, Multimedia> multimedia = new HashMap<String, Multimedia>();

    /**
     * A map of notes. The map is keyed with cross-reference numbers and the
     * notes themselves are the values.
     */
    public Map<String, Note> notes = new HashMap<String, Note>();

    /**
     * A map of all the source repositories in the GEDCOM file. The map is keyed
     * on the repository cross-reference numbers, and the repositories
     * themsevles are in the value set.
     */
    public Map<String, Repository> repositories = new HashMap<String, Repository>();

    /**
     * A map of all the sources in the GEDCOM file. The map is keyed on source
     * cross-reference numbers, and the sources themsevles are in the value set.
     */
    public Map<String, Source> sources = new HashMap<String, Source>();

    /**
     * Information about the GEDCOM submission. There is only one and it is
     * required, so the xref ID has a default.
     */
    public Submission submission = new Submission("@SUBMISSION@");

    /**
     * A map of the submitters in the GEDCOM file. The map is keyed on submitter
     * cross-reference numbers, and the submitters themselves are in the value
     * set
     */
    public Map<String, Submitter> submitters = new HashMap<String, Submitter>();

    /**
     * The trailer of the file
     */
    public Trailer trailer = new Trailer();

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
        Gedcom other = (Gedcom) obj;
        if (families == null) {
            if (other.families != null) {
                return false;
            }
        } else if (!families.equals(other.families)) {
            return false;
        }
        if (header == null) {
            if (other.header != null) {
                return false;
            }
        } else if (!header.equals(other.header)) {
            return false;
        }
        if (individuals == null) {
            if (other.individuals != null) {
                return false;
            }
        } else if (!individuals.equals(other.individuals)) {
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
        if (repositories == null) {
            if (other.repositories != null) {
                return false;
            }
        } else if (!repositories.equals(other.repositories)) {
            return false;
        }
        if (sources == null) {
            if (other.sources != null) {
                return false;
            }
        } else if (!sources.equals(other.sources)) {
            return false;
        }
        if (submission == null) {
            if (other.submission != null) {
                return false;
            }
        } else if (!submission.equals(other.submission)) {
            return false;
        }
        if (submitters == null) {
            if (other.submitters != null) {
                return false;
            }
        } else if (!submitters.equals(other.submitters)) {
            return false;
        }
        if (trailer == null) {
            if (other.trailer != null) {
                return false;
            }
        } else if (!trailer.equals(other.trailer)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (families == null ? 0 : families.hashCode());
        result = prime * result + (header == null ? 0 : header.hashCode());
        result = prime * result + (individuals == null ? 0 : individuals.hashCode());
        result = prime * result + (multimedia == null ? 0 : multimedia.hashCode());
        result = prime * result + (notes == null ? 0 : notes.hashCode());
        result = prime * result + (repositories == null ? 0 : repositories.hashCode());
        result = prime * result + (sources == null ? 0 : sources.hashCode());
        result = prime * result + (submission == null ? 0 : submission.hashCode());
        result = prime * result + (submitters == null ? 0 : submitters.hashCode());
        result = prime * result + (trailer == null ? 0 : trailer.hashCode());
        return result;
    }
}
