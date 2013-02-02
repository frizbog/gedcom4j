/*
 * Copyright (c) 2009-2013 Matthew R. Harrah
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
package org.gedcom4j.relationship;

import org.gedcom4j.model.Individual;

/**
 * A single-hop, simple relationship between two directly related individuals,
 * and the two individuals who are related.
 * 
 * @author frizbog1
 */
public class SimpleRelationship {
    /**
     * Individual 1
     */
    public Individual individual1;
    /**
     * Individual 2
     */
    public Individual individual2;

    /**
     * The name of the relationship from person 1 to person 2
     */
    public RelationshipName name;
    /**
     * The name of the relationship from person 2 to person 1
     */
    public RelationshipName reverseName;

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
    public SimpleRelationship(SimpleRelationship sr) {
        individual1 = sr.individual1;
        individual2 = sr.individual2;
        name = sr.name;
        reverseName = sr.reverseName;
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
        if (name != other.name) {
            return false;
        }
        if (reverseName != other.reverseName) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (individual1 == null ? 0 : individual1.hashCode());
        result = prime * result + (individual2 == null ? 0 : individual2.hashCode());
        result = prime * result + (name == null ? 0 : name.hashCode());
        result = prime * result + (reverseName == null ? 0 : reverseName.hashCode());
        return result;
    }

    /**
     * Get a string representation of this object
     * 
     * @return string representation of this object
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return (individual1 == null ? "null" : individual1.names.get(0)) + "'s " + name + " "
                + (individual2 == null ? "null" : individual2.names.get(0));
    }
}
