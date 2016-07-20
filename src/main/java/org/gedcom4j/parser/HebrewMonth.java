package org.gedcom4j.parser;

/**
 * An enumeration of the Hebrew Months
 * 
 * @author frizbog
 */
enum HebrewMonth {

    /** Tishrei */
    TISHREI("TSH"),
    /** Cheshvan */
    CHESHVAN("CSH"),
    /** Kislev */
    KISLEV("KSL"),
    /** Teves */
    TEVES("TVT"),
    /** Shevat */
    SHEVAT("SHV"),
    /** Adar_A (only valid on leap years) */
    ADAR_A("ADR"),
    /** Adar (called Adar B for leap years) */
    ADAR("ADS"),
    /** Nisan */
    NISAN("NSN"),
    /** Iyar */
    IYAR("IYR"),
    /** Sivan */
    SIVAN("SVN"),
    /** Tamuz */
    TAMUZ("TMZ"),
    /** Av */
    AV("AAV"),
    /** Elul */
    ELUL("ELL");

    /**
     * Get the Hebrew month by number. The number is 1-based (i.e., 1 is Tishrei).
     * 
     * @param hebrewMonth
     *            the 1-based number of the month (i.e., 1 is Tishrei)
     * @return the Hebrew month
     */
    public static HebrewMonth getFrom1BasedNumber(int hebrewMonth) {
        return HebrewMonth.values()[hebrewMonth - 1];
    }

    /**
     * Get an enum value from the gedcom abbreviation
     * 
     * @param abbrev
     *            The GEDCOM spec abbreviation for this month
     * @return the enum constant that matches the abbreviation
     */
    public static HebrewMonth getFromAbbreviation(String abbrev) {
        for (HebrewMonth hm : values()) {
            if (hm.gedcomAbbrev.equals(abbrev)) {
                return hm;
            }
        }
        return null;
    }

    /**
     * The GEDCOM spec abbreviation for this month
     */
    private final String gedcomAbbrev;

    /**
     * Constructor
     * 
     * @param gedcomAbbrev
     *            The GEDCOM spec abbreviation for this month
     */
    private HebrewMonth(String gedcomAbbrev) {
        this.gedcomAbbrev = gedcomAbbrev;
    }

    /**
     * Get the gedcomAbbrev
     * 
     * @return the gedcomAbbrev
     */
    public String getGedcomAbbrev() {
        return gedcomAbbrev;
    }

}