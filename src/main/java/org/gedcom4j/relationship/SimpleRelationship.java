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
package org.gedcom4j.relationship;

import org.gedcom4j.model.Individual;

/**
 * A single-hop, simple relationship between two directly related individuals, and the two individuals who are related.
 * 
 * @author frizbog1
 */
public class SimpleRelationship {
    /**
     * Individual 1
     */
    private Individual individual1;

    /**
     * Individual 2
     */
    private Individual individual2;

    /**
     * The name of the relationship from person 1 to person 2
     */
    private RelationshipName name;

    /**
     * The name of the relationship from person 2 to person 1
     */
    private RelationshipName reverseName;

    /**
     * Number of generations removed -- applies ONLY to cousin relationships
     */
    private int generationsRemoved = 0;

    /**
     * Default constructor
     */
    public SimpleRelationship() {
        super();
    }

    /**
     * Copy constructor
     * 
     * @param sr
     *            the other {@link SimpleRelationship} to copy
     */
    SimpleRelationship(SimpleRelationship sr) {
        individual1 = sr.individual1;
        individual2 = sr.individual2;
        name = sr.name;
        reverseName = sr.reverseName;
        generationsRemoved = sr.generationsRemoved;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof SimpleRelationship)) {
            return false;
        }
        SimpleRelationship other = (SimpleRelationship) obj;
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
        if (generationsRemoved != other.generationsRemoved) {
            return false;
        }
        if (name != other.name) {
            return false;
        }
        return (reverseName == other.reverseName);
    }

    /**
     * Get the generationsRemoved
     * 
     * @return the generationsRemoved
     */
    public int getGenerationsRemoved() {
        return generationsRemoved;
    }

    /**
     * Get the individual1
     * 
     * @return the individual1
     */
    public Individual getIndividual1() {
        return individual1;
    }

    /**
     * Get the individual2
     * 
     * @return the individual2
     */
    public Individual getIndividual2() {
        return individual2;
    }

    /**
     * Get the name
     * 
     * @return the name
     */
    public RelationshipName getName() {
        return name;
    }

    /**
     * Get the reverseName
     * 
     * @return the reverseName
     */
    public RelationshipName getReverseName() {
        return reverseName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (individual1 == null ? 0 : individual1.hashCode());
        result = prime * result + (individual2 == null ? 0 : individual2.hashCode());
        result = prime * result + (name == null ? 0 : name.hashCode());
        result = prime * result + (reverseName == null ? 0 : reverseName.hashCode());
        result = prime * result + generationsRemoved;
        return result;
    }

    /**
     * Set the generationsRemoved
     * 
     * @param generationsRemoved
     *            the generationsRemoved to set
     */
    public void setGenerationsRemoved(int generationsRemoved) {
        this.generationsRemoved = generationsRemoved;
    }

    /**
     * Set the individual1
     * 
     * @param individual1
     *            the individual1 to set
     */
    public void setIndividual1(Individual individual1) {
        this.individual1 = individual1;
    }

    /**
     * Set the individual2
     * 
     * @param individual2
     *            the individual2 to set
     */
    public void setIndividual2(Individual individual2) {
        this.individual2 = individual2;
    }

    /**
     * Set the name
     * 
     * @param name
     *            the name to set
     */
    public void setName(RelationshipName name) {
        this.name = name;
    }

    /**
     * Set the reverseName
     * 
     * @param reverseName
     *            the reverseName to set
     */
    public void setReverseName(RelationshipName reverseName) {
        this.reverseName = reverseName;
    }

    /**
     * Get a string representation of this object
     * 
     * @return string representation of this object
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append((individual1 == null ? "null" : individual1.getNames().get(0)) + "'s " + name);
        switch (generationsRemoved) {
            case 0:
                break;
            case 1:
                sb.append("_ONCE_REMOVED");
                break;
            case 2:
                sb.append("_TWICE_REMOVED");
                break;
            default:
                sb.append("_" + generationsRemoved + "X_REMOVED");
                break;
        }
        sb.append(" ").append(individual2 == null ? "null" : individual2.getNames().get(0));
        return sb.toString();
    }

}
