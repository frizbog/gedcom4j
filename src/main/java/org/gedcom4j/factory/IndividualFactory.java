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
package org.gedcom4j.factory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.gedcom4j.model.IGedcom;
import org.gedcom4j.model.Individual;
import org.gedcom4j.model.IndividualEvent;
import org.gedcom4j.model.PersonalName;
import org.gedcom4j.model.Place;
import org.gedcom4j.model.StringWithCustomFacts;
import org.gedcom4j.model.enumerations.IndividualEventType;

/**
 * @author frizbog
 *
 */
public class IndividualFactory {

    /**
     * Create an {@link Individual} and add them to the {@link IGedcom}
     * 
     * @param g
     *            the gedcom to add the individual to
     * @param givenName
     *            the given name of the new individual - optional
     * @param surname
     *            the surname of the new individual - optional
     * @param sex
     *            the sex of the new individual - optional
     * @param birth
     *            the birthdate of the new individual, from an actual {@link Date} - optional
     * @param birthPlace
     *            the place of birth for the new individual - optional
     * @param death
     *            the death date of the new individual, from an actual {@link Date} - optional
     * @param deathPlace
     *            the place of death for the new individual - optional
     * @return the individual. As a side effect, the individual is already added to the gedcom for you.
     */
    @SuppressWarnings("checkstyle:ParameterNumber")
    public Individual create(IGedcom g, String givenName, String surname, Sex sex, Date birth, String birthPlace, Date death,
            String deathPlace) {
        String deathDateString = null;
        if (death != null) {
            deathDateString = new SimpleDateFormat("d MMM yyyy", Locale.US).format(death).toUpperCase(Locale.US);
        }
        String birthDateString = null;
        if (birth != null) {
            birthDateString = new SimpleDateFormat("d MMM yyyy", Locale.US).format(birth).toUpperCase(Locale.US);
        }
        return create(g, givenName, surname, sex, birthDateString, birthPlace, deathDateString, deathPlace);
    }

    /**
     * Create an {@link Individual} and add them to the {@link IGedcom}
     * 
     * @param g
     *            the gedcom to add the individual to
     * @param givenName
     *            the given name of the new individual - optional
     * @param surname
     *            the surname of the new individual - optional
     * @param sex
     *            the sex of the new individual - optional
     * @param birthDateString
     *            the birthdate string of the new individual - optional
     * @param birthPlace
     *            the place of birth for the new individual - optional
     * @param deathDateString
     *            the death date of the new individual - optional
     * @param deathPlace
     *            the place of death for the new individual - optional
     * @return the individual. As a side effect, the individual is already added to the gedcom for you.
     */
    @SuppressWarnings("checkstyle:ParameterNumber")
    public Individual create(IGedcom g, String givenName, String surname, Sex sex, String birthDateString, String birthPlace,
            String deathDateString, String deathPlace) {

        Individual result = new Individual();

        // Make an xref and add to gedcom
        int xref = g.getIndividuals().size();
        while (g.getIndividuals().containsKey("@I" + xref + "@")) {
            xref++;
        }
        result.setXref("@I" + xref + "@");
        g.getIndividuals().put(result.getXref(), result);

        // Set sex
        if (sex != null) {
            result.setSex(new StringWithCustomFacts(sex.getCode()));
        }

        // Set name
        StringBuilder basicName = new StringBuilder();
        if (givenName != null) {
            basicName.append(givenName).append(" /");
        }
        if (surname != null) {
            basicName.append(surname);
        }
        basicName.append("/");
        PersonalName n = new PersonalName();
        n.setBasic(basicName.toString());
        result.getNames(true).add(n);

        // Set birth event
        if (birthDateString != null || birthPlace != null) {
            IndividualEvent b = new IndividualEvent();
            b.setType(IndividualEventType.BIRTH);
            if (birthDateString != null) {
                b.setDate(new StringWithCustomFacts(birthDateString));
            }
            if (birthPlace != null) {
                Place place = new Place();
                place.setPlaceName(birthPlace);
                b.setPlace(place);
            }
            result.getEvents(true).add(b);
        }

        // Set death event
        if (deathDateString != null || deathPlace != null) {
            IndividualEvent b = new IndividualEvent();
            b.setType(IndividualEventType.DEATH);
            if (deathDateString != null) {
                b.setDate(new StringWithCustomFacts(deathDateString));
            }
            if (deathPlace != null) {
                Place place = new Place();
                place.setPlaceName(deathPlace);
                b.setPlace(place);
            }
            result.getEvents(true).add(b);
        }

        return result;
    }

}
