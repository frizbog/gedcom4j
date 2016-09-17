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

import java.io.IOException;

import org.gedcom4j.exception.GedcomParserException;
import org.gedcom4j.model.enumerations.IndividualAttributeType;
import org.gedcom4j.model.enumerations.IndividualEventType;
import org.junit.Test;

/**
 * Test copy constructor for {@link Individual}
 * 
 * @author frizbog
 */
public class IndividualCopyTest extends AbstractCopyTest {

    /**
     * Test copying a null {@link Individual}, which should never work
     */
    @SuppressWarnings("unused")
    @Test(expected = NullPointerException.class)
    public void testCopyNull() {
        new Individual(null);
    }

    /**
     * Test the simplest possible scenario - copy a new default {@link Individual}
     */
    @Test
    public void testSimplestPossible() {
        Individual orig = new Individual();
        Individual copy = new Individual(orig);
        assertEquals(orig, copy);
        assertNotSame(orig, copy);
    }

    /**
     * Test with a loaded file
     * 
     * @throws IOException
     *             if the file cannot be read
     * @throws GedcomParserException
     *             if the file cannot be parsed
     */
    @Test
    public void testWithLoadedFile() throws IOException, GedcomParserException {
        Gedcom loadedGedcom = getLoadedGedcom();

        for (Individual original : loadedGedcom.getIndividuals().values()) {
            Individual copy = new Individual(original);
            assertNotSame(original, copy);
            assertEquals(original, copy);
        }
    }

    /**
     * Test with values
     */
    @Test
    public void testWithValues() {
        Individual orig = new Individual();
        orig.setAddress(getTestAddress());
        ChangeDate cd = new ChangeDate();
        cd.setDate("06 JUN 1906");
        orig.setChangeDate(cd);
        orig.setRecIdNumber("321");
        orig.setXref("@I1@");
        orig.getNotes(true).add(getTestNote());
        orig.getFaxNumbers(true).add(new StringWithCustomTags("555-1212"));
        orig.getPhoneNumbers(true).add(new StringWithCustomTags("555-1313"));
        orig.getWwwUrls(true).add(new StringWithCustomTags("www.nowhere.net"));
        orig.getEmails(true).add(new StringWithCustomTags("nobody@nowwhere.net"));
        orig.setAncestralFileNumber("LLL");
        orig.setPermanentRecFileNumber("MMM");
        orig.setRestrictionNotice("RESTRICTED");
        orig.setSex("M");
        orig.getAliases(true).add(new StringWithCustomTags("Grace"));
        Submitter s = new Submitter();
        s.setName("Harry");
        orig.getAncestorInterest(true).add(s);
        Association a = new Association();
        a.setAssociatedEntityType("Fraternal");
        orig.getAssociations(true).add(a);
        IndividualAttribute ia = new IndividualAttribute();
        ia.setType(IndividualAttributeType.FACT);
        ia.setSubType("Favorite color");
        ia.setDescription("Green");
        orig.getAttributes(true).add(ia);
        orig.getCustomTags(true).add(getTestCustomTags());
        orig.getCitations(true).add(getTestCitation());
        orig.getDescendantInterest(true).add(s);
        IndividualEvent e = new IndividualEvent();
        e.setType(IndividualEventType.EVENT);
        e.setSubType("Stubbed toe");
        e.setDate("8 AUG 1908");
        orig.getEvents(true).add(e);
        FamilyChild fc = new FamilyChild();
        orig.getFamiliesWhereChild(true).add(fc);
        fc.setFamily(new Family());
        FamilySpouse fs = new FamilySpouse();
        fs.setFamily(new Family());
        orig.getFamiliesWhereSpouse(true).add(fs);
        LdsIndividualOrdinance lio = new LdsIndividualOrdinance();
        lio.setDate("09 SEP 1909");
        orig.getLdsIndividualOrdinances(true).add(lio);
        Multimedia m = new Multimedia();
        m.setXref("@M1@");
        orig.getMultimedia(true).add(m);
        PersonalName pn = new PersonalName();
        pn.setBasic("Gloria /Gleeson/");
        orig.getNames(true).add(pn);
        orig.getNotes(true).add(getTestNote());
        Submitter sbm = new Submitter();
        sbm.setName("Paul /Paulson/");
        orig.getSubmitters(true).add(sbm);
        UserReference u = new UserReference();
        u.setReferenceNum("12345");
        orig.getUserReferences(true).add(u);

        Individual copy = new Individual(orig);
        assertEquals(orig, copy);
        assertNotSame(orig, copy);
        assertEquals(orig.toString(), copy.toString());
    }

}
