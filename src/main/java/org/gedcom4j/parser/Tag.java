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
package org.gedcom4j.parser;

/**
 * <p>
 * An enumeration of many - not all - of the tags supported by GEDCOM 5.5 and 5.5.1. Some noted omissions include
 * Individual Event Type tags and Family Event Type tags.
 * </p>
 * <p>
 * <b>Note:</b> There are multiple tags that have the same tag text. This represents the different uses of the same
 * three-to-five-character tag string in different contexts. As such, there is no function for deriving a tag from its
 * text.
 * 
 * @author frizbog
 */
enum Tag {
    /** Abbreviation */
    ABBREVIATION("ABBR"),
    /** Address */
    ADDRESS("ADDR"),
    /** Address 1 */
    ADDRESS_1("ADR1"),
    /** Address 2 */
    ADDRESS_2("ADR2"),
    /** Adoption */
    ADOPTION("ADOP"),
    /** Age */
    AGE("AGE"),
    /** Agency */
    AGENCY("AGNC"),
    /** Alias */
    ALIAS("ALIA"),
    /** Interest in an ancestor */
    ANCESTOR_INTEREST("ANCI"),
    /** Ancestors */
    ANCESTORS("ANCE"),
    /** Ancestral File Number */
    ANCESTRAL_FILE_NUMBER("AFN"),
    /** Association */
    ASSOCIATION("ASSO"),
    /** Authors */
    AUTHORS("AUTH"),
    /** BLOB (Binary Large OBject) - embedded media */
    BLOB("BLOB"),
    /** Call number */
    CALL_NUMBER("CALN"),
    /** Cause */
    CAUSE("CAUS"),
    /** Date/time of change */
    CHANGED_DATETIME("CHAN"),
    /** Character set */
    CHARACTER_SET("CHAR"),
    /** Child */
    CHILD("CHIL"),
    /** City */
    CITY("CITY"),
    /** Concatenation - more text, but do not start on a new line */
    CONCATENATION("CONC"),
    /** Continuation - more text, but start on a new line */
    CONTINUATION("CONT"),
    /** Copyright */
    COPYRIGHT("COPR"),
    /** Corporation */
    CORPORATION("CORP"),
    /** Country */
    COUNTRY("CTRY"),
    /** Data for a citation */
    DATA_FOR_CITATION("DATA"),
    /** Data for a source */
    DATA_FOR_SOURCE("DATA"),
    /** Date */
    DATE("DATE"),
    /** Interest in a descendant */
    DESCENDANT_INTEREST("DESI"),
    /** Descendants */
    DESCENDANTS("DESC"),
    /** Destination */
    DESTINATION("DEST"),
    /** Email address */
    EMAIL("EMAIL"),
    /** Event */
    EVENT("EVEN"),
    /** Family */
    FAMILY("FAM"),
    /** Family File */
    FAMILY_FILE("FAMF"),
    /** Family in which individual is a child */
    FAMILY_WHERE_CHILD("FAMC"),
    /** Family in which individual is a spouse */
    FAMILY_WHERE_SPOUSE("FAMS"),
    /** Fax number */
    FAX("FAX"),
    /** File */
    FILE("FILE"),
    /** Form */
    FORM("FORM"),
    /** GEDCOM spec version */
    GEDCOM_VERSION("GEDC"),
    /** Given name */
    GIVEN_NAME("GIVN"),
    /** Header */
    HEADER("HEAD"),
    /** Husband */
    HUSBAND("HUSB"),
    /** Individual */
    INDIVIDUAL("INDI"),
    /** Language */
    LANGUAGE("LANG"),
    /** Latitude */
    LATITUDE("LATI"),
    /** Longitude */
    LONGITUDE("LONG"),
    /** Map */
    MAP("MAP"),
    /** Media */
    MEDIA("MEDI"),
    /** Name */
    NAME("NAME"),
    /** Name Prefix */
    NAME_PREFIX("NPFX"),
    /** Name Suffix */
    NAME_SUFFIX("NSFX"),
    /** Nickname */
    NICKNAME("NICK"),
    /** Note */
    NOTE("NOTE"),
    /** Number of children */
    NUM_CHILDREN("NCHI"),
    /** Multimedia object */
    OBJECT_MULTIMEDIA("OBJE"),
    /** Ordinance Process Flag */
    ORDINANCE_PROCESS_FLAG("ORDI"),
    /** Page */
    PAGE("PAGE"),
    /** Pedigree */
    PEDIGREE("PEDI"),
    /** Phone number */
    PHONE("PHON"),
    /** Phonetic form/spelling */
    PHONETIC("FONE"),
    /** Place */
    PLACE("PLAC"),
    /** Postal Code */
    POSTAL_CODE("POST"),
    /** Publication facts */
    PUBLICATION_FACTS("PUBL"),
    /** Quality */
    QUALITY("QUAY"),
    /** Record ID Number */
    RECORD_ID_NUMBER("RIN"),
    /** Reference */
    REFERENCE("REFN"),
    /** Registration file number */
    REGISTRATION_FILE_NUMBER("RFN"),
    /** Relationship */
    RELATIONSHIP("RELA"),
    /** Religious Affiliation */
    RELIGION("RELI"),
    /** Repository */
    REPOSITORY("REPO"),
    /** Restriction */
    RESTRICTION("RESN"),
    /** Role */
    ROLE("ROLE"),
    /** Romanicized form/spelling */
    ROMANIZED("ROMN"),
    /** Sealing - Spouse */
    SEALING_SPOUSE("SLGS"),
    /** Sex */
    SEX("SEX"),
    /** Source */
    SOURCE("SOUR"),
    /** State */
    STATE("STAE"),
    /** Status */
    STATUS("STAT"),
    /** Submission */
    SUBMISSION("SUBN"),
    /** Submitter */
    SUBMITTER("SUBM"),
    /** Surname */
    SURNAME("SURN"),
    /** Surname prefix */
    SURNAME_PREFIX("SPFX"),
    /** Temple */
    TEMPLE("TEMP"),
    /** Text */
    TEXT("TEXT"),
    /** Title */
    TITLE("TITL"),
    /** Trailer */
    TRAILER("TRLR"),
    /** Type */
    TYPE("TYPE"),
    /** Version */
    VERSION("VERS"),
    /** Web address (URL) */
    WEB_ADDRESS("WWW"),
    /** Wife */
    WIFE("WIFE");

    /**
     * The text string for the tag
     */
    final String tagText;

    /**
     * Constructor for each enum value
     * 
     * @param tagText
     *            the text string for the tag
     */
    private Tag(String tagText) {
        this.tagText = tagText.intern();
    }

    /**
     * Return true iff the string provided matches the text string for this tag
     * 
     * @param s
     *            the string to evaluate against this Tag
     * @return true iff the string provided matches the text string for this tag
     */
    public boolean equalsText(String s) {
        return tagText.equals(s);
    }

    @Override
    public String toString() {
        return tagText;
    }

}
