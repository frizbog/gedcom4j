/*
 * Copyright (c) 2009-2016 Matthew R. Harrah
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
package org.gedcom4j.comparators;

import java.io.Serializable;
import java.util.Comparator;

import org.gedcom4j.model.Individual;
import org.gedcom4j.model.PersonalName;

/**
 * Comparator for sorting individuals by last name (surname) first, then first (given) name
 * 
 * @author frizbog1
 * 
 */
public class IndividualByLastNameFirstNameComparator implements Serializable, Comparator<Individual> {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -8121061183483337581L;

    /**
     * Compare two individuals
     * 
     * @param i1
     *            individual 1
     * @param i2
     *            individual 2
     * @return -1 if i1 &lt; i2, 0 if i1 == i2, 1 if i1 &gt; i2
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(Individual i1, Individual i2) {
        String s1 = "-unknown-";
        String s2 = "-unknown-";
        PersonalName n1 = null;
        PersonalName n2 = null;
        if (i1.getNames() != null && !i1.getNames().isEmpty()) {
            n1 = i1.getNames().get(0);
        }
        if (i2.getNames() != null && !i2.getNames().isEmpty()) {
            n2 = i2.getNames().get(0);
        }

        if (n1 != null) {
            if (n1.getSurname() == null && n1.getGivenName() == null) {
                if (n1.getBasic().contains("/")) {
                    String sn = n1.getBasic().substring(n1.getBasic().indexOf("/"));
                    String gn = n1.getBasic().substring(0, n1.getBasic().indexOf("/"));
                    s1 = sn + ", " + gn;
                }
            } else {
                s1 = n1.getSurname() + ", " + n1.getGivenName();
            }
        }

        if (n2 != null) {
            if (n2.getSurname() == null && n2.getGivenName() == null) {
                if (n2.getBasic().contains("/")) {
                    String sn = n2.getBasic().substring(n2.getBasic().indexOf("/"));
                    String gn = n2.getBasic().substring(0, n2.getBasic().indexOf("/"));
                    s2 = sn + ", " + gn;
                }
            } else {
                s2 = n2.getSurname() + ", " + n2.getGivenName();
            }
        }

        return s1.compareTo(s2);
    }
}
