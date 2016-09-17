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
        result.getCustomTags(true).add(getTestCustomTags());
        return result;
    }

    /**
     * Get a test Citation
     * 
     * @return a test citation
     */
    protected AbstractCitation getTestCitation() {
        CitationWithoutSource result = new CitationWithoutSource();
        result.getCustomTags(true).add(getTestCustomTags());
        result.getNotes(true).add(getTestNote());
        List<String> ls = new ArrayList<>();
        ls.add("Foo");
        ls.add("Bar");
        result.getTextFromSource(true).add(ls);
        result.getDescription(true).add("Test Citation");
        return result;
    }

    /**
     * Helper method to get some custom tags to work with
     * 
     * @return custom tags
     */
    protected StringTree getTestCustomTags() {
        StringTree ct = new StringTree();
        ct.setLevel(5);
        ct.setLineNum(60);
        ct.setTag("_HOWDY");
        ct.setValue("Pardners");

        StringTree ct2 = new StringTree();
        ct.getChildren(true).add(ct2);
        ct2.setParent(ct);
        ct2.setLevel(6);
        ct2.setLineNum(61);
        ct2.setTag("_XXXX");
        ct2.setValue("Y");
        return ct;
    }

    /**
     * Helper method to construct a test note
     * 
     * @return a Note
     */
    protected Note getTestNote() {
        Note result = new Note();
        ChangeDate cd = new ChangeDate();
        cd.setDate("1 JAN 1980");
        cd.setTime("12:00 AM");
        Note n = new Note();
        n.getLines(true).add("Note on change date");
        cd.getNotes(true).add(n);
        result.setChangeDate(cd);
        result.getCustomTags(true).add(getTestCustomTags());
        result.setRecIdNumber("YYY");
        result.setXref("@N0001@");
        result.getLines(true).add("Test Note Line 1");
        return result;
    }

}