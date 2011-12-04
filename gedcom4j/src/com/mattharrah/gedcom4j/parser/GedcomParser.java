/*
 * Copyright (c) 2009-2011 Matthew R. Harrah
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
package com.mattharrah.gedcom4j.parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mattharrah.gedcom4j.Address;
import com.mattharrah.gedcom4j.Association;
import com.mattharrah.gedcom4j.ChangeDate;
import com.mattharrah.gedcom4j.CharacterSet;
import com.mattharrah.gedcom4j.Citation;
import com.mattharrah.gedcom4j.CitationData;
import com.mattharrah.gedcom4j.CitationWithSource;
import com.mattharrah.gedcom4j.CitationWithoutSource;
import com.mattharrah.gedcom4j.Corporation;
import com.mattharrah.gedcom4j.EventRecorded;
import com.mattharrah.gedcom4j.Family;
import com.mattharrah.gedcom4j.FamilyChild;
import com.mattharrah.gedcom4j.FamilyEvent;
import com.mattharrah.gedcom4j.FamilyEventType;
import com.mattharrah.gedcom4j.FamilySpouse;
import com.mattharrah.gedcom4j.Gedcom;
import com.mattharrah.gedcom4j.GedcomVersion;
import com.mattharrah.gedcom4j.Header;
import com.mattharrah.gedcom4j.HeaderSourceData;
import com.mattharrah.gedcom4j.Individual;
import com.mattharrah.gedcom4j.IndividualAttribute;
import com.mattharrah.gedcom4j.IndividualAttributeType;
import com.mattharrah.gedcom4j.IndividualEvent;
import com.mattharrah.gedcom4j.IndividualEventType;
import com.mattharrah.gedcom4j.LdsFamilyOrdinance;
import com.mattharrah.gedcom4j.LdsIndividualOrdinance;
import com.mattharrah.gedcom4j.LdsIndividualOrdinanceType;
import com.mattharrah.gedcom4j.Multimedia;
import com.mattharrah.gedcom4j.Note;
import com.mattharrah.gedcom4j.PersonalName;
import com.mattharrah.gedcom4j.Place;
import com.mattharrah.gedcom4j.Repository;
import com.mattharrah.gedcom4j.RepositoryCitation;
import com.mattharrah.gedcom4j.Source;
import com.mattharrah.gedcom4j.SourceData;
import com.mattharrah.gedcom4j.SourceSystem;
import com.mattharrah.gedcom4j.Submission;
import com.mattharrah.gedcom4j.Submitter;
import com.mattharrah.gedcom4j.Trailer;
import com.mattharrah.gedcom4j.UserReference;

public class GedcomParser {

	/**
	 * The content of the gedcom file
	 */
	public Gedcom gedcom = new Gedcom();
	/**
	 * The things that went wrong while parsing the gedcom file
	 */
	public List<String> errors = new ArrayList<String>();
	/**
	 * The warnings issued during the parsing of the gedcome file
	 */
	public List<String> warnings = new ArrayList<String>();

	/**
	 * Load a gedcom file and create an object heirarchy from the data therein.
	 * 
	 * @param filename
	 *            the name of the file to load
	 * @throws IOException
	 *             if the file cannot be read
	 * @throws GedcomParserException
	 *             if the file cannot be parsed
	 */
	public void load(String filename) throws IOException, GedcomParserException {
		StringTree stringTree = readFile(filename);
		loadRootItems(stringTree);
	}

	/**
	 * Find the last item at the supplied level in the supplied tree
	 * 
	 * @param tree
	 *            the tree
	 * @param level
	 * @return the last item at the supplied level in the supplied tree
	 */
	private StringTree findLast(StringTree tree, int level) {
		if (tree.level == level) {
			return tree;
		}
		StringTree lastChild = tree.children.get(tree.children.size() - 1);
		if (lastChild.level == level) {
			return lastChild;
		}
		return findLast(lastChild, level);
	}

	/**
	 * Get a family by their xref, adding them to the gedcom collection of
	 * families if needed.
	 * 
	 * @param xref
	 *            the xref of the family
	 * @return the family with the specified xref
	 */
	private Family getFamily(String xref) {
		Family f = gedcom.families.get(xref);
		if (f == null) {
			f = new Family();
			f.xref = xref;
			gedcom.families.put(xref, f);
		}
		return f;
	}

	/**
	 * Get an individual by their xref, adding them to the gedcom collection of
	 * individuals if needed.
	 * 
	 * @param xref
	 *            the xref of the individual
	 * @return the individual with the specified xref
	 */
	private Individual getIndividual(String xref) {
		Individual i;
		i = gedcom.individuals.get(xref);
		if (i == null) {
			i = new Individual();
			i.xref = xref;
			gedcom.individuals.put(xref, i);
		}
		return i;
	}

	/**
	 * Get a multimedia item by its xref, adding it to the gedcom collection of
	 * multimedia items if needed.
	 * 
	 * @param xref
	 *            the xref of the multimedia item
	 * @return the multimedia item with the specified xref
	 */
	private Multimedia getMultimedia(String xref) {
		Multimedia m;
		m = gedcom.multimedia.get(xref);
		if (m == null) {
			m = new Multimedia();
			m.xref = xref;
			gedcom.multimedia.put(xref, m);
		}
		return m;
	}

	/**
	 * Get a note by its xref, adding it to the gedcom collection of notes if
	 * needed.
	 * 
	 * @param xref
	 *            the xref of the note
	 * @return the note with the specified xref
	 */
	private Note getNote(String xref) {
		Note note;
		note = gedcom.notes.get(xref);
		if (note == null) {
			note = new Note();
			note.xref = xref;
			gedcom.notes.put(xref, note);
		}
		return note;
	}

	/**
	 * Get a repository by its xref, adding it to the gedcom collection of
	 * repositories if needed.
	 * 
	 * @param xref
	 *            the xref of the repository
	 * @return the repository with the specified xref
	 */
	private Repository getRepository(String xref) {
		Repository r = gedcom.repositories.get(xref);
		if (r == null) {
			r = new Repository();
			r.xref = xref;
			gedcom.repositories.put(xref, r);
		}
		return r;

	}

	/**
	 * Get a source by its xref, adding it to the gedcom collection of sources
	 * if needed.
	 * 
	 * @param xref
	 *            the xref of the source
	 * @return the source with the specified xref
	 */
	private Source getSource(String xref) {
		Source src = gedcom.sources.get(xref);
		if (src == null) {
			src = new Source();
			src.xref = xref;
			gedcom.sources.put(src.xref, src);
		}
		return src;
	}

	/**
	 * Get a submitter by their xref, adding them to the gedcom collection of
	 * submitters if needed.
	 * 
	 * @param xref
	 *            the xref of the submitter
	 * @return the submitter with the specified xref
	 */
	private Submitter getSubmitter(String xref) {
		Submitter i;
		i = gedcom.submitters.get(xref);
		if (i == null) {
			i = new Submitter();
			i.xref = xref;
			gedcom.submitters.put(xref, i);
		}
		return i;
	}

	/**
	 * Load an address node into an address structure
	 * 
	 * @param st
	 *            the string tree node
	 * @param address
	 *            the address to load into
	 */
	private void loadAddress(StringTree st, Address address) {
		if (st.value != null) {
			address.lines.add(st.value);
		}
		for (StringTree ch : st.children) {
			if ("ADR1".equals(ch.tag)) {
				address.addr1 = ch.value;
			} else if ("ADR2".equals(ch.tag)) {
				address.addr2 = ch.value;
			} else if ("CITY".equals(ch.tag)) {
				address.city = ch.value;
			} else if ("STAE".equals(ch.tag)) {
				address.stateProvince = ch.value;
			} else if ("POST".equals(ch.tag)) {
				address.postalCode = ch.value;
			} else if ("CTRY".equals(ch.tag)) {
				address.country = ch.value;
			} else if ("CONT".equals(ch.tag)) {
				address.lines.add(ch.value);
			} else {
				unknownTag(ch);
			}
		}
	}

	/**
	 * Load an association between two individuals from a string tree node
	 * 
	 * @param st
	 *            the node
	 * @param associations
	 *            the list of associations to load into
	 */
	private void loadAssociation(StringTree st, List<Association> associations) {
		Association a = new Association();
		a.otherIndividual = getIndividual(st.value);
		for (StringTree ch : st.children) {
			if ("RELA".equals(ch.tag)) {
				a.relationship = ch.value;
			} else if ("NOTE".equals(ch.tag)) {
				loadNote(ch, a.notes);
			} else if ("SOUR".equals(ch.tag)) {
				loadCitation(ch, a.citations);
			} else {
				unknownTag(ch);
			}
		}

	}

	/**
	 * Load a change date structure from a string tree node
	 * 
	 * @param st
	 *            the node
	 * @param changeDate
	 *            the change date to load into
	 */
	private void loadChangeDate(StringTree st, ChangeDate changeDate) {
		for (StringTree ch : st.children) {
			if ("DATE".equals(ch.tag)) {
				changeDate.date = ch.value;
				if (!ch.children.isEmpty()) {
					changeDate.time = ch.children.get(0).value;
				}
			} else if ("NOTE".equals(ch.tag)) {
				loadNote(ch, changeDate.notes);
			}
		}

	}

	/**
	 * Load a source citation from a string tree node into a list of citations
	 * 
	 * @param st
	 *            the string tree node
	 * @param list
	 *            the list of citations to load into
	 */
	private void loadCitation(StringTree st, List<Citation> list) {
		Citation citation;
		if (referencesAnotherNode(st)) {
			citation = new CitationWithSource();
			loadCitationWithSource(st, citation);
		} else {
			citation = new CitationWithoutSource();
			loadCitationWithoutSource(st, citation);
		}
		list.add(citation);
	}

	/**
	 * Load a DATA structure in a source citation from a string tree node
	 * 
	 * @param st
	 *            the node
	 * @param d
	 *            the CitationData structure
	 */
	private void loadCitationData(StringTree st, CitationData d) {
		for (StringTree ch : st.children) {
			if ("DATE".equals(ch.tag)) {
				d.entryDate = ch.value;
			} else if ("TEXT".equals(ch.tag)) {
				List<String> ls = new ArrayList<String>();
				d.sourceText.add(ls);
				loadMultiLinesOfText(ch, ls);
			} else {
				unknownTag(ch);
			}
		}

	}

	/**
	 * Load the non-cross-referenced source citation from a string tree node
	 * 
	 * @param st
	 *            the node
	 * @param citation
	 *            the citation to load into
	 */
	private void loadCitationWithoutSource(StringTree st, Citation citation) {
		CitationWithoutSource cws = (CitationWithoutSource) citation;
		cws.description.add(st.value);
		for (StringTree ch : st.children) {
			if ("CONC".equals(ch.tag) || "CONT".equals(ch.tag)) {
				cws.description.add(ch.value);
			} else if ("TEXT".equals(ch.tag)) {
				List<String> ls = new ArrayList<String>();
				cws.textFromSource.add(ls);
				loadMultiLinesOfText(ch, ls);
			} else if ("NOTE".equals(ch.tag)) {
				loadNote(ch, cws.notes);
			} else {
				unknownTag(ch);
			}
		}
	}

	/**
	 * Load a cross-referenced source citation from a string tree node
	 * 
	 * @param st
	 *            the node
	 * @param citation
	 *            the citation to load into
	 */
	private void loadCitationWithSource(StringTree st, Citation citation) {
		CitationWithSource cws = (CitationWithSource) citation;
		Source src = null;
		if (referencesAnotherNode(st)) {
			src = getSource(st.value);
		}
		cws.source = src;
		for (StringTree ch : st.children) {
			if ("PAGE".equals(ch.tag)) {
				cws.whereInSource = ch.value;
			} else if ("EVEN".equals(ch.tag)) {
				System.out.println("Yo!!! " + ch);
				// cws.eventCited = ch.value;
			} else if ("DATA".equals(ch.tag)) {
				CitationData d = new CitationData();
				cws.data.add(d);
				loadCitationData(ch, d);
			} else if ("QUAY".equals(ch.tag)) {
				cws.certainty = ch.value;
			} else if ("NOTE".equals(ch.tag)) {
				loadNote(ch, cws.notes);
			} else {
				unknownTag(ch);
			}
		}
	}

	/**
	 * Load a corporation structure from a string tree node
	 * 
	 * @param st
	 *            the node
	 * @param corporation
	 *            the corporation structure to fill
	 */
	private void loadCorporation(StringTree st, Corporation corporation) {
		corporation.businessName = st.value;
		for (StringTree ch : st.children) {
			if ("ADDR".equals(ch.tag)) {
				corporation.address = new Address();
				loadAddress(ch, corporation.address);
			} else if ("PHON".equals(ch.tag)) {
				corporation.phoneNumbers.add(ch.value);
			} else {
				unknownTag(ch);
			}
		}
	}

	/**
	 * Load a family structure from a stringtree node, and load it into the
	 * gedcom family collection
	 * 
	 * @param st
	 *            the node
	 */
	private void loadFamily(StringTree st) {
		Family f = getFamily(st.id);
		for (StringTree ch : st.children) {
			if ("HUSB".equals(ch.tag)) {
				f.husband = getIndividual(ch.value);
			} else if ("WIFE".equals(ch.tag)) {
				f.wife = getIndividual(ch.value);
			} else if ("CHIL".equals(ch.tag)) {
				f.children.add(getIndividual(ch.value));
			} else if ("NCHI".equals(ch.tag)) {
				f.numChildren = Integer.parseInt(ch.value);
			} else if ("SOUR".equals(ch.tag)) {
				loadCitation(ch, f.citations);
			} else if ("OBJE".equals(ch.tag)) {
				loadMultimedia(ch, f.multimedia);
			} else if ("RIN".equals(ch.tag)) {
				f.recIdNumber = ch.value;
			} else if ("CHAN".equals(ch.tag)) {
				f.changeDate = new ChangeDate();
				loadChangeDate(ch, f.changeDate);
			} else if ("NOTE".equals(ch.tag)) {
				loadNote(ch, f.notes);
			} else if ("RFN".equals(ch.tag)) {
				f.regFileNumber = ch.value;
			} else if (FamilyEventType.isValidTag(ch.tag)) {
				loadFamilyEvent(ch, f.events);
			} else if ("SLGS".equals(ch.tag)) {
				loadLdsSpouseSealing(ch, f.ldsSpouseSealings);
			} else if ("SUBM".equals(ch.tag)) {
				f.submitters.add(getSubmitter(ch.value));
			} else if ("REFN".equals(ch.tag)) {
				UserReference u = new UserReference();
				f.userReferences.add(u);
				loadUserReference(ch, u);
			} else {
				unknownTag(ch);
			}
		}

	}

	/**
	 * Load a family event from a string tree node into a list of family events
	 * 
	 * @param st
	 *            the node
	 * @param events
	 *            the list of family events
	 */
	private void loadFamilyEvent(StringTree st, List<FamilyEvent> events) {
		FamilyEvent e = new FamilyEvent();
		events.add(e);
		e.type = FamilyEventType.getFromTag(st.tag);
		for (StringTree ch : st.children) {
			if ("TYPE".equals(ch.tag)) {
				e.subType = ch.value;
			} else if ("DATE".equals(ch.tag)) {
				e.date = ch.value;
			} else if ("PLAC".equals(ch.tag)) {
				e.place = new Place();
				loadPlace(ch, e.place);
			} else if ("OBJE".equals(ch.tag)) {
				loadMultimedia(ch, e.multimedia);
			} else if ("NOTE".equals(ch.tag)) {
				loadNote(ch, e.notes);
			} else if ("SOUR".equals(ch.tag)) {
				loadCitation(ch, e.citations);
			} else if ("AGE".equals(ch.tag)) {
				e.age = ch.value;
			} else if ("CAUS".equals(ch.tag)) {
				e.cause = ch.value;
			} else if ("ADDR".equals(ch.tag)) {
				e.address = new Address();
				loadAddress(ch, e.address);
			} else if ("AGNC".equals(ch.tag)) {
				e.respAgency = ch.value;
			} else if ("PHON".equals(ch.tag)) {
				e.phoneNumbers.add(ch.value);
			} else if ("HUSB".equals(ch.tag)) {
				e.husbandAge = ch.children.get(0).value;
			} else if ("WIFE".equals(ch.tag)) {
				e.wifeAge = ch.children.get(0).value;
			} else {
				unknownTag(ch);
			}
		}

	}

	/**
	 * Load a reference to a family where this individual was a child, from a
	 * string tree node
	 * 
	 * @param st
	 *            the string tree node
	 * @param familiesWhereChild
	 *            the list of families where the individual was a child
	 */
	private void loadFamilyWhereChild(StringTree st,
	        List<FamilyChild> familiesWhereChild) {
		Family f = getFamily(st.value);
		FamilyChild fc = new FamilyChild();
		familiesWhereChild.add(fc);
		fc.family = f;
		for (StringTree ch : st.children) {
			if ("NOTE".equals(ch.tag)) {
				loadNote(ch, fc.notes);
			} else if ("PEDI".equals(ch.tag)) {
				fc.pedigree = ch.value;
			} else if ("ADOP".equals(ch.tag)) {
				fc.adoptedBy = ch.value;
			} else {
				unknownTag(ch);
			}
		}

	}

	/**
	 * Load a reference to a family where this individual was a spouse, from a
	 * string tree node
	 * 
	 * @param st
	 *            the string tree node
	 * @param familiesWhereSpouse
	 *            the list of families where the individual was a child
	 */
	private void loadFamilyWhereSpouse(StringTree st,
	        List<FamilySpouse> familiesWhereSpouse) {
		Family f = getFamily(st.value);
		FamilySpouse fs = new FamilySpouse();
		fs.family = f;
		familiesWhereSpouse.add(fs);
		for (StringTree ch : st.children) {
			if ("NOTE".equals(ch.tag)) {
				loadNote(ch, fs.notes);
			} else {
				unknownTag(ch);
			}
		}
	}

	/**
	 * Load a gedcom version from the string tree node
	 * 
	 * @param st
	 *            the node
	 * @param gedcomVersion
	 *            the gedcom version structure to load
	 */
	private void loadGedcomVersion(StringTree st, GedcomVersion gedcomVersion) {
		for (StringTree ch : st.children) {
			if ("VERS".equals(ch.tag)) {
				gedcomVersion.versionNumber = ch.value;
			} else if ("FORM".equals(ch.tag)) {
				gedcomVersion.gedcomForm = ch.value;
			} else {
				unknownTag(ch);
			}
		}
	}

	/**
	 * Load a gedcom header from a string tree node
	 * 
	 * @param st
	 *            the node
	 */
	private void loadHeader(StringTree st) {
		Header header = new Header();
		gedcom.header = header;
		for (StringTree ch : st.children) {
			if ("SOUR".equals(ch.tag)) {
				header.sourceSystem = new SourceSystem();
				loadSourceSystem(ch, header.sourceSystem);
			} else if ("DEST".equals(ch.tag)) {
				header.destinationSystem = ch.value;
			} else if ("DATE".equals(ch.tag)) {
				header.date = ch.value;
				// one optional time subitem is the only possibility here
				if (!ch.children.isEmpty()) {
					header.time = ch.children.get(0).value;
				}
			} else if ("CHAR".equals(ch.tag)) {
				header.characterSet = new CharacterSet();
				header.characterSet.characterSetName = ch.value;
				// one optional version subitem is the only possibility here
				if (!ch.children.isEmpty()) {
					header.characterSet.versionNum = ch.children.get(0).value;
				}
			} else if ("SUBM".equals(ch.tag)) {
				header.submitter = getSubmitter(ch.value);
			} else if ("FILE".equals(ch.tag)) {
				header.fileName = ch.value;
			} else if ("GEDC".equals(ch.tag)) {
				header.gedcomVersion = new GedcomVersion();
				loadGedcomVersion(ch, header.gedcomVersion);
			} else if ("COPR".equals(ch.tag)) {
				header.copyrightData = ch.value;
			} else if ("SUBN".equals(ch.tag)) {
				; // do nothing, who cares
			} else if ("LANG".equals(ch.tag)) {
				header.language = ch.value;
			} else if ("PLAC".equals(ch.tag)) {
				header.placeStructure = ch.children.get(0).value;
			} else if ("NOTE".equals(ch.tag)) {
				loadMultiLinesOfText(ch, header.notes);
			} else {
				unknownTag(ch);
			}
		}
	}

	/**
	 * Load the header source data structure from a string tree node
	 * 
	 * @param st
	 *            the node
	 * @param sourceData
	 *            the header source data
	 */
	private void loadHeaderSourceData(StringTree st, HeaderSourceData sourceData) {
		sourceData.name = st.value;
		for (StringTree ch : st.children) {
			if ("DATE".equals(ch.tag)) {
				sourceData.publishDate = ch.value;
			} else if ("COPR".equals(ch.tag)) {
				sourceData.copyright = ch.value;
			} else {
				unknownTag(ch);
			}
		}

	}

	/**
	 * Load an individual from a string tree node
	 * 
	 * @param st
	 *            the node
	 */
	private void loadIndividual(StringTree st) {
		Individual i = new Individual();
		gedcom.individuals.put(st.id, i);
		for (StringTree ch : st.children) {
			if ("NAME".equals(ch.tag)) {
				PersonalName pn = new PersonalName();
				i.names.add(pn);
				loadPersonalName(ch, pn);
			} else if ("SEX".equals(ch.tag)) {
				i.sex = ch.value;
			} else if ("ADDR".equals(ch.tag)) {
				i.address = new Address();
				loadAddress(ch, i.address);
			} else if ("PHON".equals(ch.tag)) {
				i.phoneNumbers.add(ch.value);
			} else if (IndividualEventType.isValidTag(ch.tag)) {
				loadIndividualEvent(ch, i.events);
			} else if (IndividualAttributeType.isValidTag(ch.tag)) {
				loadIndividualAttribute(ch, i.attributes);
			} else if (LdsIndividualOrdinanceType.isValidTag(ch.tag)) {
				loadLdsIndividualOrdinance(ch, i.ldsIndividualOrdinances);
			} else if ("NOTE".equals(ch.tag)) {
				loadNote(ch, i.notes);
			} else if ("CHAN".equals(ch.tag)) {
				i.changeDate = new ChangeDate();
				loadChangeDate(ch, i.changeDate);
			} else if ("RIN".equals(ch.tag)) {
				i.recIdNumber = ch.value;
			} else if ("RFN".equals(ch.tag)) {
				i.regFileNumber = ch.value;
			} else if ("OBJE".equals(ch.tag)) {
				loadMultimedia(ch, i.multimedia);
			} else if ("RESN".equals(ch.tag)) {
				i.restrictionNotice = ch.value;
			} else if ("SOUR".equals(ch.tag)) {
				loadCitation(ch, i.citations);
			} else if ("ALIA".equals(ch.tag)) {
				i.aliases.add(ch.value);
			} else if ("FAMS".equals(ch.tag)) {
				loadFamilyWhereSpouse(ch, i.familiesWhereSpouse);
			} else if ("FAMC".equals(ch.tag)) {
				loadFamilyWhereChild(ch, i.familiesWhereChild);
			} else if ("ASSO".equals(ch.tag)) {
				loadAssociation(ch, i.associations);
			} else if ("ANCI".equals(ch.tag)) {
				i.ancestorInterest.add(getSubmitter(ch.value));
			} else if ("DESI".equals(ch.tag)) {
				i.descendantInterest.add(getSubmitter(ch.value));
			} else if ("AFN".equals(ch.tag)) {
				i.ancestralFileNumber = ch.value;
			} else if ("REFN".equals(ch.tag)) {
				UserReference u = new UserReference();
				i.userReferences.add(u);
				loadUserReference(ch, u);
			} else if ("SUBM".equals(ch.tag)) {
				i.submitters.add(getSubmitter(ch.value));
			} else {
				unknownTag(ch);
			}
		}

	}

	/**
	 * Load an attribute about an individual from a string tree node
	 * 
	 * @param st
	 *            the node
	 * @param attributes
	 *            the list of individual attributes
	 */
	private void loadIndividualAttribute(StringTree st,
	        List<IndividualAttribute> attributes) {
		IndividualAttribute a = new IndividualAttribute();
		attributes.add(a);
		a.type = IndividualAttributeType.getFromTag(st.tag).toString();
		for (StringTree ch : st.children) {
			if ("TYPE".equals(ch.tag)) {
				a.type = ch.value;
			} else if ("DATE".equals(ch.tag)) {
				a.date = ch.value;
			} else if ("PLAC".equals(ch.tag)) {
				a.place = new Place();
				loadPlace(ch, a.place);
			} else if ("AGE".equals(ch.tag)) {
				a.age = ch.value;
			} else if ("CAUS".equals(ch.tag)) {
				a.cause = ch.value;
			} else if ("SOUR".equals(ch.tag)) {
				loadCitation(ch, a.citations);
			} else if ("AGNC".equals(ch.tag)) {
				a.respAgency = ch.value;
			} else if ("PHON".equals(ch.tag)) {
				a.phoneNumbers.add(ch.value);
			} else if ("ADDR".equals(ch.tag)) {
				a.address = new Address();
				loadAddress(ch, a.address);
			} else if ("OBJE".equals(ch.tag)) {
				loadMultimedia(ch, a.multimedia);
			} else if ("NOTE".equals(ch.tag)) {
				loadNote(ch, a.notes);
			} else if ("SOUR".equals(ch.tag)) {
				loadCitation(ch, a.citations);
			} else if ("CONC".equals(ch.tag)) {
				if (a.description == null) {
					a.description = ch.value;
				} else {
					a.description += ch.value;
				}
			} else {
				unknownTag(ch);
			}
		}
	}

	/**
	 * Load an event about an individual from a string tree node
	 * 
	 * @param st
	 *            the node
	 * @param events
	 *            the list of events about an individual
	 */
	private void loadIndividualEvent(StringTree st, List<IndividualEvent> events) {
		IndividualEvent e = new IndividualEvent();
		events.add(e);
		e.type = IndividualEventType.getFromTag(st.tag);
		for (StringTree ch : st.children) {
			if ("TYPE".equals(ch.tag)) {
				e.subType = ch.value;
			} else if ("DATE".equals(ch.tag)) {
				e.date = ch.value;
			} else if ("PLAC".equals(ch.tag)) {
				e.place = new Place();
				loadPlace(ch, e.place);
			} else if ("OBJE".equals(ch.tag)) {
				loadMultimedia(ch, e.multimedia);
			} else if ("NOTE".equals(ch.tag)) {
				loadNote(ch, e.notes);
			} else if ("SOUR".equals(ch.tag)) {
				loadCitation(ch, e.citations);
			} else if ("AGE".equals(ch.tag)) {
				e.age = ch.value;
			} else if ("CAUS".equals(ch.tag)) {
				e.cause = ch.value;
			} else if ("ADDR".equals(ch.tag)) {
				e.address = new Address();
				loadAddress(ch, e.address);
			} else if ("AGNC".equals(ch.tag)) {
				e.respAgency = ch.value;
			} else if ("PHON".equals(ch.tag)) {
				e.phoneNumbers.add(ch.value);
			} else if ("CONC".equals(ch.tag)) {
				if (e.description == null) {
					e.description = ch.value;
				} else {
					e.description += ch.value;
				}
			} else if ("FAMC".equals(ch.tag)) {
				List<FamilyChild> families = new ArrayList<FamilyChild>();
				loadFamilyWhereChild(ch, families);
				if (!families.isEmpty()) {
					e.family = families.get(0);
				}
			} else {
				unknownTag(ch);
			}
		}

	}

	/**
	 * Load an LDS individual ordinance from a string tree node
	 * 
	 * @param st
	 *            the node
	 * @param ldsIndividualOrdinances
	 *            the list of LDS ordinances
	 */
	private void loadLdsIndividualOrdinance(StringTree st,
	        List<LdsIndividualOrdinance> ldsIndividualOrdinances) {
		LdsIndividualOrdinance o = new LdsIndividualOrdinance();
		ldsIndividualOrdinances.add(o);
		o.type = LdsIndividualOrdinanceType.getFromTag(st.tag);
		for (StringTree ch : st.children) {
			if ("DATE".equals(ch.tag)) {
				o.date = ch.value;
			} else if ("PLAC".equals(ch.tag)) {
				o.place = ch.value;
			} else if ("STAT".equals(ch.tag)) {
				o.status = ch.value;
			} else if ("TEMP".equals(ch.tag)) {
				o.temple = ch.value;
			} else if ("SOUR".equals(ch.tag)) {
				loadCitation(ch, o.citations);
			} else if ("NOTE".equals(ch.tag)) {
				loadNote(ch, o.notes);
			} else if ("FAMC".equals(ch.tag)) {
				List<FamilyChild> families = new ArrayList<FamilyChild>();
				loadFamilyWhereChild(ch, families);
				if (!families.isEmpty()) {
					o.familyWhereChild = families.get(0);
				}
			} else {
				unknownTag(ch);
			}
		}
	}

	/**
	 * Load an LDS Spouse Sealing from a string tree node
	 * 
	 * @param st
	 *            the string tree node
	 * @param ldsSpouseSealings
	 *            the list of LDS spouse sealings on the family
	 */
	private void loadLdsSpouseSealing(StringTree st,
	        List<LdsFamilyOrdinance> ldsSpouseSealings) {
		LdsFamilyOrdinance o = new LdsFamilyOrdinance();
		for (StringTree ch : st.children) {
			if ("DATE".equals(ch.tag)) {
				o.date = ch.value;
			} else if ("PLAC".equals(ch.tag)) {
				o.place = ch.value;
			} else if ("STAT".equals(ch.tag)) {
				o.status = ch.value;
			} else if ("TEMP".equals(ch.tag)) {
				o.temple = ch.value;
			} else if ("SOUR".equals(ch.tag)) {
				loadCitation(ch, o.citations);
			} else if ("NOTE".equals(ch.tag)) {
				loadNote(ch, o.notes);
			} else {
				unknownTag(ch);
			}

		}

	}

	/**
	 * Load multiple (continued) lines of text from a string tree node
	 * 
	 * @param st
	 *            the node
	 * @param listOfString
	 *            the list of string to load into
	 */
	private void loadMultiLinesOfText(StringTree st, List<String> listOfString) {
		if (st.value != null) {
			listOfString.add(st.value);
		}
		for (StringTree ch : st.children) {
			if ("CONC".equals(ch.tag) || "CONT".equals(ch.tag)) {
				listOfString.add(ch.value);
			} else {
				unknownTag(ch);
			}
		}
	}

	/**
	 * Load a multimedia reference from a string tree node
	 * 
	 * @param st
	 *            the string tree node
	 * @param multimedia
	 *            the list of multimedia on the current object
	 */
	private void loadMultimedia(StringTree st, List<Multimedia> multimedia) {
		Multimedia m = null;
		if (referencesAnotherNode(st)) {
			m = getMultimedia(st.value);
		} else {
			m = new Multimedia();
		}
		multimedia.add(m);

		for (StringTree ch : st.children) {
			if ("FORM".equals(ch.tag)) {
				m.format = ch.value;
			} else if ("TITL".equals(ch.tag)) {
				m.title = ch.value;
			} else if ("FILE".equals(ch.tag)) {
				m.fileReference = ch.value;
			} else if ("NOTE".equals(ch.tag)) {
				loadNote(ch, m.notes);
			} else {
				unknownTag(ch);
			}
		}

	}

	/**
	 * Load a multimedia record (that could be referenced from another object)
	 * from a string tree node
	 * 
	 * @param st
	 *            the node
	 */
	private void loadMultimediaRecord(StringTree st) {
		Multimedia m = getMultimedia(st.id);
		for (StringTree ch : st.children) {
			if ("FORM".equals(ch.tag)) {
				m.format = ch.value;
			} else if ("TITL".equals(ch.tag)) {
				m.title = ch.value;
			} else if ("NOTE".equals(ch.tag)) {
				loadNote(ch, m.notes);
			} else if ("SOUR".equals(ch.tag)) {
				loadCitation(ch, m.citations);
			} else if ("BLOB".equals(ch.tag)) {
				loadMultiLinesOfText(ch, m.blob);
			} else if ("OBJE".equals(ch.tag)) {
				List<Multimedia> continuedObjects = new ArrayList<Multimedia>();
				loadMultimedia(ch, continuedObjects);
				m.continuedObject = continuedObjects.get(0);
			} else if ("REFN".equals(ch.tag)) {
				UserReference u = new UserReference();
				m.userReferences.add(u);
				loadUserReference(ch, u);
			} else if ("RIN".equals(ch.tag)) {
				m.recIdNumber = ch.value;
			} else if ("CHAN".equals(ch.tag)) {
				m.changeDate = new ChangeDate();
				loadChangeDate(ch, m.changeDate);
			} else {
				unknownTag(ch);
			}
		}

	}

	/**
	 * Load a note from a string tree node into a list of notes
	 * 
	 * @param st
	 *            the node
	 * @param notes
	 *            the list of notes
	 */
	private void loadNote(StringTree st, List<Note> notes) {
		Note note = null;
		if (referencesAnotherNode(st)) {
			note = getNote(st.value);
		} else {
			note = new Note();
			note.lines.add(st.value);
			for (StringTree ch : st.children) {
				if ("CONC".equals(ch.tag) || "CONT".equals(ch.tag)) {
					note.lines.add(st.value);
				} else if ("SOUR".equals(ch.tag)) {
					loadCitation(ch, note.citations);
				}
			}
		}
	}

	/**
	 * Load a personal name structure from a string tree node
	 * 
	 * @param st
	 *            the node
	 * @param pn
	 *            the personal name structure to fill in
	 */
	private void loadPersonalName(StringTree st, PersonalName pn) {
		pn.basic = st.value;
		for (StringTree ch : st.children) {
			if ("NPFX".equals(ch.tag)) {
				pn.prefix = ch.value;
			} else if ("GIVN".equals(ch.tag)) {
				pn.givenName = ch.value;
			} else if ("NICK".equals(ch.tag)) {
				pn.nickname = ch.value;
			} else if ("SPFX".equals(ch.tag)) {
				pn.surnamePrefix = ch.value;
			} else if ("SURN".equals(ch.tag)) {
				pn.surname = ch.value;
			} else if ("NSFX".equals(ch.tag)) {
				pn.suffix = ch.value;
			} else if ("SOUR".equals(ch.tag)) {
				loadCitation(ch, pn.citations);
			} else if ("NOTE".equals(ch.tag)) {
				loadNote(ch, pn.notes);
			} else {
				unknownTag(ch);
			}
		}

	}

	/**
	 * Load a place structure from a string tree node
	 * 
	 * @param st
	 *            the node
	 * @param place
	 *            the place structure to fill in
	 */
	private void loadPlace(StringTree st, Place place) {
		place.placeName = st.value;
		for (StringTree ch : st.children) {
			if ("FORM".equals(ch.tag)) {
				place.placeFormat = ch.value;
			} else if ("SOUR".equals(ch.tag)) {
				loadCitation(ch, place.citations);
			} else if ("NOTE".equals(ch.tag)) {
				loadNote(ch, place.notes);
			} else if ("CONC".equals(ch.tag) || "CONT".equals(ch.tag)) {
				place.placeName += ch.value;
			} else {
				unknownTag(ch);
			}
		}

	}

	/**
	 * Load a repository for sources from a string tree node, and put it in the
	 * gedcom collection of repositories
	 * 
	 * @param st
	 *            the node
	 */
	private void loadRepository(StringTree st) {
		Repository r = getRepository(st.id);
		for (StringTree ch : st.children) {
			if ("NAME".equals(ch.tag)) {
				r.name = ch.value;
			} else if ("ADDR".equals(ch.tag)) {
				r.address = new Address();
				loadAddress(ch, r.address);
			} else if ("PHON".equals(ch.tag)) {
				r.phoneNumbers.add(ch.value);
			} else if ("NOTE".equals(ch.tag)) {
				loadNote(ch, r.notes);
			} else if ("REFN".equals(ch.tag)) {
				UserReference u = new UserReference();
				r.userReferences.add(u);
				loadUserReference(ch, u);
			} else if ("RIN".equals(ch.tag)) {
				r.recIdNumber = ch.value;
			} else if ("CHAN".equals(ch.tag)) {
				r.changeDate = new ChangeDate();
				loadChangeDate(ch, r.changeDate);
			} else if ("EMAIL".equals(ch.tag)) {
				r.emails.add(ch.value);
			} else {
				unknownTag(ch);
			}
		}

	}

	/**
	 * Load a reference to a repository in a source, from a string tree node
	 * 
	 * @param st
	 *            the node
	 * @param s
	 *            the source which is referencing a repository
	 */
	private void loadRepositoryCitation(StringTree st, Source s) {
		RepositoryCitation r = new RepositoryCitation();
		r.repository = getRepository(st.value);
		for (StringTree ch : st.children) {
			if ("NOTE".equals(ch.tag)) {
				loadNote(ch, r.notes);
			} else if ("CALN".equals(ch.tag)) {
				r.callNumber = ch.value;
				if (!ch.children.isEmpty()) {
					r.mediaType = ch.children.get(0).value;
				}
			} else {
				unknownTag(ch);
			}
		}

	}

	/**
	 * Load the root level items for the gedcom
	 * 
	 * @param st
	 *            the root of the string tree
	 */
	private void loadRootItems(StringTree st) {
		for (StringTree ch : st.children) {
			if ("HEAD".equals(ch.tag)) {
				loadHeader(ch);
			} else if ("SUBM".equals(ch.tag)) {
				loadSubmitter(ch);
			} else if ("INDI".equals(ch.tag)) {
				loadIndividual(ch);
			} else if ("SUBN".equals(ch.tag)) {
				loadSubmission(ch);
			} else if ("NOTE".equals(ch.tag)) {
				loadNote(ch, null);
			} else if ("FAM".equals(ch.tag)) {
				loadFamily(ch);
			} else if ("TRLR".equals(ch.tag)) {
				gedcom.trailer = new Trailer();
			} else if ("SOUR".equals(ch.tag)) {
				loadSource(ch);
			} else if ("REPO".equals(ch.tag)) {
				loadRepository(ch);
			} else if ("OBJE".equals(ch.tag)) {
				loadMultimediaRecord(ch);
			} else {
				unknownTag(ch);
			}
		}
	}

	/**
	 * Load a source (which may be referenced later) from a source tree node,
	 * and put it in the gedcom collection of sources.
	 * 
	 * @param st
	 *            the node
	 */
	private void loadSource(StringTree st) {
		Source s = getSource(st.id);
		for (StringTree ch : st.children) {
			if ("DATA".equals(ch.tag)) {
				s.data = new SourceData();
				loadSourceData(ch, s.data);
			} else if ("TITL".equals(ch.tag)) {
				loadMultiLinesOfText(ch, s.title);
			} else if ("PUBL".equals(ch.tag)) {
				loadMultiLinesOfText(ch, s.publicationFacts);
			} else if ("TEXT".equals(ch.tag)) {
				loadMultiLinesOfText(ch, s.sourceText);
			} else if ("ABBR".equals(ch.tag)) {
				s.sourceFiledBy = ch.value;
			} else if ("AUTH".equals(ch.tag)) {
				loadMultiLinesOfText(ch, s.originatorsAuthors);
			} else if ("REPO".equals(ch.tag)) {
				loadRepositoryCitation(ch, s);
			} else if ("NOTE".equals(ch.tag)) {
				loadNote(ch, s.notes);
			} else if ("OBJE".equals(ch.tag)) {
				loadMultimedia(ch, s.multimedia);
			} else if ("REFN".equals(ch.tag)) {
				UserReference u = new UserReference();
				s.userReferences.add(u);
				loadUserReference(ch, u);
			} else if ("RIN".equals(ch.tag)) {
				s.recIdNumber = ch.value;
			} else if ("RFN".equals(ch.tag)) {
				s.regFileNumber = ch.value;
			} else if ("CHAN".equals(ch.tag)) {
				s.changeDate = new ChangeDate();
				loadChangeDate(ch, s.changeDate);
			} else {
				unknownTag(ch);
			}
		}
	}

	/**
	 * Load data for a source from a string tree node into a source data
	 * structure
	 * 
	 * @param st
	 *            the node
	 * @param data
	 *            the source data structure
	 */
	private void loadSourceData(StringTree st, SourceData data) {
		for (StringTree ch : st.children) {
			if ("EVEN".equals(ch.tag)) {
				loadSourceDataEventRecorded(ch, data);
			} else if ("NOTE".equals(ch.tag)) {
				loadNote(ch, data.notes);
			} else if ("AGNC".equals(ch.tag)) {
				data.respAgency = ch.value;
			} else {
				unknownTag(ch);
			}
		}
	}

	/**
	 * Load the data for a recorded event from a string tree node
	 * 
	 * @param st
	 *            the node
	 * @param data
	 *            the source data
	 */
	private void loadSourceDataEventRecorded(StringTree st, SourceData data) {
		EventRecorded e = new EventRecorded();
		e.eventType = st.value;
		for (StringTree ch : st.children) {
			if ("DATE".equals(ch.tag)) {
				e.datePeriod = ch.value;
			} else if ("PLAC".equals(ch.tag)) {
				e.jurisdiction = ch.value;
			} else {
				unknownTag(ch);
			}
		}

	}

	/**
	 * Load a source system structure from a string tree node
	 * 
	 * @param st
	 *            the node
	 * @param sourceSystem
	 *            the source system structure
	 */
	private void loadSourceSystem(StringTree st, SourceSystem sourceSystem) {
		sourceSystem.systemId = st.value;
		for (StringTree ch : st.children) {
			if ("VERS".equals(ch.tag)) {
				sourceSystem.versionNum = ch.value;
			} else if ("NAME".equals(ch.tag)) {
				sourceSystem.productName = ch.value;
			} else if ("CORP".equals(ch.tag)) {
				sourceSystem.corporation = new Corporation();
				loadCorporation(ch, sourceSystem.corporation);
			} else if ("DATA".equals(ch.tag)) {
				sourceSystem.sourceData = new HeaderSourceData();
				loadHeaderSourceData(ch, sourceSystem.sourceData);
			} else {
				unknownTag(ch);
			}
		}
	}

	/**
	 * Load the submission structure from a string tree node
	 * 
	 * @param st
	 *            the node
	 */
	private void loadSubmission(StringTree st) {
		Submission s = new Submission();
		gedcom.submission = s;
		for (StringTree ch : st.children) {
			if ("SUBM".equals(ch.tag)) {
				gedcom.submission.submitter = getSubmitter(ch.value);
			} else if ("FAMF".equals(ch.tag)) {
				gedcom.submission.nameOfFamilyFile = ch.value;
			} else if ("TEMP".equals(ch.tag)) {
				gedcom.submission.templeCode = ch.value;
			} else if ("ANCE".equals(ch.tag)) {
				gedcom.submission.ancestorsCount = ch.value;
			} else if ("DESC".equals(ch.tag)) {
				gedcom.submission.descendantsCount = ch.value;
			} else if ("ORDI".equals(ch.tag)) {
				gedcom.submission.ordinanceProcessFlag = ch.value;
			} else if ("RIN".equals(ch.tag)) {
				gedcom.submission.recIdNumber = ch.value;
			} else {
				unknownTag(ch);
			}
		}

	}

	/**
	 * Load a submitter from a string tree node into the gedcom global
	 * colelction of submitters
	 * 
	 * @param st
	 *            the node
	 */
	private void loadSubmitter(StringTree st) {
		Submitter submitter = getSubmitter(st.id);
		gedcom.submitters.put(st.id, submitter);
		for (StringTree ch : st.children) {
			if ("NAME".equals(ch.tag)) {
				submitter.name = ch.value;
			} else if ("ADDR".equals(ch.tag)) {
				submitter.address = new Address();
				loadAddress(ch, submitter.address);
			} else if ("PHON".equals(ch.tag)) {
				submitter.phoneNumbers.add(ch.value);
			} else if ("LANG".equals(ch.tag)) {
				submitter.languagePref.add(ch.value);
			} else if ("CHAN".equals(ch.tag)) {
				submitter.changeDate = new ChangeDate();
				loadChangeDate(ch, submitter.changeDate);
			} else if ("OBJE".equals(ch.tag)) {
				loadMultimedia(ch, submitter.multimedia);
			} else if ("RIN".equals(ch.tag)) {
				submitter.recIdNumber = ch.value;
			} else if ("RFN".equals(ch.tag)) {
				submitter.regFileNumber = ch.value;
			} else if ("EMAIL".equals(ch.tag)) {
				submitter.emails.add(ch.value);
			} else {
				unknownTag(ch);
			}
		}
	}

	/**
	 * Load a user reference to from a string tree node
	 * 
	 * @param st
	 *            the string tree node
	 * @param u
	 *            the user reference object
	 */
	private void loadUserReference(StringTree st, UserReference u) {
		u.referenceNum = st.value;
		if (!st.children.isEmpty()) {
			u.type = st.children.get(0).value;
		}

	}

	/**
	 * Load the file into a tree structure
	 * 
	 * @param filename
	 *            the file to load
	 * @return the string tree
	 * @throws FileNotFoundException
	 *             if the file cannot be found
	 * @throws IOException
	 *             if there is a problem reading the file
	 */
	private StringTree readFile(String filename) throws FileNotFoundException,
	        IOException {
		StringTree result = new StringTree();
		result.level = -1;
		BufferedReader f = new BufferedReader(new FileReader(filename));
		int lineNum = 0;
		try {
			String line = f.readLine();
			while (line != null) {
				lineNum++;
				LinePieces lp = new LinePieces(line);
				StringTree st = new StringTree();
				st.lineNum = lineNum;
				st.level = lp.level;
				st.id = lp.id;
				st.tag = lp.tag;
				st.value = lp.remainder;
				StringTree addTo = findLast(result, lp.level - 1);
				addTo.children.add(st);
				st.parent = addTo;
				line = f.readLine();
			}
		} finally {
			if (f != null) {
				f.close();
			}
		}
		return result;
	}

	/**
	 * Returns true if the node passed in uses a cross-reference to another node
	 * 
	 * @param st
	 *            the node
	 * @return true if and only if the node passed in uses a cross-reference to
	 *         another node
	 */
	private boolean referencesAnotherNode(StringTree st) {
		return st.value != null && st.value.matches("\\@.*\\@");
	}

	/**
	 * Note unknown tags. If the tag begins with an underscore, it is a
	 * vendor/system-specific tag, which is unrecognized and just ignored and
	 * added as a warning. If it does not begin with an underscore, it is a real
	 * tag from the spec and should be processed, so that would indicate a bug.
	 * 
	 * @param node
	 *            the node containing the unknown tag.
	 */
	private void unknownTag(StringTree node) {
		StringBuilder sb = new StringBuilder("Line " + node.lineNum
		        + ": Cannot handle tag ");
		sb.append(node.tag);
		StringTree st = node;
		while (st.parent != null) {
			st = st.parent;
			sb.append(", child of ").append(st.tag).append(" on line ")
			        .append(st.lineNum);
		}
		if (node.tag.startsWith("_")) {
			warnings.add(sb.toString());
		} else {
			new Exception(sb.toString()).printStackTrace();
			errors.add(sb.toString());
		}
	}

}
