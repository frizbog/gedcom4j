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
    private FrenchRepublicanMonth(String gedcomAbbrev) {
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
