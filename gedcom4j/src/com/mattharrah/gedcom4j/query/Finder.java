package com.mattharrah.gedcom4j.query;

import java.util.ArrayList;
import java.util.List;

import com.mattharrah.gedcom4j.Gedcom;
import com.mattharrah.gedcom4j.Individual;
import com.mattharrah.gedcom4j.PersonalName;

public class Finder {
	private Gedcom g;

	public Finder(Gedcom gedcom) {
		g = gedcom;
	}

	public List<Individual> findByName(String surname, String given) {
		List<Individual> result = new ArrayList<Individual>();
		for (Individual i : g.individuals.values()) {
			for (PersonalName n : i.names) {
				if ((surname == null || surname.equalsIgnoreCase(n.surname))
				        && (given == null || given
				                .equalsIgnoreCase(n.givenName))) {
					result.add(i);
					continue;
				}
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
