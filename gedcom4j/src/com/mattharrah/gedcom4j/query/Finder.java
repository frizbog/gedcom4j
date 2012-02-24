package com.mattharrah.gedcom4j.query;

import java.util.ArrayList;
import java.util.List;

import com.mattharrah.gedcom4j.model.Gedcom;
import com.mattharrah.gedcom4j.model.Individual;
import com.mattharrah.gedcom4j.model.PersonalName;

/**
 * A class for finding specific data in a GEDCOM object graph
 * 
 * @author frizbog1
 */
public class Finder {
    /**
     * The gedcom object graph being searched
     */
    private Gedcom g;

    /**
     * Constructor. Requires a reference to the {@link Gedcom} object being
     * searched.
     * 
     * @param gedcom
     *            the {@link Gedcom} object being searched
     */
    public Finder(Gedcom gedcom) {
        g = gedcom;
    }

    /**
     * Find individuals whos surname and given names match the parameters.
     * 
     * @param surname
     *            the surname of the individual(s) you wish to find. Required,
     *            must match exactly (case insensitive).
     * @param given
     *            the given name of the individual(s) you wish to find.
     *            Required, must match exactly (case insensitive).
     * @return a {@link List} of {@link Individual}s that have both the surname
     *         and given name supplied.
     */
    public List<Individual> findByName(String surname, String given) {
        List<Individual> result = new ArrayList<Individual>();
        for (Individual i : g.individuals.values()) {
            for (PersonalName n : i.names) {
                // Sometimes the name is broken up into separate fields in the
                // GEDCOM
                if ((surname == null || surname.equalsIgnoreCase(n.surname))
                        && (given == null || given
                                .equalsIgnoreCase(n.givenName))) {
                    result.add(i);
                    continue;
                }
                // Other times they are concatenated with slashes around the
                // surname
                if (n.basic != null
                        && n.basic.equalsIgnoreCase("" + given + " /" + surname
                                + "/")) {
                    result.add(i);
                    continue;
                }
            }
        }
        return result;
    }

}
