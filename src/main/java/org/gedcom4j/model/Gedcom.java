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

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * <p>
 * Main (root) class for an entire GEDCOM file.
 * </p>
 * 
 * <p>
 * Note that if you are creating a Gedcom object graph programmatically from scratch (as opposed to by parsing a GEDCOM file), you
 * will (probably) want to do the following things. (Some are required for the structure to pass validation, and the results of
 * autorepair (if enabled) may not be what you want - see {@link org.gedcom4j.validate.Validator}).
 * </p>
 * <ol type="1">
 * <li>Define a {@link Submitter} and add it to the {@link Gedcom#submitters} map. Autorepair will make a fake submitter record with
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
 * @author frizbog1
 */
@SuppressWarnings("PMD.GodClass")
public class Gedcom extends AbstractElement {
    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -2972879346299316334L;

    /**
     * A helper class to limit the number of items shown from a collection
     * 
     * @param collection
     *            the collection
     * @param maxLen
     *            the max number of items to show from the collection
     * @return a String representation of the first <var>maxLen</var> items in the collection
     */
    protected static String toStringLimitCollection(@SuppressWarnings("rawtypes") Collection collection, int maxLen) {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        int i = 0;
        for (@SuppressWarnings("rawtypes")
        Iterator iterator = collection.iterator(); iterator.hasNext() && i < maxLen; i++) {
            if (i > 0) {
                builder.append(", ");
            }
            builder.append(iterator.next());
        }
        builder.append("]");
        return builder.toString();
    }

    /**
     * A map of all the families in the GEDCOM file. The map is keyed on family cross-reference numbers, and the families themselves
     * are in the value set.
     */
    private final Map<String, Family> families = new HashMap<>();

    /**
     * Header information about the GEDCOM
     */
    private Header header = new Header();

    /**
     * A map of all the individuals in the GEDCOM file. The map is keyed on the individual cross-reference numbers and the
     * individuals themselves are in the value set.
     */
    private final Map<String, Individual> individuals = new HashMap<>(0);

    /**
     * A map of all the multimedia items in the GEDCOM file. The map is keyed by the multimedia cross-reference numbers, and the
     * multimedia items themselves (well, the metadata about them for 5.5.1) are in the value set. Remember, GEDCOM 5.5.1 multimedia
     * is not embedded in the GEDCOM, but the GEDCOM contains metadata about the multimedia.
     */
    private final Map<String, Multimedia> multimedia = new HashMap<>(0);

    /**
     * A map of notes. The map is keyed with cross-reference numbers and the notes themselves are the values.
     */
    private final Map<String, NoteRecord> notes = new HashMap<>(0);

    /**
     * A map of all the source repositories in the GEDCOM file. The map is keyed on the repository cross-reference numbers, and the
     * repositories themselves are in the value set.
     */
    private final Map<String, Repository> repositories = new HashMap<>(0);

    /**
     * A map of all the sources in the GEDCOM file. The map is keyed on source cross-reference numbers, and the sources themselves
     * are in the value set.
     */
    private final Map<String, Source> sources = new HashMap<>(0);

    /**
     * Information about the GEDCOM submission. There is only one and it is required, so the xref ID has a default.
     */
    private Submission submission = new Submission("@SUBMISSION@");

    /**
     * A map of the submitters in the GEDCOM file. The map is keyed on submitter cross-reference numbers, and the submitters
     * themselves are in the value set
     */
    private final Map<String, Submitter> submitters = new HashMap<>(0);

    /**
     * The trailer of the file
     */
    private Trailer trailer = new Trailer();

    /** Default constructor */
    public Gedcom() {
        // Default constructor does nothing
    }

    /**
     * Copy constructor
     * 
     * @param other
     *            object being copied
     */
    public Gedcom(Gedcom other) {
        super(other);
        for (Family f : other.families.values()) {
            families.put(f.getXref(), new Family(f));
        }
        if (other.header != null) {
            header = new Header(other.header);
        }
        for (Individual i : other.individuals.values()) {
            individuals.put(i.getXref(), new Individual(i));
        }
        for (Multimedia m : other.multimedia.values()) {
            multimedia.put(m.getXref(), new Multimedia(m));
        }
        for (NoteRecord n : other.notes.values()) {
            notes.put(n.getXref(), new NoteRecord(n));
        }
        for (Repository r : other.repositories.values()) {
            repositories.put(r.getXref(), new Repository(r));
        }
        for (Source r : other.sources.values()) {
            sources.put(r.getXref(), new Source(r));
        }
        if (other.submission != null) {
            submission = new Submission(other.submission);
        }
        for (Submitter s : other.submitters.values()) {
            submitters.put(s.getXref(), new Submitter(s));
        }
        // All trailers are the same, and it's already initialized
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
        Gedcom other = (Gedcom) obj;
        if (header == null) {
            if (other.header != null) {
                return false;
            }
        } else if (!header.equals(other.header)) {
            return false;
        }
        if (!families.equals(other.families)) {
            return false;
        }
        if (!individuals.equals(other.individuals)) {
            return false;
        }
        if (!multimedia.equals(other.multimedia)) {
            return false;
        }
        if (!getNotes().equals(other.getNotes())) {
            return false;
        }
        if (!repositories.equals(other.repositories)) {
            return false;
        }
        if (!sources.equals(other.sources)) {
            return false;
        }
        if (!submission.equals(other.submission)) {
            return false;
        }
        if (!submitters.equals(other.submitters)) {
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

    /**
     * Gets the families.
     *
     * @return the families
     */
    public Map<String, Family> getFamilies() {
        return families;
    }

    /**
     * Gets the header.
     *
     * @return the header
     */
    public Header getHeader() {
        return header;
    }

    /**
     * Gets the individuals.
     *
     * @return the individuals
     */
    public Map<String, Individual> getIndividuals() {
        return individuals;
    }

    /**
     * Gets the multimedia.
     *
     * @return the multimedia
     */
    public Map<String, Multimedia> getMultimedia() {
        return multimedia;
    }

    /**
     * Gets the notes.
     *
     * @return the notes
     */
    public Map<String, NoteRecord> getNotes() {
        return notes;
    }

    /**
     * Gets the repositories.
     *
     * @return the repositories
     */
    public Map<String, Repository> getRepositories() {
        return repositories;
    }

    /**
     * Gets the sources.
     *
     * @return the sources
     */
    public Map<String, Source> getSources() {
        return sources;
    }

    /**
     * Gets the submission.
     *
     * @return the submission
     */
    public Submission getSubmission() {
        return submission;
    }

    /**
     * Gets the submitters.
     *
     * @return the submitters
     */
    public Map<String, Submitter> getSubmitters() {
        return submitters;
    }

    /**
     * Gets the trailer.
     *
     * @return the trailer
     */
    public Trailer getTrailer() {
        return trailer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (families == null ? 0 : families.hashCode());
        result = prime * result + (header == null ? 0 : header.hashCode());
        result = prime * result + (individuals == null ? 0 : individuals.hashCode());
        result = prime * result + (multimedia == null ? 0 : multimedia.hashCode());
        result = prime * result + (getNotes() == null ? 0 : getNotes().hashCode());
        result = prime * result + (repositories == null ? 0 : repositories.hashCode());
        result = prime * result + (sources == null ? 0 : sources.hashCode());
        result = prime * result + (submission == null ? 0 : submission.hashCode());
        result = prime * result + (submitters == null ? 0 : submitters.hashCode());
        result = prime * result + (trailer == null ? 0 : trailer.hashCode());
        return result;
    }

    /**
     * Sets the header.
     *
     * @param header
     *            the new header
     */
    public void setHeader(Header header) {
        this.header = header;
    }

    /**
     * Sets the submission.
     *
     * @param submission
     *            the new submission
     */
    public void setSubmission(Submission submission) {
        this.submission = submission;
    }

    /**
     * Sets the trailer.
     *
     * @param trailer
     *            the new trailer
     */
    public void setTrailer(Trailer trailer) {
        this.trailer = trailer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final int maxLen = 3;
        StringBuilder builder = new StringBuilder(128);
        builder.append("Gedcom [");
        if (header != null) {
            builder.append("header=");
            builder.append(header);
            builder.append(", ");
        }
        builder.append("families=");
        builder.append(toStringLimitCollection(families.entrySet(), maxLen));
        builder.append(", individuals=");
        builder.append(toStringLimitCollection(individuals.entrySet(), maxLen));
        builder.append(", multimedia=");
        builder.append(toStringLimitCollection(multimedia.entrySet(), maxLen));
        builder.append(", noteStructures=");
        builder.append(toStringLimitCollection(notes.entrySet(), maxLen));
        builder.append(", repositories=");
        builder.append(toStringLimitCollection(repositories.entrySet(), maxLen));
        builder.append(", sources=");
        builder.append(toStringLimitCollection(sources.entrySet(), maxLen));
        builder.append(", submission=");
        builder.append(submission);
        builder.append(", submitters=");
        builder.append(toStringLimitCollection(submitters.entrySet(), maxLen));
        builder.append(", ");
        if (trailer != null) {
            builder.append("trailer=");
            builder.append(trailer);
        }
        builder.append("]");
        return builder.toString();
    }
}
