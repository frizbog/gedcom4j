/*
 * Copyright (c) 2009-2012 Matthew R. Harrah
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
package com.mattharrah.gedcom4j.relationship;

import java.util.ArrayList;
import java.util.List;

import com.mattharrah.gedcom4j.model.Individual;

/**
 * A class which represents the complex relationship between two individuals
 * 
 * @author frizbog1
 */
public class Relationship implements Comparable<Relationship> {

    /**
     * The chain of relationships from person 1 to person 2
     */
    public List<SimpleRelationship> chain = new ArrayList<SimpleRelationship>();

    /**
     * Person 1
     */
    public Individual individual1;

    /**
     * Person 2
     */
    public Individual individual2;

    /**
     * Default constructor
     */
    public Relationship() {
        super();
    }

    /**
     * All-arg constructor
     * 
     * @param startingIndividual
     *            the starting individual
     * @param targetIndividual
     *            the ending individual
     * @param chain
     *            the chain of {@link SimpleRelationship}s that get you from
     *            person 1 to person 2
     */
    public Relationship(Individual startingIndividual,
            Individual targetIndividual, List<SimpleRelationship> chain) {
        this.individual1 = startingIndividual;
        this.individual2 = targetIndividual;
        this.chain.clear();
        for (SimpleRelationship sr : chain) {
            this.chain.add(new SimpleRelationship(sr));
        }
    }

    /**
     * Simple sorting algorithm - sort by length of the chain (simpler
     * relationship)
     * 
     * @param other
     *            the Relationship we are comparing this one to
     * @return -1 if this relationship is simpler than the other, 0 if equally
     *         complex, and 1 if the other one is longer (or null)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Relationship other) {
        if (other == null) {
            return 1;
        }
        return Math.round(Math.signum(this.chain.size() - other.chain.size()));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Relationship)) {
            return false;
        }
        Relationship other = (Relationship) obj;
        if (chain == null) {
            if (other.chain != null) {
                return false;
            }
        } else if (!chain.equals(other.chain)) {
            return false;
        }
        if (individual1 == null) {
            if (other.individual1 != null) {
                return false;
            }
        } else if (!individual1.equals(other.individual1)) {
            return false;
        }
        if (individual2 == null) {
            if (other.individual2 != null) {
                return false;
            }
        } else if (!individual2.equals(other.individual2)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((chain == null) ? 0 : chain.hashCode());
        result = prime * result
                + ((individual1 == null) ? 0 : individual1.hashCode());
        result = prime * result
                + ((individual2 == null) ? 0 : individual2.hashCode());
        return result;
    }

    /**
     * Represent this object as a string
     * 
     * @return this object as a string
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("<");
        boolean first = true;
        for (SimpleRelationship sr : chain) {
            if (!first) {
                sb.append(", ");
            }
            first = false;
            sb.append(sr.individual1.names.get(0));
            sb.append("'s ").append(sr.name).append(" ");
            sb.append(sr.individual2.names.get(0));
        }
        sb.append(">, ").append(chain.size()).append(" step(s)");
        return sb.toString();
    }

}
