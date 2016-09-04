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

import org.gedcom4j.model.Gedcom;
import org.gedcom4j.model.Individual;
import org.gedcom4j.model.IndividualEvent;
import org.gedcom4j.model.IndividualEventType;
import org.gedcom4j.model.PersonalName;
import org.gedcom4j.model.Place;
import org.gedcom4j.model.StringWithCustomTags;

/**
 * @author frizbog
 *
 */
public class IndividualFactory {

    /**
     * Create an {@link Individual} and add them to the {@link Gedcom}
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
     *            the birthdate of the new individual - optional
     * @param birthPlace
     *            the place of birth for the new individual - optional
     * @param death
     *            the death date of the new individual - optional
     * @param deathPlace
     *            the place of death for the new individual - optional
     * @return the individual. As a side effect, the individual is already added to the gedcom for you.
     */
    @SuppressWarnings("checkstyle:ParameterNumber")
    public Individual create(Gedcom g, String givenName, String surname, Sex sex, Date birth, String birthPlace, Date death,
            String deathPlace) {
        Individual result = new Individual();

        // Make an xref and add to gedcom
        for (int xref = g.getIndividuals().size(); !g.getIndividuals().containsKey("@I" + xref + "@") && result
                .getXref() == null; xref++) {
            result.setXref("@I" + xref + "@");
            g.getIndividuals().put(result.getXref(), result);
        }

        // Set sex
        if (sex != null) {
            result.setSex(new StringWithCustomTags(sex.getCode()));
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
        if (birth != null || birthPlace != null) {
            IndividualEvent b = new IndividualEvent();
            b.setType(IndividualEventType.BIRTH);
            if (birth != null) {
                b.setDate(new StringWithCustomTags(new SimpleDateFormat("d MMM yyyy", Locale.US).format(birth).toUpperCase(
                        Locale.US)));
            }
            if (birthPlace != null) {
                Place place = new Place();
                place.setPlaceName(birthPlace);
                b.setPlace(place);
            }
            result.getEvents(true).add(b);
        }

        // Set death event
        if (death != null || deathPlace != null) {
            IndividualEvent b = new IndividualEvent();
            b.setType(IndividualEventType.DEATH);
            if (death != null) {
                b.setDate(new StringWithCustomTags(new SimpleDateFormat("d MMM yyyy", Locale.US).format(death).toUpperCase(
                        Locale.US)));
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
