package com.mattharrah.gedcom4j.parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mattharrah.gedcom4j.Address;
import com.mattharrah.gedcom4j.ChangeDate;
import com.mattharrah.gedcom4j.CharacterSet;
import com.mattharrah.gedcom4j.Citation;
import com.mattharrah.gedcom4j.CitationData;
import com.mattharrah.gedcom4j.CitationWithSource;
import com.mattharrah.gedcom4j.CitationWithoutSource;
import com.mattharrah.gedcom4j.Corporation;
import com.mattharrah.gedcom4j.Family;
import com.mattharrah.gedcom4j.FamilyChild;
import com.mattharrah.gedcom4j.FamilySpouse;
import com.mattharrah.gedcom4j.Gedcom;
import com.mattharrah.gedcom4j.GedcomVersion;
import com.mattharrah.gedcom4j.Header;
import com.mattharrah.gedcom4j.Individual;
import com.mattharrah.gedcom4j.IndividualAttribute;
import com.mattharrah.gedcom4j.IndividualAttributeType;
import com.mattharrah.gedcom4j.IndividualEvent;
import com.mattharrah.gedcom4j.IndividualEventType;
import com.mattharrah.gedcom4j.LdsIndividualOrdinance;
import com.mattharrah.gedcom4j.LdsIndividualOrdinanceType;
import com.mattharrah.gedcom4j.Multimedia;
import com.mattharrah.gedcom4j.Note;
import com.mattharrah.gedcom4j.PersonalName;
import com.mattharrah.gedcom4j.Place;
import com.mattharrah.gedcom4j.Source;
import com.mattharrah.gedcom4j.SourceData;
import com.mattharrah.gedcom4j.SourceSystem;
import com.mattharrah.gedcom4j.Submission;
import com.mattharrah.gedcom4j.Submitter;

public class GedcomParser {

	public List<String> errors = new ArrayList<String>();
	public Gedcom gedcom = new Gedcom();
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

	private Family getFamily(String xref) {
		Family f = gedcom.families.get(xref);
		if (f == null) {
			f = new Family();
			f.xref = xref;
			gedcom.families.put(xref, f);
		}
		return f;
	}

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

	private Source getSource(String xref) {
		Source src = gedcom.sources.get(xref);
		if (src == null) {
			src = new Source();
			src.xref = xref;
			gedcom.sources.put(src.xref, src);
		}
		return src;
	}

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

	private void loadCitation(StringTree st, List<Citation> list) {
		Citation source;
		if (referencesAnotherNode(st)) {
			source = new CitationWithSource();
			loadCitationWithSource(st, source);
		} else {
			source = new CitationWithoutSource();
			loadCitationWithoutSource(st, source);
		}
	}

	private void loadCitationData(StringTree st, CitationData d) {
		for (StringTree ch : st.children) {
			if ("DATE".equals(ch.tag)) {
				d.entryDate = ch.value;
			} else if ("TEXT".equals(ch.tag)) {
				List<String> ls = new ArrayList<String>();
				ls.add(ch.value);
				d.sourceText.add(ls);
				for (StringTree chch : ch.children) {
					if ("CONC".equals(chch.tag) || "CONT".equals(chch.tag)) {
						ls.add(chch.value);
					} else {
						unknownTag(chch);
					}
				}
			} else {
				unknownTag(ch);
			}
		}

	}

	private void loadCitationWithoutSource(StringTree st, Citation citation) {
		CitationWithoutSource cws = (CitationWithoutSource) citation;
		cws.description.add(st.value);
		for (StringTree ch : st.children) {
			if ("CONC".equals(ch.tag) || "CONT".equals(ch.tag)) {
				cws.description.add(ch.value);
			} else if ("TEXT".equals(ch.tag)) {
				List<String> ls = new ArrayList<String>();
				cws.textFromSource.add(ls);
				ls.add(ch.value);
				for (StringTree chch : ch.children) {
					if ("CONC".equals(chch.tag) || "CONT".equals(chch.tag)) {
						ls.add(chch.value);
					} else {
						unknownTag(chch);
					}
				}
			} else {
				unknownTag(ch);
			}
		}
	}

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
				cws.eventCited = ch.value;
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

	private void loadFamilyWhereChild(StringTree st,
			List<FamilyChild> familiesWhereChild) {
		Family f = getFamily(st.value);
		FamilyChild fc = new FamilyChild();
		fc.family = f;
		for (StringTree ch : st.children) {
			if ("NOTE".equals(ch.tag)) {
				loadNote(ch, fc.notes);
			} else if ("PEDI".equals(ch.tag)) {
				fc.pedigree = ch.value;
			} else if ("ADOP".equals(ch.tag)) {
			} else {
				unknownTag(ch);
			}
		}

	}

	private void loadFamilyWhereSpouse(StringTree st,
			List<FamilySpouse> familiesWhereSpouse) {
		Family f = getFamily(st.value);
		FamilySpouse fs = new FamilySpouse();
		fs.family = f;
		for (StringTree ch : st.children) {
			if ("NOTE".equals(ch.tag)) {
				loadNote(ch, fs.notes);
			} else {
				unknownTag(ch);
			}
		}
	}

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

	private void loadHeader(StringTree st, Header header) {
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
				gedcom.submitter = new Submitter();
				gedcom.submitter.xref = ch.id;
				header.submitter = gedcom.submitter;
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
				header.notes.add(ch.value);
				for (StringTree chch : ch.children) {
					if ("CONC".equals(chch.tag) || "CONT".equals(chch.tag)) {
						header.notes.add(chch.value);
					} else {
						unknownTag(chch);
					}
				}

			} else {
				unknownTag(ch);
			}
		}
	}

	private void loadIndividual(StringTree st, Individual i) {
		for (StringTree ch : st.children) {
			if ("NAME".equals(ch.tag)) {
				PersonalName pn = new PersonalName();
				i.names.add(pn);
				loadPersonalName(ch, pn);
			} else if ("SEX".equals(ch.tag)) {
				i.sex = ch.value;
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
			} else if ("FAMS".equals(ch.tag)) {
				loadFamilyWhereSpouse(ch, i.familiesWhereSpouse);
			} else if ("FAMC".equals(ch.tag)) {
				loadFamilyWhereChild(ch, i.familiesWhereChild);
			} else {
				unknownTag(ch);
			}
		}

	}

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
			} else {
				unknownTag(ch);
			}
		}
	}

	private void loadIndividualEvent(StringTree st, List<IndividualEvent> events) {
		IndividualEvent e = new IndividualEvent();
		events.add(e);
		for (StringTree ch : st.children) {
			if ("TYPE".equals(ch.tag)) {
				e.type = ch.value;
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
				loadCitation(ch, pn.sources);
			} else if ("NOTE".equals(ch.tag)) {
				loadNote(ch, pn.notes);
			} else {
				unknownTag(ch);
			}
		}

	}

	private void loadPlace(StringTree st, Place place) {
		place.placeName = st.value;
		for (StringTree ch : st.children) {
			if ("FORM".equals(ch.tag)) {
				place.placeFormat = ch.value;
			} else if ("SOUR".equals(ch.tag)) {
				loadCitation(ch, place.citations);
			} else if ("NOTE".equals(ch.tag)) {
				loadNote(ch, place.notes);
			} else {
				unknownTag(ch);
			}
		}

	}

	private void loadRootItems(StringTree st) {
		for (StringTree ch : st.children) {
			if ("HEAD".equals(ch.tag)) {
				gedcom.header = new Header();
				loadHeader(ch, gedcom.header);
			} else if ("SUBM".equals(ch.tag)) {
				if (gedcom.submitter == null) {
					gedcom.submitter = new Submitter();
				}
				loadSubmitter(ch, gedcom.submitter);
			} else if ("INDI".equals(ch.tag)) {
				Individual i = new Individual();
				gedcom.individuals.put(ch.id, i);
				loadIndividual(ch, i);
			} else if ("SUBN".equals(ch.tag)) {
				gedcom.submission = new Submission();
				loadSubmission(ch, gedcom.submission);
			} else if ("NOTE".equals(ch.tag)) {
				loadNote(ch, null);
			} else {
				unknownTag(ch);
			}
		}
	}

	private void loadSourceData(StringTree st, SourceData sourceData) {
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
				sourceSystem.sourceData = new SourceData();
				loadSourceData(ch, sourceSystem.sourceData);
			} else {
				unknownTag(ch);
			}
		}
	}

	private void loadSubmission(StringTree st, Submission submission) {
		for (StringTree ch : st.children) {
			if ("SUBM".equals(ch.tag)) {
				if (gedcom.submitter != null) {
					gedcom.submitter = new Submitter();
					gedcom.submitter.xref = ch.id;
				}
				submission.submitter = gedcom.submitter;
			} else if ("FAMF".equals(ch.tag)) {
				submission.nameOfFamilyFile = ch.value;
			} else if ("TEMP".equals(ch.tag)) {
				submission.templeCode = ch.value;
			} else if ("ANCE".equals(ch.tag)) {
				submission.ancestorsCount = ch.value;
			} else if ("DESC".equals(ch.tag)) {
				submission.descendantsCount = ch.value;
			} else if ("ORDI".equals(ch.tag)) {
				submission.ordinanceProcessFlag = ch.value;
			} else if ("RIN".equals(ch.tag)) {
				submission.recIdNumber = ch.value;
			} else {
				unknownTag(ch);
			}
		}

	}

	private void loadSubmitter(StringTree st, Submitter submitter) {
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
			} else {
				unknownTag(ch);
			}
		}
	}

	/**
	 * Load the file into a tree structure
	 * 
	 * @param filename
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
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
			sb.append(", child of ").append(st.tag).append(" on line ").append(
					st.lineNum);
		}
		if (node.tag.startsWith("_")) {
			warnings.add(sb.toString());
		} else {
			// new Exception(sb.toString()).printStackTrace();
			errors.add(sb.toString());
		}
	}

}
