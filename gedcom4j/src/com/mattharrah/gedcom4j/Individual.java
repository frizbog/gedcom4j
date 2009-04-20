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
import java.util.List;

/**
 * An individual person.
 * 
 * @author Matt
 */
public class Individual {
	public List<Individual> aliases = new ArrayList<Individual>();
	public List<Individual> ancestorInterest = new ArrayList<Individual>();
	public String ancestralFileNumber;
	public List<Association> associations = new ArrayList<Association>();
	public List<IndividualAttribute> attributes = new ArrayList<IndividualAttribute>();
	public ChangeDate changeDate;
	public List<Citation> citations = new ArrayList<Citation>();
	public List<Individual> descendantInterest = new ArrayList<Individual>();
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
	public List<Individual> submitters = new ArrayList<Individual>();
	public List<UserReference> userReferences = new ArrayList<UserReference>();
	public String xref;
	public Address address;
	public List<String> phoneNumbers = new ArrayList<String>();
}
