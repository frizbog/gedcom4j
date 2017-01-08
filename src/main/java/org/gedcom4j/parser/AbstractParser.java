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
package org.gedcom4j.parser;

import java.util.List;

import org.gedcom4j.model.AbstractElement;
import org.gedcom4j.model.CustomFact;
import org.gedcom4j.model.Family;
import org.gedcom4j.model.HasCustomFacts;
import org.gedcom4j.model.IGedcom;
import org.gedcom4j.model.Individual;
import org.gedcom4j.model.MultiStringWithCustomFacts;
import org.gedcom4j.model.Multimedia;
import org.gedcom4j.model.NoteRecord;
import org.gedcom4j.model.Repository;
import org.gedcom4j.model.Source;
import org.gedcom4j.model.StringTree;
import org.gedcom4j.model.StringWithCustomFacts;
import org.gedcom4j.model.Submitter;
import org.gedcom4j.model.enumerations.SupportedVersion;

/**
 * A base class for all Parser subclasses.
 * 
 * @param <T>
 *            The type of object this parser can load into
 * @author frizbog
 */
@SuppressWarnings("PMD.GodClass")
abstract class AbstractParser<T> {
    /** The {@link StringTree} to be parsed */
    protected final StringTree stringTree;

    /** a reference to the root {@link GedcomParser} */
    protected final GedcomParser gedcomParser;

    /** a reference to the object we are loading data into */
    protected final T loadInto;

    /**
     * Constructor
     * 
     * @param gedcomParser
     *            a reference to the root {@link GedcomParser}
     * @param stringTree
     *            {@link StringTree} to be parsed
     * @param loadInto
     *            the object we are loading data into
     */
    AbstractParser(GedcomParser gedcomParser, StringTree stringTree, T loadInto) {
        this.gedcomParser = gedcomParser == null && this instanceof GedcomParser ? (GedcomParser) this : gedcomParser;
        this.stringTree = stringTree;
        this.loadInto = loadInto;
    }

    /**
     * Add an error to the errors collection on the root parser
     * 
     * @param string
     *            the text of the error
     */
    protected void addError(String string) {
        gedcomParser.getErrors().add(string);
    }

    /**
     * Add a warning to the warnings collection on the root parser
     * 
     * @param string
     *            the text of the error
     */
    protected void addWarning(String string) {
        gedcomParser.getWarnings().add(string);
    }

    /**
     * Returns true if and only if the Gedcom data says it is for the 5.5 standard.
     * 
     * @return true if and only if the Gedcom data says it is for the 5.5 standard.
     */
    protected final boolean g55() {
        IGedcom g = gedcomParser.getGedcom();
        return g != null && g.getHeader() != null && g.getHeader().getGedcomVersion() != null && g.getHeader().getGedcomVersion()
                .getVersionNumber() != null && SupportedVersion.V5_5.toString().equals(g.getHeader().getGedcomVersion()
                        .getVersionNumber().getValue());
    }

    /**
     * Get a family by their xref, adding them to the gedcom collection of families if needed.
     * 
     * @param xref
     *            the xref of the family
     * @return the family with the specified xref
     */
    protected Family getFamily(String xref) {
        Family f = gedcomParser.getGedcom().getFamilies().get(xref);
        if (f == null) {
            f = new Family();
            f.setXref(xref);
            gedcomParser.getGedcom().getFamilies().put(xref, f);
        }
        return f;
    }

    /**
     * Get an individual by their xref, adding them to the gedcom collection of individuals if needed.
     * 
     * @param xref
     *            the xref of the individual
     * @return the individual with the specified xref
     */
    protected Individual getIndividual(String xref) {
        Individual i;
        i = gedcomParser.getGedcom().getIndividuals().get(xref);
        if (i == null) {
            i = new Individual();
            i.setXref(xref);
            gedcomParser.getGedcom().getIndividuals().put(xref, i);
        }
        return i;
    }

    /**
     * Get a multimedia item by its xref, adding it to the gedcom collection of multimedia items if needed.
     * 
     * @param xref
     *            the xref of the multimedia item
     * @return the multimedia item with the specified xref
     */
    protected Multimedia getMultimedia(String xref) {
        Multimedia m;
        m = gedcomParser.getGedcom().getMultimedia().get(xref);
        if (m == null) {
            m = new Multimedia();
            m.setXref(xref);
            gedcomParser.getGedcom().getMultimedia().put(xref, m);
        }
        return m;
    }

    /**
     * Get a note record by their xref, adding them to the gedcom collection of {@link NoteRecord}s if needed.
     * 
     * @param xref
     *            the xref of the note record
     * @return the note record with the specified xref
     */
    protected NoteRecord getNoteRecord(String xref) {
        NoteRecord nr;
        nr = gedcomParser.getGedcom().getNotes().get(xref);
        if (nr == null) {
            nr = new NoteRecord(xref);
            gedcomParser.getGedcom().getNotes().put(xref, nr);
        }
        return nr;
    }

    /**
     * Get a repository by its xref, adding it to the gedcom collection of repositories if needed.
     * 
     * @param xref
     *            the xref of the repository
     * @return the repository with the specified xref
     */
    protected Repository getRepository(String xref) {
        Repository r = gedcomParser.getGedcom().getRepositories().get(xref);
        if (r == null) {
            r = new Repository();
            r.setXref(xref);
            gedcomParser.getGedcom().getRepositories().put(xref, r);
        }
        return r;

    }

    /**
     * Get a source by its xref, adding it to the gedcom collection of sources if needed.
     * 
     * @param xref
     *            the xref of the source
     * @return the source with the specified xref
     */
    protected Source getSource(String xref) {
        Source src = gedcomParser.getGedcom().getSources().get(xref);
        if (src == null) {
            src = new Source(xref);
            gedcomParser.getGedcom().getSources().put(src.getXref(), src);
        }
        return src;
    }

    /**
     * Get a submitter by their xref, adding them to the gedcom collection of submitters if needed.
     * 
     * @param xref
     *            the xref of the submitter
     * @return the submitter with the specified xref
     */
    protected Submitter getSubmitter(String xref) {
        Submitter s;
        s = gedcomParser.getGedcom().getSubmitters().get(xref);
        if (s == null) {
            s = new Submitter();
            s.setName("UNSPECIFIED");
            s.setXref(xref);
            gedcomParser.getGedcom().getSubmitters().put(xref, s);
        }
        return s;
    }

    /**
     * Load multiple (continued) lines of text from a string tree node
     * 
     * @param stringTreeWithLinesOfText
     *            the node
     * @param listOfString
     *            the list of string to load into
     * @param element
     *            the parent element to which the <code>listOfString</code> belongs
     */
    protected void loadMultiLinesOfText(StringTree stringTreeWithLinesOfText, List<String> listOfString, AbstractElement element) {
        if (stringTreeWithLinesOfText.getValue() != null) {
            listOfString.add(stringTreeWithLinesOfText.getValue());
        }
        if (stringTreeWithLinesOfText.getChildren() != null) {
            for (StringTree ch : stringTreeWithLinesOfText.getChildren()) {
                if (Tag.CONTINUATION.equalsText(ch.getTag())) {
                    if (ch.getValue() == null) {
                        listOfString.add("");
                    } else {
                        listOfString.add(ch.getValue());
                    }
                } else if (Tag.CONCATENATION.equalsText(ch.getTag())) {
                    // If there's no value to concatenate, ignore it
                    if (ch.getValue() != null) {
                        if (listOfString.isEmpty()) {
                            listOfString.add(ch.getValue());
                        } else {
                            listOfString.set(listOfString.size() - 1, listOfString.get(listOfString.size() - 1) + ch.getValue());
                        }
                    }
                } else {
                    unknownTag(ch, element);
                }
            }
        }
    }

    /**
     * Load multiple (continued) lines of text from a string tree node along with custom facts from that node
     * 
     * @param stringTreeWithLinesOfText
     *            the string tree with lots of lines of text
     * @param multiString
     *            the multi-line object (with custom facts) that we're loading into
     */
    protected void loadMultiStringWithCustomFacts(StringTree stringTreeWithLinesOfText, MultiStringWithCustomFacts multiString) {
        List<String> listOfString = multiString.getLines(true);
        if (stringTreeWithLinesOfText.getValue() != null) {
            listOfString.add(stringTreeWithLinesOfText.getValue());
        }
        if (stringTreeWithLinesOfText.getChildren() != null) {
            for (StringTree ch : stringTreeWithLinesOfText.getChildren()) {
                if (Tag.CONTINUATION.equalsText(ch.getTag())) {
                    if (ch.getValue() == null) {
                        listOfString.add("");
                    } else {
                        listOfString.add(ch.getValue());
                    }
                } else if (Tag.CONCATENATION.equalsText(ch.getTag())) {
                    // If there's no value to concatenate, ignore it
                    if (ch.getValue() != null) {
                        if (listOfString.isEmpty()) {
                            listOfString.add(ch.getValue());
                        } else {
                            listOfString.set(listOfString.size() - 1, listOfString.get(listOfString.size() - 1) + ch.getValue());
                        }
                    }
                } else {
                    unknownTag(ch, multiString);
                }
            }
        }
    }

    /**
     * Helper method to take a string tree and all its children and load them into a StringWithCustomFacts object
     * 
     * @param ch
     *            the string tree
     * @return the constructed {@link StringWithCustomFacts}, with all the {@link CustomFact} objects built from the string tree's
     *         children
     */
    protected StringWithCustomFacts parseStringWithCustomFacts(StringTree ch) {
        StringWithCustomFacts swcf = new StringWithCustomFacts(ch.getValue());
        if (ch.getChildren() != null) {
            for (StringTree gch : ch.getChildren()) {
                CustomFact cf = new CustomFact(gch.getTag());
                swcf.getCustomFacts(true).add(cf);
                cf.setXref(gch.getXref());
                new CustomFactParser(gedcomParser, gch, cf).parse();
            }
        }
        return swcf;
    }

    /**
     * Returns true if the node passed in uses a cross-reference to another node
     * 
     * @param st
     *            the node
     * @return true if and only if the node passed in uses a cross-reference to another node
     */
    protected boolean referencesAnotherNode(StringTree st) {
        if (st.getValue() == null) {
            return false;
        }
        int r1 = st.getValue().indexOf('@');
        if (r1 == -1) {
            return false;
        }
        int r2 = st.getValue().indexOf('@', r1);
        return r2 > -1;
    }

    /**
     * Load all the remaining children of this tag as custom tags
     * 
     * @param st
     *            the string tree we're parsing
     * @param into
     *            what we're parsing all the custom tags into
     */
    protected void remainingChildrenAreCustomTags(StringTree st, HasCustomFacts into) {
        if (st == null || st.getChildren() == null) {
            return;
        }
        for (StringTree ch : st.getChildren()) {
            unknownTag(ch, into);
        }
    }

    /**
     * <p>
     * Default handler for a tag that the parser was not expecting to see.
     * </p>
     * <ul>
     * <li>If custom tags are ignored in the parser (see {@link GedcomParser#isIgnoreCustomTags()}), the custom/unknown tag and all
     * its children will be ignored, regardless of the value of the strict custom tags setting (because it doesn't make sense to be
     * strict about tags that are being ignored).
     * <li>If the tag begins with an underscore, it is a user-defined tag, which is stored in the customFacts collection of the
     * passed in element, and returns.</li>
     * <li>If {@link GedcomParser#isStrictCustomTags()} parsing is turned off (i.e., == false), it is treated as a user-defined tag
     * (despite the lack of beginning underscore) and treated like any other user-defined tag.</li>
     * <li>If {@link GedcomParser#isStrictCustomTags()} parsing is turned on (i.e., == true), it is treated as bad tag and an error
     * is logged in the {@link GedcomParser#getErrors()} collection, and then the tag and its children are ignored.</li>
     * </ul>
     * 
     * @param node
     *            the node containing the unknown tag.
     * @param element
     *            the element that the node is part of, so if it's a custom tag, this unknown tag can be added to this node's
     *            collection of custom tags
     */
    protected void unknownTag(StringTree node, HasCustomFacts element) {
        if (gedcomParser.isIgnoreCustomTags()) {
            return;
        }
        boolean beginsWithUnderscore = node.getTag().length() > 0 && node.getTag().charAt(0) == '_';
        if (beginsWithUnderscore || !gedcomParser.isStrictCustomTags() || gedcomParser.isInsideCustomTag()) {
            CustomFact cf = new CustomFact(node.getTag());
            element.getCustomFacts(true).add(cf);
            cf.setXref(node.getXref());
            cf.setDescription(node.getValue());
            // Save current value
            boolean saveIsInsideCustomTag = gedcomParser.isInsideCustomTag();
            gedcomParser.setInsideCustomTag(true);
            new CustomFactParser(gedcomParser, node, cf).parse();
            // Restore prior value
            gedcomParser.setInsideCustomTag(saveIsInsideCustomTag);
            return;
        }

        StringBuilder sb = new StringBuilder(64); // Min size = 64
        sb.append("Line ").append(node.getLineNum()).append(": Cannot handle tag ");
        sb.append(node.getTag());
        StringTree st = node;
        while (st.getParent() != null) {
            st = st.getParent();
            sb.append(", child of ").append(st.getTag() == null ? null : st.getTag());
            if (st.getXref() != null) {
                sb.append(" ").append(st.getXref());
            }
            sb.append(" on line ").append(st.getLineNum());
        }
        addError(sb.toString());
    }

    /**
     * Parse the string tree passed into the constructor, and load it into the object model
     */
    abstract void parse();

}
