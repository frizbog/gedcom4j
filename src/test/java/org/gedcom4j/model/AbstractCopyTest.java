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
package org.gedcom4j.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.parser.GedcomParser;

/**
 * A base class for the copy constructor tests, to reduce copy-and-pasted code
 * 
 * @author frizbog
 */
public abstract class AbstractCopyTest {

    /**
     * Load a gedcom from a file for finding objects to copy
     * 
     * @return the Gedcom read from the sample file
     * 
     * @throws IOException
     *             if the file cannot be read
     * @throws GedcomParserException
     *             if the file cannot be parsed
     */
    protected Gedcom getLoadedGedcom() throws IOException, GedcomParserException {
        GedcomParser gp = new GedcomParser();
        gp.load("sample/willis.ged");
        return gp.getGedcom();
    }

    /**
     * Helper method to get a test address
     * 
     * @return the address
     */
    protected Address getTestAddress() {
        Address result = new Address();
        result.setAddr1("123 Main St.");
        result.setCity("Anytown");
        result.setStateProvince("ME");
        result.setCountry("USA");
        result.getLines(true).add("XXX");
        result.getCustomFacts(true).add(getTestCustomFact());
        return result;
    }

    /**
     * Get a test Citation
     * 
     * @return a test citation
     */
    protected AbstractCitation getTestCitation() {
        CitationWithoutSource result = new CitationWithoutSource();
        result.getCustomFacts(true).add(getTestCustomFact());
        result.getNoteStructures(true).add(getTestNoteStructure());
        List<String> ls = new ArrayList<>();
        ls.add("Foo");
        ls.add("Bar");
        result.getTextFromSource(true).add(ls);
        result.getDescription(true).add("Test Citation");
        return result;
    }

    /**
     * Helper method to get a custom fact representing some custom tags to work with
     * 
     * @return a custom fact
     */
    protected CustomFact getTestCustomFact() {
        CustomFact result = new CustomFact("_HOWDY");
        result.setDescription("Pardners");
        Place p = new Place();
        p.setPlaceName("TV Land");
        result.setPlace(p);
        result.getNoteStructures(true).add(getTestNoteStructure());
        result.setDate("11 SEP 1911");

        CustomFact subFact = new CustomFact("_XXXX");
        subFact.setDescription("More input");
        result.getCustomFacts(true).add(subFact);

        return result;
    }

    /**
     * Helper method to construct a test root-level note record
     * 
     * @return a Note
     */
    protected NoteRecord getTestNoteRecord() {
        NoteRecord result = new NoteRecord("@N0001@");
        ChangeDate cd = new ChangeDate();
        cd.setDate("1 JAN 1980");
        cd.setTime("12:00 AM");
        result.setChangeDate(cd);
        CustomFact n2 = new CustomFact("_QWER");
        result.getCustomFacts(true).add(n2);
        result.setRecIdNumber("YYY");
        result.getLines(true).add("Test Note Line 1");
        return result;
    }

    /**
     * Helper method to construct a test note structure (inline note)
     * 
     * @return the note structure
     */
    protected NoteStructure getTestNoteStructure() {
        NoteStructure result = new NoteStructure();
        result.getLines(true).add("This is a test");
        return result;
    }

}