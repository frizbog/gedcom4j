/*
 * Copyright (c) 2009 Matthew R. Harrah
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
package com.mattharrah.gedcom4j;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * An individual person.
 * 
 * @author Matt
 */
public class Individual {
	public List<String> aliases = new ArrayList<String>();
	public List<Submitter> ancestorInterest = new ArrayList<Submitter>();
	public String ancestralFileNumber;
	public List<Association> associations = new ArrayList<Association>();
	public List<IndividualAttribute> attributes = new ArrayList<IndividualAttribute>();
	public ChangeDate changeDate;
	public List<Citation> citations = new ArrayList<Citation>();
	public List<Submitter> descendantInterest = new ArrayList<Submitter>();
	public List<IndividualEvent> events = new ArrayList<IndividualEvent>();
	public List<FamilyChild> familiesWhereChild = new ArrayList<FamilyChild>();
	public List<FamilySpouse> familiesWhereSpouse = new ArrayList<FamilySpouse>();
	public List<LdsIndividualOrdinance> ldsIndividualOrdinances = new ArrayList<LdsIndividualOrdinance>();
	public List<Multimedia> multimedia = new ArrayList<Multimedia>();
	public List<PersonalName> names = new ArrayList<PersonalName>();
	public List<Note> notes = new ArrayList<Note>();
	public String permanentRecFileNumber;
	public String recIdNumber;
	public String regFileNumber;
	public String restrictionNotice;
	public String sex;
	public List<Submitter> submitters = new ArrayList<Submitter>();
	public List<UserReference> userReferences = new ArrayList<UserReference>();
	public String xref;
	public Address address;
	public List<String> phoneNumbers = new ArrayList<String>();

	public Set<Individual> getAncestors() {
		Set<Individual> result = new HashSet<Individual>();
		for (FamilyChild f : this.familiesWhereChild) {
			if (f.family.husband != null) {
				result.add(f.family.husband);
				result.addAll(f.family.husband.getAncestors());
			}
			if (f.family.wife != null) {
				result.add(f.family.wife);
				result.addAll(f.family.wife.getAncestors());
			}
		}
		return result;
	}

	public Set<Individual> getDescendants() {
		Set<Individual> result = new HashSet<Individual>();
		for (FamilySpouse f : this.familiesWhereSpouse) {
			result.addAll(f.family.children);
			for (Individual i : f.family.children) {
				result.addAll(i.getDescendants());
			}
		}
		return result;
	}

	public List<IndividualEvent> getEventsOfType(IndividualEventType type) {
		List<IndividualEvent> result = new ArrayList<IndividualEvent>();
		for (IndividualEvent ie : events) {
			if (type.tag.equalsIgnoreCase(ie.type)) {
				result.add(ie);
			}
		}
		return result;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (PersonalName n : names) {
			if (sb.length() > 0) {
				sb.append(" aka ");
			}
			sb.append(n);
		}
		for (String n : aliases) {
			if (sb.length() > 0) {
				sb.append(" aka ");
			}
			sb.append(n);
		}
		if (sb.length() == 0) {
			sb.append("Unknown name");
		}
		for (FamilySpouse f : familiesWhereSpouse) {
			sb.append(", spouse of ");
			if (f.family.husband == this) {
				sb.append(f.family.wife);
			} else {
				sb.append(f.family.husband);
			}
		}
		for (FamilyChild f : familiesWhereChild) {
			sb.append(", child of ");
			sb.append(f.family.wife);
			sb.append(" and ");
			sb.append(f.family.husband);
		}
		boolean found = false;
		for (IndividualEvent b : getEventsOfType(IndividualEventType.BIRTH)) {
			if (!found) {
				sb.append(", b.");
			}
			sb.append(b.date);
			found = true;
		}
		for (IndividualEvent b : getEventsOfType(IndividualEventType.DEATH)) {
			sb.append(", d." + b.date);
			break;
		}
		return sb.toString();
	}
}
