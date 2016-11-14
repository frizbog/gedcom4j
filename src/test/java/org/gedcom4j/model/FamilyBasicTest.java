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
import static org.junit.Assert.assertFalse;

import org.junit.Test;

/**
 * Test for {@link Family}
 * 
 * @author frizbog
 */
public class FamilyBasicTest {

    /**
     * Test for {@link Family#equals(Object)}
     */
    @Test
    @SuppressWarnings({ "checkstyle:MethodLength", "PMD.EqualsNull", "PMD.ExcessiveMethodLength", "PMD.NcssMethodCount" })
    public void testEquals() {
        Family f1 = new Family();
        assertEquals(f1, f1);
        assertFalse(f1.equals(this));
        assertFalse(f1.equals(null));

        Family f2 = new Family();
        assertEquals(f1, f2);

        f2.setAutomatedRecordId("X");
        assertFalse(f1.equals(f2));
        f1.setAutomatedRecordId("X");
        assertEquals(f1, f2);
        f2.setAutomatedRecordId((String) null);
        assertFalse(f1.equals(f2));
        f2.setAutomatedRecordId("X");
        assertEquals(f1, f2);

        f2.setChangeDate(new ChangeDate());
        assertFalse(f1.equals(f2));
        f1.setChangeDate(new ChangeDate());
        assertEquals(f1, f2);
        f2.setChangeDate(null);
        assertFalse(f1.equals(f2));
        f2.setChangeDate(new ChangeDate());
        assertEquals(f1, f2);

        f2.setHusband(new IndividualReference());
        assertFalse(f1.equals(f2));
        f1.setHusband(new IndividualReference());
        assertEquals(f1, f2);
        f2.setHusband(null);
        assertFalse(f1.equals(f2));
        f2.setHusband(new IndividualReference());
        assertEquals(f1, f2);

        f2.setNumChildren("1");
        assertFalse(f1.equals(f2));
        f1.setNumChildren("1");
        assertEquals(f1, f2);
        f2.setNumChildren((String) null);
        assertFalse(f1.equals(f2));
        f2.setNumChildren("1");
        assertEquals(f1, f2);

        f2.setRecFileNumber("2");
        assertFalse(f1.equals(f2));
        f1.setRecFileNumber("2");
        assertEquals(f1, f2);
        f2.setRecFileNumber((String) null);
        assertFalse(f1.equals(f2));
        f2.setRecFileNumber("2");
        assertEquals(f1, f2);

        f2.setRestrictionNotice("privacy");
        assertFalse(f1.equals(f2));
        f1.setRestrictionNotice("privacy");
        assertEquals(f1, f2);
        f2.setRestrictionNotice((String) null);
        assertFalse(f1.equals(f2));
        f2.setRestrictionNotice("privacy");
        assertEquals(f1, f2);

        f2.setWife(new IndividualReference());
        assertFalse(f1.equals(f2));
        f1.setWife(new IndividualReference());
        assertEquals(f1, f2);
        f2.setWife(null);
        assertFalse(f1.equals(f2));
        f2.setWife(new IndividualReference());
        assertEquals(f1, f2);

        f2.setXref("@F1@");
        assertFalse(f1.equals(f2));
        f1.setXref("@F1@");
        assertEquals(f1, f2);
        f2.setXref((String) null);
        assertFalse(f1.equals(f2));
        f2.setXref("@F1@");
        assertEquals(f1, f2);

        f2.getChildren(true);
        assertFalse(f1.equals(f2));
        f1.getChildren(true);
        assertEquals(f1, f2);
        f2.getChildren().add(new IndividualReference());
        assertFalse(f1.equals(f2));
        f1.getChildren().add(new IndividualReference());
        assertEquals(f1, f2);

        f2.getCitations(true);
        assertFalse(f1.equals(f2));
        f1.getCitations(true);
        assertEquals(f1, f2);
        f2.getCitations().add(new CitationWithoutSource());
        assertFalse(f1.equals(f2));
        f1.getCitations().add(new CitationWithoutSource());
        assertEquals(f1, f2);

        f2.getCustomFacts(true);
        assertFalse(f1.equals(f2));
        f1.getCustomFacts(true);
        assertEquals(f1, f2);
        f2.getCustomFacts().add(new CustomFact("_X"));
        assertFalse(f1.equals(f2));
        f1.getCustomFacts().add(new CustomFact("_X"));
        assertEquals(f1, f2);

        f2.getEvents(true);
        assertFalse(f1.equals(f2));
        f1.getEvents(true);
        assertEquals(f1, f2);
        f2.getEvents().add(new FamilyEvent());
        assertFalse(f1.equals(f2));
        f1.getEvents().add(new FamilyEvent());
        assertEquals(f1, f2);

        f2.getLdsSpouseSealings(true);
        assertFalse(f1.equals(f2));
        f1.getLdsSpouseSealings(true);
        assertEquals(f1, f2);
        f2.getLdsSpouseSealings().add(new LdsSpouseSealing());
        assertFalse(f1.equals(f2));
        f1.getLdsSpouseSealings().add(new LdsSpouseSealing());
        assertEquals(f1, f2);

        f2.getMultimedia(true);
        assertFalse(f1.equals(f2));
        f1.getMultimedia(true);
        assertEquals(f1, f2);
        f2.getMultimedia().add(new MultimediaReference());
        assertFalse(f1.equals(f2));
        f1.getMultimedia().add(new MultimediaReference());
        assertEquals(f1, f2);

        f2.getNoteStructures(true);
        assertFalse(f1.equals(f2));
        f1.getNoteStructures(true);
        assertEquals(f1, f2);
        f2.getNoteStructures().add(new NoteStructure());
        assertFalse(f1.equals(f2));
        f1.getNoteStructures().add(new NoteStructure());
        assertEquals(f1, f2);

        f2.getSubmitters(true);
        assertFalse(f1.equals(f2));
        f1.getSubmitters(true);
        assertEquals(f1, f2);
        f2.getSubmitters().add(new SubmitterReference());
        assertFalse(f1.equals(f2));
        f1.getSubmitters().add(new SubmitterReference());
        assertEquals(f1, f2);

        f2.getUserReferences(true);
        assertFalse(f1.equals(f2));
        f1.getUserReferences(true);
        assertEquals(f1, f2);
        f2.getUserReferences().add(new UserReference());
        assertFalse(f1.equals(f2));
        f1.getUserReferences().add(new UserReference());
        assertEquals(f1, f2);
    }

    /**
     * Test for {@link Family#equals(Object)}
     */
    @Test
    @SuppressWarnings({ "checkstyle:MethodLength", "PMD.ExcessiveMethodLength", "PMD.NcssMethodCount" })
    public void testHashCode() {
        Family f1 = new Family();
        assertFalse(f1.hashCode() == hashCode());

        Family f2 = new Family();
        assertEquals(f1.hashCode(), f2.hashCode());

        f2.setAutomatedRecordId("X");
        assertFalse(f1.hashCode() == f2.hashCode());
        f1.setAutomatedRecordId("X");
        assertEquals(f1.hashCode(), f2.hashCode());
        f2.setAutomatedRecordId((String) null);
        assertFalse(f1.hashCode() == f2.hashCode());
        f2.setAutomatedRecordId("X");
        assertEquals(f1.hashCode(), f2.hashCode());

        f2.setChangeDate(new ChangeDate());
        assertFalse(f1.hashCode() == f2.hashCode());
        f1.setChangeDate(new ChangeDate());
        assertEquals(f1.hashCode(), f2.hashCode());
        f2.setChangeDate(null);
        assertFalse(f1.hashCode() == f2.hashCode());
        f2.setChangeDate(new ChangeDate());
        assertEquals(f1.hashCode(), f2.hashCode());

        f2.setHusband(new IndividualReference());
        assertFalse(f1.hashCode() == f2.hashCode());
        f1.setHusband(new IndividualReference());
        assertEquals(f1.hashCode(), f2.hashCode());
        f2.setHusband(null);
        assertFalse(f1.hashCode() == f2.hashCode());
        f2.setHusband(new IndividualReference());
        assertEquals(f1.hashCode(), f2.hashCode());

        f2.setNumChildren("1");
        assertFalse(f1.hashCode() == f2.hashCode());
        f1.setNumChildren("1");
        assertEquals(f1.hashCode(), f2.hashCode());
        f2.setNumChildren((String) null);
        assertFalse(f1.hashCode() == f2.hashCode());
        f2.setNumChildren("1");
        assertEquals(f1.hashCode(), f2.hashCode());

        f2.setRecFileNumber("2");
        assertFalse(f1.hashCode() == f2.hashCode());
        f1.setRecFileNumber("2");
        assertEquals(f1.hashCode(), f2.hashCode());
        f2.setRecFileNumber((String) null);
        assertFalse(f1.hashCode() == f2.hashCode());
        f2.setRecFileNumber("2");
        assertEquals(f1.hashCode(), f2.hashCode());

        f2.setRestrictionNotice("privacy");
        assertFalse(f1.hashCode() == f2.hashCode());
        f1.setRestrictionNotice("privacy");
        assertEquals(f1.hashCode(), f2.hashCode());
        f2.setRestrictionNotice((String) null);
        assertFalse(f1.hashCode() == f2.hashCode());
        f2.setRestrictionNotice("privacy");
        assertEquals(f1.hashCode(), f2.hashCode());

        f2.setWife(new IndividualReference());
        assertFalse(f1.hashCode() == f2.hashCode());
        f1.setWife(new IndividualReference());
        assertEquals(f1.hashCode(), f2.hashCode());
        f2.setWife(null);
        assertFalse(f1.hashCode() == f2.hashCode());
        f2.setWife(new IndividualReference());
        assertEquals(f1.hashCode(), f2.hashCode());

        f2.setXref("@F1@");
        assertFalse(f1.hashCode() == f2.hashCode());
        f1.setXref("@F1@");
        assertEquals(f1.hashCode(), f2.hashCode());
        f2.setXref((String) null);
        assertFalse(f1.hashCode() == f2.hashCode());
        f2.setXref("@F1@");
        assertEquals(f1.hashCode(), f2.hashCode());

        f2.getChildren(true);
        assertFalse(f1.hashCode() == f2.hashCode());
        f1.getChildren(true);
        assertEquals(f1.hashCode(), f2.hashCode());
        f2.getChildren().add(new IndividualReference());
        assertFalse(f1.hashCode() == f2.hashCode());
        f1.getChildren().add(new IndividualReference());
        assertEquals(f1.hashCode(), f2.hashCode());

        f2.getCitations(true);
        assertFalse(f1.hashCode() == f2.hashCode());
        f1.getCitations(true);
        assertEquals(f1.hashCode(), f2.hashCode());
        f2.getCitations().add(new CitationWithoutSource());
        assertFalse(f1.hashCode() == f2.hashCode());
        f1.getCitations().add(new CitationWithoutSource());
        assertEquals(f1.hashCode(), f2.hashCode());

        f2.getCustomFacts(true);
        assertFalse(f1.hashCode() == f2.hashCode());
        f1.getCustomFacts(true);
        assertEquals(f1.hashCode(), f2.hashCode());
        f2.getCustomFacts().add(new CustomFact("_X"));
        assertFalse(f1.hashCode() == f2.hashCode());
        f1.getCustomFacts().add(new CustomFact("_X"));
        assertEquals(f1.hashCode(), f2.hashCode());

        f2.getEvents(true);
        assertFalse(f1.hashCode() == f2.hashCode());
        f1.getEvents(true);
        assertEquals(f1.hashCode(), f2.hashCode());
        f2.getEvents().add(new FamilyEvent());
        assertFalse(f1.hashCode() == f2.hashCode());
        f1.getEvents().add(new FamilyEvent());
        assertEquals(f1.hashCode(), f2.hashCode());

        f2.getLdsSpouseSealings(true);
        assertFalse(f1.hashCode() == f2.hashCode());
        f1.getLdsSpouseSealings(true);
        assertEquals(f1.hashCode(), f2.hashCode());
        f2.getLdsSpouseSealings().add(new LdsSpouseSealing());
        assertFalse(f1.hashCode() == f2.hashCode());
        f1.getLdsSpouseSealings().add(new LdsSpouseSealing());
        assertEquals(f1.hashCode(), f2.hashCode());

        f2.getMultimedia(true);
        assertFalse(f1.hashCode() == f2.hashCode());
        f1.getMultimedia(true);
        assertEquals(f1.hashCode(), f2.hashCode());
        f2.getMultimedia().add(new MultimediaReference());
        assertFalse(f1.hashCode() == f2.hashCode());
        f1.getMultimedia().add(new MultimediaReference());
        assertEquals(f1.hashCode(), f2.hashCode());

        f2.getNoteStructures(true);
        assertFalse(f1.hashCode() == f2.hashCode());
        f1.getNoteStructures(true);
        assertEquals(f1.hashCode(), f2.hashCode());
        f2.getNoteStructures().add(new NoteStructure());
        assertFalse(f1.hashCode() == f2.hashCode());
        f1.getNoteStructures().add(new NoteStructure());
        assertEquals(f1.hashCode(), f2.hashCode());

        f2.getSubmitters(true);
        assertFalse(f1.hashCode() == f2.hashCode());
        f1.getSubmitters(true);
        assertEquals(f1.hashCode(), f2.hashCode());
        f2.getSubmitters().add(new SubmitterReference());
        assertFalse(f1.hashCode() == f2.hashCode());
        f1.getSubmitters().add(new SubmitterReference());
        assertEquals(f1.hashCode(), f2.hashCode());

        f2.getUserReferences(true);
        assertFalse(f1.hashCode() == f2.hashCode());
        f1.getUserReferences(true);
        assertEquals(f1.hashCode(), f2.hashCode());
        f2.getUserReferences().add(new UserReference());
        assertFalse(f1.hashCode() == f2.hashCode());
        f1.getUserReferences().add(new UserReference());
        assertEquals(f1.hashCode(), f2.hashCode());
    }

    /**
     * Test for {@link Family#toString()}
     */
    @Test
    public void testToString() {
        Family f = new Family();
        assertEquals("Family []", f.toString());

        f.setAutomatedRecordId("1");
        f.setChangeDate(new ChangeDate());
        f.setHusband(new IndividualReference());
        f.setNumChildren("1");
        f.setRecFileNumber("A");
        f.setRestrictionNotice("B");
        f.setWife(new IndividualReference());
        f.setXref("@X1@");
        f.getCitations(true).add(new CitationWithoutSource());
        f.getCustomFacts(true).add(new CustomFact("_X"));
        f.getEvents(true).add(new FamilyEvent());
        f.getLdsSpouseSealings(true).add(new LdsSpouseSealing());
        f.getMultimedia(true).add(new MultimediaReference());
        f.getNoteStructures(true).add(new NoteStructure());
        f.getSubmitters(true).add(new SubmitterReference());
        f.getUserReferences(true).add(new UserReference());

        assertEquals("Family [automatedRecordId=1, changeDate=ChangeDate [], citations=[CitationWithoutSource []], "
                + "events=[FamilyEvent []], husband=IndividualReference [], ldsSpouseSealings=[LdsSpouseSealing []], "
                + "multimedia=[MultimediaReference []], noteStructures=[NoteStructure []], numChildren=1, "
                + "recFileNumber=A, restrictionNotice=B, submitters=[SubmitterReference []], "
                + "userReferences=[UserReference []], wife=IndividualReference [], xref=@X1@, "
                + "customFacts=[CustomFact [tag=_X, ]]]", f.toString());
    }

}
