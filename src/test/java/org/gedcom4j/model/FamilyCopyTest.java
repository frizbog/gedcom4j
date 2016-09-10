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

import org.junit.Test;

/**
 * Test copy constructor for {@link Family}
 * 
 * @author frizbog
 */
public class FamilyCopyTest extends AbstractCopyTest {

    /**
     * Test copying a null {@link Family}, which should never work
     */
    @SuppressWarnings("unused")
    @Test(expected = NullPointerException.class)
    public void testCopyNull() {
        new Family(null);
    }

    /**
     * Test the simplest possible scenario - copy a new default {@link Family}
     */
    @Test
    public void testSimplestPossible() {
        Family orig = new Family();
        Family copy = new Family(orig);
        assertEquals(orig, copy);
        assertNotSame(orig, copy);
    }

    /**
     * Test with values
     */
    @Test
    public void testWithValues() {
        Family orig = new Family();
        orig.setAutomatedRecordId(new StringWithCustomTags("123"));
        ChangeDate cd = new ChangeDate();
        cd.setTime(new StringWithCustomTags("12:21a"));
        orig.setChangeDate(cd);
        orig.setNumChildren(new StringWithCustomTags("1"));
        Individual h = new Individual();
        h.setXref("@I1@");
        orig.setHusband(h);
        Individual w = new Individual();
        w.setXref("@I2@");
        orig.setWife(w);
        Individual k = new Individual();
        k.setXref("@I3@");
        orig.getChildren(true).add(k);
        orig.setRecFileNumber(new StringWithCustomTags("123"));
        orig.setRestrictionNotice(new StringWithCustomTags("UNRESTRICTED"));
        orig.getCitations(true).add(getTestCitation());
        orig.getCustomTags(true).add(getTestCustomTags());
        FamilyEvent e = new FamilyEvent();
        e.setType(FamilyEventType.ANNULMENT);
        e.setDate(new StringWithCustomTags("10 OCT 1910"));
        e.setDescription(new StringWithCustomTags("Never happened"));
        orig.getEvents(true).add(e);
        LdsSpouseSealing lss = new LdsSpouseSealing();
        lss.setStatus(new StringWithCustomTags("Complete"));
        orig.getLdsSpouseSealings(true).add(lss);
        Multimedia m = new Multimedia();
        m.getBlob(true).add("qwpeoklskfsekrpweoalksfnsleorpwqoewklsfjlskjfpoeriwpeori");
        orig.getMultimedia(true).add(m);
        orig.getNotes(true).add(getTestNote());
        Submitter s = new Submitter();
        s.setName(new StringWithCustomTags("Eloise /King/"));
        orig.getSubmitters(true).add(s);
        UserReference u = new UserReference();
        u.setType(new StringWithCustomTags("Any type you like"));
        orig.getUserReferences(true).add(u);

        Family copy = new Family(orig);
        assertEquals(orig, copy);
        assertNotSame(orig, copy);
        assertEquals(orig.toString(), copy.toString());
    }

}
