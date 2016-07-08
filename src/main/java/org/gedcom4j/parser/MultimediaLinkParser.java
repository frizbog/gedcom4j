/*
 * Copyright (c) 2009-2016 Matthew R. Harrah
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.gedcom4j.parser;

import java.util.List;

import org.gedcom4j.model.*;

/**
 * @author frizbog
 *
 */
class MultimediaLinkParser extends AbstractParser<List<Multimedia>> {

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
    public MultimediaLinkParser(GedcomParser gedcomParser, StringTree stringTree, List<Multimedia> loadInto) {
        super(gedcomParser, stringTree, loadInto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void parse() {
        Multimedia m;
        if (referencesAnotherNode(stringTree)) {
            m = getMultimedia(stringTree.getValue());
        } else {
            m = new Multimedia();
            loadFileReferences(m, stringTree);
        }
        loadInto.add(m);
    }

    /**
     * Load a single 5.5-style file reference
     * 
     * @param m
     *            The multimedia object to contain the new file reference
     * @param objeChildren
     *            the sub-tags of the OBJE tag
     */
    private void loadFileReferences55(Multimedia m, List<StringTree> objeChildren) {
        FileReference currentFileRef = new FileReference();
        m.getFileReferences(true).add(currentFileRef);
        if (objeChildren != null) {
            for (StringTree ch : objeChildren) {
                if (Tag.FORM.equalsText(ch.getTag())) {
                    currentFileRef.setFormat(new StringWithCustomTags(ch));
                } else if (Tag.TITLE.equalsText(ch.getTag())) {
                    m.setEmbeddedTitle(new StringWithCustomTags(ch));
                } else if (Tag.FILE.equalsText(ch.getTag())) {
                    currentFileRef.setReferenceToFile(new StringWithCustomTags(ch));
                } else if (Tag.NOTE.equalsText(ch.getTag())) {
                    List<Note> notes = m.getNotes(true);
                    new NoteListParser(gedcomParser, ch, notes).parse();
                } else {
                    unknownTag(ch, m);
                }
            }
        }

    }

    /**
     * Load a single file reference, GEDCOM 5.5.1 style
     * 
     * @param m
     *            the multimedia object we're loading into
     * @param fileStringTree
     *            the string tree with the FILE tag
     */
    private void loadFileReference551(Multimedia m, StringTree fileStringTree) {
        FileReference fr = new FileReference();
        m.getFileReferences(true).add(fr);
        fr.setReferenceToFile(new StringWithCustomTags(fileStringTree));
        List<StringTree> fileChildren = fileStringTree.getChildren();
        if (fileChildren != null) {
            int formCount = 0;
            for (StringTree gch : fileChildren) {
                if (Tag.FORM.equalsText(gch.getTag())) {
                    formCount++;
                    fr.setFormat(new StringWithCustomTags(gch.getValue()));
                    List<StringTree> formChildren = gch.getChildren();
                    if (formChildren != null) {
                        for (StringTree ggch : formChildren) {
                            if (Tag.MEDIA.equalsText(ggch.getTag())) {
                                fr.setMediaType(new StringWithCustomTags(ggch));
                            } else {
                                unknownTag(ggch, fr);
                            }
                        }
                    }
                } else {
                    unknownTag(gch, fr);
                }
            }
            if (formCount != 1) {
                addError("Missing or FORM nodes found under FILE node - GEDCOM 5.5.1 standard requires exactly 1 FORM node");
            }
        }
    }

    /**
     * Load all the file references in the current OBJE tag
     * 
     * @param m
     *            the multimedia object being with the reference
     * @param objeStringTree
     *            the OBJE node being parsed
     */
    private void loadFileReferences(Multimedia m, StringTree objeStringTree) {
        int fileTagCount = 0;
        int formTagCount = 0;

        if (objeStringTree.getChildren() != null) {
            for (StringTree ch : objeStringTree.getChildren()) {
                /*
                 * Count up the number of files referenced for this object - GEDCOM 5.5.1 allows multiple, 5.5 only
                 * allows 1
                 */
                if (Tag.FILE.equalsText(ch.getTag())) {
                    fileTagCount++;
                }
                /*
                 * Count the number of formats referenced per file - GEDCOM 5.5.1 has them as children of FILEs (so
                 * should be zero), 5.5 pairs them with the single FILE tag (so should be one)
                 */
                if (Tag.FORM.equalsText(ch.getTag())) {
                    formTagCount++;
                }
            }
        }
        if (g55()) {
            if (fileTagCount > 1) {
                addWarning("GEDCOM version is 5.5, but multiple files referenced in multimedia reference on line " + objeStringTree.getLineNum()
                        + ", which is only allowed in 5.5.1. "
                        + "Data will be loaded, but cannot be written back out unless the GEDCOM version is changed to 5.5.1");
            }
            if (formTagCount == 0) {
                addWarning("GEDCOM version is 5.5, but there is not a FORM tag in the multimedia link on line " + objeStringTree.getLineNum()
                        + ", a scenario which is only allowed in 5.5.1. "
                        + "Data will be loaded, but cannot be written back out unless the GEDCOM version is changed to 5.5.1");
            }
        }
        if (formTagCount > 1) {
            addError("Multiple FORM tags were found for a multimedia file reference at line " + objeStringTree.getLineNum()
                    + " - this is not compliant with any GEDCOM standard - data not loaded");
            return;
        }

        if (fileTagCount > 1 || formTagCount < fileTagCount) {
            loadFileReferences551(m, objeStringTree.getChildren());
        } else {
            loadFileReferences55(m, objeStringTree.getChildren());
        }
    }

    /**
     * Load one or more 5.5.1-style references
     * 
     * @param m
     *            the multimedia object to which we are adding the file references
     * 
     * @param objeChildren
     *            the sub-tags of the OBJE tag
     */
    private void loadFileReferences551(Multimedia m, List<StringTree> objeChildren) {
        if (objeChildren != null) {
            for (StringTree ch : objeChildren) {
                if (Tag.FILE.equalsText(ch.getTag())) {
                    loadFileReference551(m, ch);
                } else if (Tag.TITLE.equalsText(ch.getTag())) {
                    if (m.getFileReferences() != null) {
                        for (FileReference fr : m.getFileReferences()) {
                            fr.setTitle(new StringWithCustomTags(ch.getTag().intern()));
                        }
                    }
                } else if (Tag.NOTE.equalsText(ch.getTag())) {
                    List<Note> notes = m.getNotes(true);
                    new NoteListParser(gedcomParser, ch, notes).parse();
                    if (!g55()) {
                        addWarning("Gedcom version was 5.5.1, but a NOTE was found on a multimedia link on line " + ch.getLineNum()
                                + ", which is no longer supported. "
                                + "Data will be loaded, but cannot be written back out unless the GEDCOM version is changed to 5.5");
                    }
                } else {
                    unknownTag(ch, m);
                }
            }
        }
    }

}
