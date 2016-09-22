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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import org.gedcom4j.model.enumerations.SupportedVersion;
import org.junit.Test;

/**
 * Test copy constructor for {@link Header}
 * 
 * @author frizbog
 */
public class HeaderCopyTest extends AbstractCopyTest {

    /**
     * Test copying a null {@link Header}, which should never work
     */
    @SuppressWarnings("unused")
    @Test(expected = NullPointerException.class)
    public void testCopyNull() {
        new Header(null);
    }

    /**
     * Test the simplest possible scenario - copy a new default {@link Header}
     */
    @Test
    public void testSimplestPossible() {
        Header orig = new Header();
        Header copy = new Header(orig);
        assertEquals(orig, copy);
        assertNotSame(orig, copy);
    }

    /**
     * Test with values
     */
    @Test
    public void testWithValues() {
        Header orig = new Header();
        CharacterSet cs = new CharacterSet();
        cs.setCharacterSetName("AA");
        cs.setVersionNum("1");
        orig.setCharacterSet(cs);
        orig.setDate("5 MAY 1905");
        orig.setDestinationSystem("Black Hole");
        orig.setFileName("foo.ged");
        GedcomVersion gv = new GedcomVersion();
        gv.setGedcomForm("XXX");
        gv.setVersionNumber(SupportedVersion.V5_5_1);
        orig.setGedcomVersion(gv);
        orig.setLanguage("Klingon");
        orig.setPlaceHierarchy("Solar System");
        SourceSystem ss = new SourceSystem();
        ss.setProductName("gedcom4j");
        orig.setSourceSystem(ss);
        Submission s = new Submission("@SBM123@");
        s.setRecIdNumber("999");
        orig.setSubmission(s);
        Submitter submitter = new Submitter();
        submitter.setName("Matt /Harrah/");
        orig.setSubmitter(submitter);
        orig.setTime("12:34pm");
        orig.getCopyrightData(true).add("(c) 932 AD");
        orig.getCustomFacts(true).add(getTestCustomFact());
        orig.getNotes(true).add(getTestNote());

        Header copy = new Header(orig);
        assertEquals(orig, copy);
        assertNotSame(orig, copy);
        assertEquals(orig.toString(), copy.toString());
    }

}
