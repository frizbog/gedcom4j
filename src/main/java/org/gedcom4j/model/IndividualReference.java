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
 * A reference to an individual, which may have custom facts on the reference that are not custom facts about the individual
 * themselves.
 * 
 * @author frizbog
 */
public class IndividualReference extends AbstractElement {

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = -1855269504871281907L;

    /**
     * The individual referred to
     */
    Individual individual;

    /**
     * Default constructor
     */
    public IndividualReference() {
        // Default constructor does nothing
    }

    /**
     * Create a new IndividualReference object
     * 
     * @param individual
     *            the individual being referred to
     */
    public IndividualReference(Individual individual) {
        this.individual = individual;
    }

    /**
     * Copy constructor.
     * 
     * @param other
     *            the other IndividualReference being copied
     * @param deep
     *            pass in true if a full, deep copy of the {@link IndividualReference} should be created, including copies of the
     *            families the referenced individual is in. If false, the object is copied without references to the families the
     *            individual is in.
     */
    public IndividualReference(IndividualReference other, boolean deep) {
        super(other);
        individual = new Individual(other.individual, deep);
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
        if (!(obj instanceof IndividualReference)) {
            return false;
        }
        IndividualReference other = (IndividualReference) obj;
        if (individual == null) {
            if (other.individual != null) {
                return false;
            }
        } else if (!individual.equals(other.individual)) {
            return false;
        }
        return true;
    }

    /**
     * Get the individual
     * 
     * @return the individual
     */
    public Individual getIndividual() {
        return individual;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((individual == null) ? 0 : individual.hashCode());
        return result;
    }

    /**
     * Set the individual
     * 
     * @param individual
     *            the individual to set
     */
    public void setIndividual(Individual individual) {
        this.individual = individual;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(25);
        builder.append("IndividualReference [");
        if (individual != null) {
            builder.append("individual=");
            builder.append(individual);
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
