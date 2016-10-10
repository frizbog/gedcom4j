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
 * A reference to an submission, which may have custom facts on the reference that are not custom facts about the submission
 * themselves.
 * 
 * @author frizbog
 */
public class SubmissionReference extends AbstractElement {

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = -1855269504871281907L;

    /**
     * The submission referred to
     */
    Submission submission;

    /**
     * Default constructor
     */
    public SubmissionReference() {
        // Default constructor does nothing
    }

    /**
     * Create a new SubmissionReference object
     * 
     * @param submission
     *            the submission being referred to
     */
    public SubmissionReference(Submission submission) {
        this.submission = submission;
    }

    /**
     * Copy constructor.
     * 
     * @param other
     *            the other SubmissionReference being copied
     */
    public SubmissionReference(SubmissionReference other) {
        super(other);
        submission = new Submission(other.submission);
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
        if (!(obj instanceof SubmissionReference)) {
            return false;
        }
        SubmissionReference other = (SubmissionReference) obj;
        if (submission == null) {
            if (other.submission != null) {
                return false;
            }
        } else if (!submission.equals(other.submission)) {
            return false;
        }
        return true;
    }

    /**
     * Get the submission
     * 
     * @return the submission
     */
    public Submission getSubmission() {
        return submission;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((submission == null) ? 0 : submission.hashCode());
        return result;
    }

    /**
     * Set the submission
     * 
     * @param submission
     *            the submission to set
     */
    public void setSubmission(Submission submission) {
        this.submission = submission;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(25);
        builder.append("SubmissionReference [");
        if (submission != null) {
            builder.append("submission=");
            builder.append(submission);
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
