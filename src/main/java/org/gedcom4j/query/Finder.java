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
package org.gedcom4j.query;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.gedcom4j.model.Gedcom;
import org.gedcom4j.model.IGedcom;
import org.gedcom4j.model.Individual;
import org.gedcom4j.model.IndividualEvent;
import org.gedcom4j.model.PersonalName;
import org.gedcom4j.model.enumerations.IndividualEventType;
import org.gedcom4j.parser.DateParser;
import org.gedcom4j.parser.DateParser.ImpreciseDatePreference;

/**
 * A class for finding specific data in a GEDCOM object graph
 * 
 * @author frizbog1
 */
@SuppressWarnings("PMD.GodClass")
public class Finder {
    /** A regex pattern for finding the surname from a basic name */
    private static final Pattern BASIC_NAME_PATTERN = Pattern.compile("\\/[^\\/]*\\/");

    /**
     * The gedcom object graph being searched
     */
    private final IGedcom g;

    /**
     * Constructor. Requires a reference to the {@link Gedcom} object being searched.
     * 
     * @param gedcom
     *            the {@link Gedcom} object being searched
     */
    public Finder(IGedcom gedcom) {
        g = gedcom;
    }

    /**
     * Find individuals that have an event of a specific type, with a date that in any way overlaps the date range provided
     * 
     * @param eventType
     *            the type of event to look for
     * @param dateRangeStart
     *            the start of the date range during which the event has to overlap. A null value indicates that there's no limit on
     *            how early the event might have occurred to match.
     * @param dateRangeEnd
     *            the end of the date range during which the event has to overlap. A null value indicates that there's no limit on
     *            how late the event might have occurred to match.
     * @return a List of the individuals that match the criteria, if any. Returns an empty list on no matches.
     */
    public Set<Individual> findByEvent(IndividualEventType eventType, Date dateRangeStart, Date dateRangeEnd) {
        DateParser dp = new DateParser();
        Set<Individual> result = new HashSet<>();
        nextPerson: for (Individual i : g.getIndividuals().values()) {
            List<IndividualEvent> eventsOfType = i.getEventsOfType(eventType);
            nextEvent: for (IndividualEvent ie : eventsOfType) {
                if (dateRangeStart == null && dateRangeEnd == null) {
                    result.add(i);
                    continue nextPerson;
                }

                if (ie.getDate() == null || ie.getDate().getValue() == null) {
                    // No dates to parse, can't compare to range
                    continue nextEvent;
                }

                Date eventStart = dp.parse(ie.getDate().getValue(), ImpreciseDatePreference.FAVOR_EARLIEST);
                Date eventEnd = dp.parse(ie.getDate().getValue(), ImpreciseDatePreference.FAVOR_LATEST);

                if (dateRangeStart == null && eventStart != null && !eventStart.after(dateRangeEnd)) {
                    result.add(i);
                    continue nextPerson;
                }
                if (dateRangeEnd == null && eventEnd != null && !eventEnd.before(dateRangeStart)) {
                    result.add(i);
                    continue nextPerson;
                }
                if (dateRangeStart != null && dateRangeEnd != null && eventStart != null && eventEnd != null && !eventStart.after(
                        dateRangeEnd) && !eventEnd.before(dateRangeStart)) {
                    result.add(i);
                    continue nextPerson;
                }
            }
        }

        return result;
    }

    /**
     * Find individuals that have an event of a specific type, with a date that in any way overlaps the date range provided
     * 
     * @param eventType
     *            the type of event to look for
     * @param dateRangeStartAsString
     *            a GEDCOM format string representing the start of the date range during which the event has to overlap. A null
     *            value indicates that there's no limit on how early the event might have occurred to match.
     * @param dateRangeEndAsString
     *            a GEDCOM format string representing the end of the date range during which the event has to overlap. A null value
     *            indicates that there's no limit on how late the event might have occurred to match.
     * @return a List of the individuals that match the critaria, if any. Returns an empty list on no matches.
     */
    public Set<Individual> findByEvent(IndividualEventType eventType, String dateRangeStartAsString, String dateRangeEndAsString) {
        DateParser dp = new DateParser();
        Date dateRangeStart = null;
        if (dateRangeStartAsString != null) {
            dateRangeStart = dp.parse(dateRangeStartAsString, ImpreciseDatePreference.FAVOR_EARLIEST);
            if (dateRangeStart == null) {
                throw new IllegalArgumentException(dateRangeStartAsString + " could not be parsed as a date");
            }
        }
        Date dateRangeEnd = null;
        if (dateRangeEndAsString != null) {
            dateRangeEnd = dp.parse(dateRangeEndAsString, ImpreciseDatePreference.FAVOR_LATEST);
            if (dateRangeEnd == null) {
                throw new IllegalArgumentException(dateRangeEndAsString + " could not be parsed as a date");
            }
        }
        return findByEvent(eventType, dateRangeStart, dateRangeEnd);
    }

    /**
     * Find individuals whose surname and given names match the parameters.
     * 
     * @param surname
     *            the surname of the individual(s) you wish to find. Required, must match exactly (case insensitive).
     * @param given
     *            the given name of the individual(s) you wish to find. Required, must match exactly (case insensitive).
     * @return a {@link List} of {@link Individual}s that have both the surname and given name supplied.
     */
    public List<Individual> findByName(String surname, String given) {
        return findByName(null, surname, given, null);
    }

    /**
     * Find individuals whose surname and given names match the parameters.
     * 
     * @param prefix
     *            the prefix for the name (or null if no prefix)
     * 
     * @param surname
     *            the surname of the individual(s) you wish to find. Required, must match exactly (case insensitive).
     * @param given
     *            the given name of the individual(s) you wish to find. Required, must match exactly (case insensitive).
     * @param suffix
     *            the suffix for the name (or null if no suffix)
     * @return a {@link List} of {@link Individual}s that have both the surname and given name supplied.
     */
    public List<Individual> findByName(String prefix, String surname, String given, String suffix) {
        List<Individual> result = new ArrayList<>();
        for (Individual i : g.getIndividuals().values()) {
            if (i.getNames() != null) {
                for (PersonalName n : i.getNames()) {
                    // Sometimes the name is broken up into separate fields in the
                    // GEDCOM
                    if ((surname == null || n.getSurname() != null && surname.equalsIgnoreCase(n.getSurname().getValue()))
                            && (given == null || n.getGivenName() != null && given.equalsIgnoreCase(n.getGivenName().getValue()))) {
                        result.add(i);
                        continue;
                    }
                    // Other times they are concatenated with slashes around the
                    // surname
                    StringBuffer lookingFor = new StringBuffer();
                    lookingFor.append(given).append(" /").append(surname).append("/");
                    if (prefix != null) {
                        lookingFor.insert(0, " ").insert(0, prefix);
                    }
                    if (suffix != null) {
                        lookingFor.append(" ").append(suffix);
                    }

                    if (n.getBasic() != null && n.getBasic().equalsIgnoreCase(lookingFor.toString())) {
                        result.add(i);
                        continue;
                    }
                }
            }
        }
        return result;
    }

    /**
     * Find individuals whose surname and given names sound like the parameters supplied, using {@link Soundex} matching
     * 
     * @param surname
     *            the surname of the individual(s) you wish to find. Required, must match Soundex exactly.
     * @param given
     *            the given name of the individual(s) you wish to find. Required, must match Soundex exactly.
     * @return a {@link List} of {@link Individual}s that have both the surname and given name supplied.
     */
    @SuppressWarnings("PMD.AvoidDeeplyNestedIfStmts")
    public List<Individual> findByNameSoundsLike(String surname, String given) {
        if (surname == null) {
            throw new IllegalArgumentException("surname is required");
        }
        if (given == null) {
            throw new IllegalArgumentException("given name is required");
        }

        List<Individual> result = new ArrayList<>();
        for (Individual i : g.getIndividuals().values()) {
            if (i.getNames() != null && namesSoundAlike(surname, given, i)) {
                result.add(i);
            }
        }
        return result;
    }

    /**
     * Return true if the names supplied sound like the names on the individual supplied
     * 
     * @param surname
     *            the surname we're looking for
     * @param given
     *            the given name we're looking for
     * @param i
     *            the individual we are checking
     * @return true if the names supplied sound like the names on the individual supplied
     */
    private boolean namesSoundAlike(String surname, String given, Individual i) {
        for (PersonalName n : i.getNames()) {
            if (n.getSurname() != null || n.getGivenName() != null) {
                // Sometimes the name is broken up into separate fields
                boolean surnamesSoundAlike = n.getSurname() != null && soundsLike(surname, n.getSurname().getValue());
                boolean givenNamesSoundAlike = n.getGivenName() != null && soundsLike(given, n.getGivenName().getValue());
                if (surnamesSoundAlike && givenNamesSoundAlike) {
                    return true;
                }
            }
            // Other times they are concatenated with slashes around the
            // surname
            if (n.getBasic() == null) {
                continue;
            }

            Matcher matcher = BASIC_NAME_PATTERN.matcher(n.getBasic());
            boolean matched = matcher.find();
            if (matched) {
                String extractedGiven = n.getBasic().substring(0, matcher.start());
                if (extractedGiven.length() > 4 && (extractedGiven.startsWith("Mr. ") || extractedGiven.startsWith("Dr. ")
                        || extractedGiven.startsWith("Ms. "))) {
                    extractedGiven = extractedGiven.substring(4);
                }
                if (extractedGiven.length() > 5 && extractedGiven.startsWith("Mrs. ")) {
                    extractedGiven = extractedGiven.substring(5);
                }
                String extractedSurname = n.getBasic().substring(matcher.start() + 1, matcher.end() - 1);
                if (soundsLike(surname, extractedSurname) && soundsLike(given, extractedGiven)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Return true if the two strings supplied have the same Soundex mapping
     * 
     * @param s1
     *            string 1
     * @param s2
     *            string 2
     * @return true iff the two strings supplied have the same Soundex mapping
     */
    private boolean soundsLike(String s1, String s2) {
        if (s1 == null && s2 == null) {
            return true;
        }
        String sdx1 = Soundex.soundex(s1);
        String sdx2 = Soundex.soundex(s2);
        return sdx1 == null && sdx2 == null || sdx1 != null && sdx1.equals(sdx2);
    }

}
