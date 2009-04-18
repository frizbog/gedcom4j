package com.mattharrah.gedcom4j.parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mattharrah.gedcom4j.Address;
import com.mattharrah.gedcom4j.CharacterSet;
import com.mattharrah.gedcom4j.Citation;
import com.mattharrah.gedcom4j.CitationData;
import com.mattharrah.gedcom4j.CitationWithSource;
import com.mattharrah.gedcom4j.CitationWithoutSource;
import com.mattharrah.gedcom4j.Corporation;
import com.mattharrah.gedcom4j.EventDetail;
import com.mattharrah.gedcom4j.Gedcom;
import com.mattharrah.gedcom4j.GedcomVersion;
import com.mattharrah.gedcom4j.Header;
import com.mattharrah.gedcom4j.Individual;
import com.mattharrah.gedcom4j.IndividualEvent;
import com.mattharrah.gedcom4j.IndividualEventType;
import com.mattharrah.gedcom4j.PersonalName;
import com.mattharrah.gedcom4j.Source;
import com.mattharrah.gedcom4j.SourceSystem;
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
		StringTree stringTree = loadIntoTree(filename);
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

	private void loadAddress(StringTree st, Address address) {
		if (st.value != null) {
			address.lines.add(st.value);
		}
		for (StringTree ch : st.children) {
			if ("ADD1".equals(ch.tag)) {
				address.addr1 = ch.value;
			} else if ("ADD2".equals(ch.tag)) {
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
				unknownTagWarning(st, ch);
			}
		}
	}

	private void loadCitation(StringTree st, Citation source) {
		if (st.id == null) {
			loadCitationWithoutSource(st, source);
		} else {
			loadCitationWithSource(st, source);
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
					if ("CONC".equals(ch.tag) || "CONT".equals(ch.tag)) {
						ls.add(chch.value);
					} else {
						unknownTagWarning(ch, chch);
					}
				}
			} else {
				unknownTagWarning(st, ch);
			}

		}

	}

	private void loadCitationWithoutSource(StringTree st,
			Citation sourceCitation) {
		CitationWithoutSource cws = (CitationWithoutSource) sourceCitation;
		cws.description.add(st.value);
		for (StringTree ch : st.children) {
			if ("CONC".equals(ch.tag) || "CONT".equals(ch.tag)) {
				cws.description.add(ch.value);
			} else if ("TEXT".equals(ch.tag)) {
				List<String> ls = new ArrayList<String>();
				cws.textFromSource.add(ls);
				ls.add(ch.value);
				for (StringTree chch : ch.children) {
					if ("CONC".equals(ch.tag) || "CONT".equals(ch.tag)) {
						ls.add(chch.value);
					} else {
						unknownTagWarning(ch, chch);
					}
				}
			} else {
				unknownTagWarning(st, ch);
			}
		}
	}

	private void loadCitationWithSource(StringTree st, Citation sourceCitation) {
		CitationWithSource cws = (CitationWithSource) sourceCitation;
		Source src = gedcom.sources.get(st.id);
		if (src == null) {
			src = new Source();
			src.recFileNumber = st.id;
			gedcom.sources.put(st.id, src);
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
			} else {
				unknownTagWarning(st, ch);
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
				corporation.address.phoneNumbers.add(ch.value);
			} else {
				unknownTagWarning(st, ch);
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
				unknownTagWarning(st, ch);
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
				gedcom.submitter.recFileNumber = ch.id;
				header.submitter = gedcom.submitter;
			} else if ("FILE".equals(ch.tag)) {
				header.fileName = ch.value;
			} else if ("GEDC".equals(ch.tag)) {
				header.gedcomVersion = new GedcomVersion();
				loadGedcomVersion(ch, header.gedcomVersion);
			} else if ("COPR".equals(ch.tag)) {
				header.copyrightData = ch.value;
			} else {
				unknownTagWarning(st, ch);
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
			} else if ("BIRT".equals(ch.tag)) {
				IndividualEvent e = new IndividualEvent();
				i.events.add(e);
				e.type = IndividualEventType.BIRTH;
				loadIndividualEvent(ch, e);
			} else if ("GRAD".equals(ch.tag)) {
				IndividualEvent e = new IndividualEvent();
				i.events.add(e);
				e.type = IndividualEventType.GRADUATION;
				loadIndividualEvent(ch, e);
			} else if ("EVEN".equals(ch.tag)) {
				IndividualEvent e = new IndividualEvent();
				i.events.add(e);
				e.type = IndividualEventType.EVENT;
				loadIndividualEvent(ch, e);
			} else {
				unknownTagWarning(st, ch);
			}
		}

	}

	private void loadIndividualEvent(StringTree ch, IndividualEvent e) {
		warnings.add("loadIndividualEvent is not implemented yet");

	}

	/**
	 * Load the file into a tree structure
	 * 
	 * @param filename
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private StringTree loadIntoTree(String filename)
			throws FileNotFoundException, IOException {
		StringTree result = new StringTree();
		result.level = -1;
		BufferedReader f = new BufferedReader(new FileReader(filename));
		try {
			String line = f.readLine();
			while (line != null) {
				LinePieces lp = new LinePieces(line);
				StringTree st = new StringTree();
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
				Citation source;
				if (ch.id != null) {
					source = new CitationWithSource();
				} else {
					source = new CitationWithoutSource();
				}
				pn.citation = source;
				loadCitation(ch, source);
			} else {
				unknownTagWarning(st, ch);
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
			} else {
				unknownTagWarning(st, ch);
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
			} else {
				unknownTagWarning(st, ch);
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
			} else {
				unknownTagWarning(st, ch);
			}
		}
	}

	private void unknownTagWarning(StringTree parentNode, StringTree childNode) {
		if (childNode.tag.startsWith("_")) {
			warnings.add("Cannot handle level " + childNode.level + " tag "
					+ childNode.tag + " in " + parentNode.level + " "
					+ parentNode.tag + ", ignored");
		} else {
			errors.add("Cannot handle level " + childNode.level + " tag "
					+ childNode.tag + " in " + parentNode.level + " "
					+ parentNode.tag + ", ignored");
		}
	}

}
