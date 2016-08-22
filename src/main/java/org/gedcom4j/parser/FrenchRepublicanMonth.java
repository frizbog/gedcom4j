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
package org.gedcom4j.parser;

/**
 * An enumeration of French Republican months
 * 
 * @author frizbog
 */
enum FrenchRepublicanMonth {

    /** Vendemiaire */
    VENDEMIAIRE("VEND"),
    /** Brumaire */
    BRUMAIRE("BRUM"),
    /** Frimaire */
    FRIMAIRE("FRIM"),
    /** Nivose */
    NIVOSE("NIVO"),
    /** Pluviose */
    PLUVIOSE("PLUV"),
    /** Ventose */
    VENTOSE("VENT"),
    /** Germinal */
    GERMINAL("GERM"),
    /** Floreal */
    FLOREAL("FLOR"),
    /** Prairial */
    PRAIRIAL("PRAI"),
    /** Messidor */
    MESSIDOR("MESS"),
    /** Thermidor */
    THERMIDOR("THER"),
    /** Fructidor */
    FRUCTIDOR("FRUC"),
    /** The complementary days at the end of each year */
    JOUR_COMPLEMENTAIRS("COMP");

    /**
     * Get the enumerated constant value with the supplied abbreviation
     * 
     * @param gedcomAbbrev
     *            the gedcom-spec abbreviation for the month
     * @return the enumerated constant value with the supplied abbreviation, or null if no match is found
     */
    @SuppressWarnings("ucd")
    public static FrenchRepublicanMonth getFromGedcomAbbrev(String gedcomAbbrev) {
        for (FrenchRepublicanMonth frm : values()) {
            if (gedcomAbbrev.equalsIgnoreCase(frm.getGedcomAbbrev())) {
                return frm;
            }
        }
        return null;
    }

    /**
     * The gedcom abbreviation
     */
    private final String gedcomAbbrev;

    /**
     * Constructor
     * 
     * @param gedcomAbbrev
     *            The gedcom abbreviation
     */
    FrenchRepublicanMonth(String gedcomAbbrev) {
        this.gedcomAbbrev = gedcomAbbrev;
    }

    /**
     * Get the gedcom abbreviation
     * 
     * @return the gedcom abbreviation
     */
    public String getGedcomAbbrev() {
        return gedcomAbbrev;
    }

}
