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
package org.gedcom4j.writer;

import java.util.Date;

import org.gedcom4j.exception.GedcomWriterVersionDataMismatchException;
import org.gedcom4j.exception.WriterCancelledException;
import org.gedcom4j.factory.FamilyFactory;
import org.gedcom4j.factory.IndividualFactory;
import org.gedcom4j.factory.Sex;
import org.gedcom4j.model.CharacterSet;
import org.gedcom4j.model.Corporation;
import org.gedcom4j.model.FamilyChild;
import org.gedcom4j.model.Gedcom;
import org.gedcom4j.model.GedcomVersion;
import org.gedcom4j.model.Individual;
import org.gedcom4j.model.IndividualAttribute;
import org.gedcom4j.model.IndividualEvent;
import org.gedcom4j.model.Repository;
import org.gedcom4j.model.SourceSystem;
import org.gedcom4j.model.StringWithCustomFacts;
import org.gedcom4j.model.Submitter;
import org.gedcom4j.model.enumerations.IndividualAttributeType;
import org.gedcom4j.model.enumerations.SupportedVersion;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Test for checking version compatibility for {@link GedcomWriter}
 * 
 * @author frizbog
 */
@SuppressWarnings("PMD.TooManyMethods")
public class GedcomWriterVersionCompatibilityTest {

    /**
     * JUnit rule
     */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Class under test
     */
    private GedcomWriter classUnderTest;

    /**
     * Test fixture Gedcom with the data
     */
    private Gedcom g;

    /**
     * Set up before each test
     * 
     * @throws WriterCancelledException
     *             if things get cancelled, which isn't going to happen here
     */
    @Before
    public void setUp() throws WriterCancelledException {
        g = new Gedcom();
        classUnderTest = new GedcomWriter(g);
        classUnderTest.setValidationSuppressed(true);
    }

    /**
     * Test combining GEDCOM 5.5 and Email on the corporation
     * 
     * @throws GedcomWriterVersionDataMismatchException
     *             if there's a version compatibility problem
     */
    @Test
    public void testGedcom55CorpEmails() throws GedcomWriterVersionDataMismatchException {
        GedcomVersion gv = new GedcomVersion();
        gv.setVersionNumber(SupportedVersion.V5_5);
        g.getHeader().setGedcomVersion(gv);

        // Emails on the corporation and Gedcom 5.5 are not a valid combination
        Corporation c = new Corporation();
        c.getEmails(true).add(new StringWithCustomFacts("support@gedcom4j.org"));
        SourceSystem ss = new SourceSystem();
        ss.setCorporation(c);
        g.getHeader().setSourceSystem(ss);

        thrown.expect(GedcomWriterVersionDataMismatchException.class);
        thrown.expectMessage("email");
        classUnderTest.checkVersionCompatibility55();
    }

    /**
     * Test combining GEDCOM 5.5 and Fax Numbers on the corporation
     * 
     * @throws GedcomWriterVersionDataMismatchException
     *             if there's a version compatibility problem
     */
    @Test
    public void testGedcom55CorpFaxNumbers() throws GedcomWriterVersionDataMismatchException {
        GedcomVersion gv = new GedcomVersion();
        gv.setVersionNumber(SupportedVersion.V5_5);
        g.getHeader().setGedcomVersion(gv);

        // Fax Numbers on the corporation and Gedcom 5.5 are not a valid combination
        Corporation c = new Corporation();
        c.getFaxNumbers(true).add(new StringWithCustomFacts("800-555-1212"));
        SourceSystem ss = new SourceSystem();
        ss.setCorporation(c);
        g.getHeader().setSourceSystem(ss);

        thrown.expect(GedcomWriterVersionDataMismatchException.class);
        thrown.expectMessage("fax numbers");
        classUnderTest.checkVersionCompatibility55();
    }

    /**
     * Test combining GEDCOM 5.5 and WWW URLs on the corporation
     * 
     * @throws GedcomWriterVersionDataMismatchException
     *             if there's a version compatibility problem
     */
    @Test
    public void testGedcom55CorpWwwUrls() throws GedcomWriterVersionDataMismatchException {
        GedcomVersion gv = new GedcomVersion();
        gv.setVersionNumber(SupportedVersion.V5_5);
        g.getHeader().setGedcomVersion(gv);

        // WWW URLs on the corporation and Gedcom 5.5 are not a valid combination
        Corporation c = new Corporation();
        c.getWwwUrls(true).add(new StringWithCustomFacts("http://gedcom4j.org"));
        SourceSystem ss = new SourceSystem();
        ss.setCorporation(c);
        g.getHeader().setSourceSystem(ss);

        thrown.expect(GedcomWriterVersionDataMismatchException.class);
        thrown.expectMessage("www urls");
        classUnderTest.checkVersionCompatibility55();
    }

    /**
     * Test family status and GEDCOM 5.5
     * 
     * @throws GedcomWriterVersionDataMismatchException
     *             if there's a version compatibility problem
     */
    @Test
    public void testGedcom55FamilyStatus() throws GedcomWriterVersionDataMismatchException {
        GedcomVersion gv = new GedcomVersion();
        gv.setVersionNumber(SupportedVersion.V5_5);
        g.getHeader().setGedcomVersion(gv);

        Individual father = new IndividualFactory().create(g, "Dad", "Smith", Sex.MALE, (Date) null, null, (Date) null, null);
        Individual mother = new IndividualFactory().create(g, "Mom", "Smith", Sex.MALE, (Date) null, null, (Date) null, null);
        Individual child = new IndividualFactory().create(g, "Kid", "Smith", Sex.MALE, (Date) null, null, (Date) null, null);
        new FamilyFactory().create(g, father, mother, child);

        FamilyChild familyChild = child.getFamiliesWhereChild().get(0);
        familyChild.setStatus("Swell -- but not allowed in GEDCOM 5.5");

        thrown.expect(GedcomWriterVersionDataMismatchException.class);
        thrown.expectMessage("status");
        classUnderTest.checkVersionCompatibility55();
    }

    /**
     * Test combining GEDCOM 5.5 and FACT type individual attributes
     * 
     * @throws GedcomWriterVersionDataMismatchException
     *             if there's a version compatibility problem
     */
    @Test
    public void testGedcom55IndividualAttributeFacts() throws GedcomWriterVersionDataMismatchException {
        GedcomVersion gv = new GedcomVersion();
        gv.setVersionNumber(SupportedVersion.V5_5);
        g.getHeader().setGedcomVersion(gv);

        // Emails on the individual attribute and Gedcom 5.5 are not a valid combination
        IndividualAttribute ia = new IndividualAttribute();
        ia.setType(IndividualAttributeType.FACT);
        Individual i = new Individual();
        i.getAttributes(true).add(ia);
        g.getIndividuals().put("@I1@", i);

        thrown.expect(GedcomWriterVersionDataMismatchException.class);
        thrown.expectMessage("FACT");
        classUnderTest.checkVersionCompatibility55();
    }

    /**
     * Test combining GEDCOM 5.5 and Emails on the individual
     * 
     * @throws GedcomWriterVersionDataMismatchException
     *             if there's a version compatibility problem
     */
    @Test
    public void testGedcom55IndividualEmails() throws GedcomWriterVersionDataMismatchException {
        GedcomVersion gv = new GedcomVersion();
        gv.setVersionNumber(SupportedVersion.V5_5);
        g.getHeader().setGedcomVersion(gv);

        // Emails on the individual and Gedcom 5.5 are not a valid combination
        Individual i = new Individual();
        i.getEmails(true).add(new StringWithCustomFacts("support@gedcom4j.org"));
        g.getIndividuals().put("@I1@", i);

        thrown.expect(GedcomWriterVersionDataMismatchException.class);
        thrown.expectMessage("email");
        classUnderTest.checkVersionCompatibility55();
    }

    /**
     * Test combining GEDCOM 5.5 and emails on the individual event
     * 
     * @throws GedcomWriterVersionDataMismatchException
     *             if there's a version compatibility problem
     */
    @Test
    public void testGedcom55IndividualEventEmails() throws GedcomWriterVersionDataMismatchException {
        GedcomVersion gv = new GedcomVersion();
        gv.setVersionNumber(SupportedVersion.V5_5);
        g.getHeader().setGedcomVersion(gv);

        // Emails on the individual event and Gedcom 5.5 are not a valid combination
        IndividualEvent ie = new IndividualEvent();
        ie.getEmails(true).add(new StringWithCustomFacts("support@gedcom4j.org"));
        Individual i = new Individual();
        i.getEvents(true).add(ie);
        g.getIndividuals().put("@I1@", i);

        thrown.expect(GedcomWriterVersionDataMismatchException.class);
        thrown.expectMessage("email");
        classUnderTest.checkVersionCompatibility55();
    }

    /**
     * Test combining GEDCOM 5.5 and Fax Numbers on the individual event
     * 
     * @throws GedcomWriterVersionDataMismatchException
     *             if there's a version compatibility problem
     */
    @Test
    public void testGedcom55IndividualEventFaxNumbers() throws GedcomWriterVersionDataMismatchException {
        GedcomVersion gv = new GedcomVersion();
        gv.setVersionNumber(SupportedVersion.V5_5);
        g.getHeader().setGedcomVersion(gv);

        // Fax Numbers on the individual event and Gedcom 5.5 are not a valid combination
        IndividualEvent ie = new IndividualEvent();
        ie.getFaxNumbers(true).add(new StringWithCustomFacts("800-555-1212"));
        Individual i = new Individual();
        i.getEvents(true).add(ie);
        g.getIndividuals().put("@I1@", i);

        thrown.expect(GedcomWriterVersionDataMismatchException.class);
        thrown.expectMessage("fax numbers");
        classUnderTest.checkVersionCompatibility55();
    }

    /**
     * Test combining GEDCOM 5.5 and WWW URLs on the individual event
     * 
     * @throws GedcomWriterVersionDataMismatchException
     *             if there's a version compatibility problem
     */
    @Test
    public void testGedcom55IndividualEventWwwUrls() throws GedcomWriterVersionDataMismatchException {
        GedcomVersion gv = new GedcomVersion();
        gv.setVersionNumber(SupportedVersion.V5_5);
        g.getHeader().setGedcomVersion(gv);

        // WWW URLs on the individual event and Gedcom 5.5 are not a valid combination
        IndividualEvent ie = new IndividualEvent();
        ie.getWwwUrls(true).add(new StringWithCustomFacts("http://gedcom4j.org"));
        Individual i = new Individual();
        i.getEvents(true).add(ie);
        g.getIndividuals().put("@I1@", i);

        thrown.expect(GedcomWriterVersionDataMismatchException.class);
        thrown.expectMessage("www urls");
        classUnderTest.checkVersionCompatibility55();
    }

    /**
     * Test combining GEDCOM 5.5 and Fax Numbers on the individual
     * 
     * @throws GedcomWriterVersionDataMismatchException
     *             if there's a version compatibility problem
     */
    @Test
    public void testGedcom55IndividualFaxNumbers() throws GedcomWriterVersionDataMismatchException {
        GedcomVersion gv = new GedcomVersion();
        gv.setVersionNumber(SupportedVersion.V5_5);
        g.getHeader().setGedcomVersion(gv);

        // Fax Numbers on the individual and Gedcom 5.5 are not a valid combination
        Individual i = new Individual();
        i.getFaxNumbers(true).add(new StringWithCustomFacts("800-555-1212"));
        g.getIndividuals().put("@I1@", i);

        thrown.expect(GedcomWriterVersionDataMismatchException.class);
        thrown.expectMessage("fax numbers");
        classUnderTest.checkVersionCompatibility55();
    }

    /**
     * Test combining GEDCOM 5.5 and WWW URLs on the individual
     * 
     * @throws GedcomWriterVersionDataMismatchException
     *             if there's a version compatibility problem
     */
    @Test
    public void testGedcom55IndividualWwwUrls() throws GedcomWriterVersionDataMismatchException {
        GedcomVersion gv = new GedcomVersion();
        gv.setVersionNumber(SupportedVersion.V5_5);
        g.getHeader().setGedcomVersion(gv);

        // WWW URLs on the individual and Gedcom 5.5 are not a valid combination
        Individual i = new Individual();
        i.getWwwUrls(true).add(new StringWithCustomFacts("http://gedcom4j.org"));
        g.getIndividuals().put("@I1@", i);

        thrown.expect(GedcomWriterVersionDataMismatchException.class);
        thrown.expectMessage("www urls");
        classUnderTest.checkVersionCompatibility55();
    }

    /**
     * Test combining GEDCOM 5.5 and initialized collections/fields that should be ok as long as they are empty
     * 
     * @throws GedcomWriterVersionDataMismatchException
     *             if there's a version compatibility problem
     */
    @Test
    public void testGedcom55InitializedButEmptyForbiddenValues() throws GedcomWriterVersionDataMismatchException {
        GedcomVersion gv = new GedcomVersion();
        gv.setVersionNumber(SupportedVersion.V5_5);
        g.getHeader().setGedcomVersion(gv);

        Corporation corp = new Corporation();
        corp.getWwwUrls(true);
        corp.getFaxNumbers(true);
        corp.getEmails(true);
        SourceSystem ss = new SourceSystem();
        ss.setCorporation(corp);
        g.getHeader().setSourceSystem(ss);

        Individual father = new IndividualFactory().create(g, "Dad", "Smith", Sex.MALE, (Date) null, null, (Date) null, null);
        Individual mother = new IndividualFactory().create(g, "Mom", "Smith", Sex.MALE, (Date) null, null, (Date) null, null);
        Individual child = new IndividualFactory().create(g, "Kid", "Smith", Sex.MALE, (Date) null, null, (Date) null, null);
        new FamilyFactory().create(g, father, mother, child);

        father.getWwwUrls(true);
        father.getFaxNumbers(true);
        father.getEmails(true);

        IndividualEvent ie = new IndividualEvent();
        ie.getWwwUrls(true);
        ie.getFaxNumbers(true);
        ie.getEmails(true);
        father.getEvents(true).add(ie);

        Repository r = new Repository();
        r.getWwwUrls(true);
        r.getFaxNumbers(true);
        r.getEmails(true);
        g.getRepositories().put("@R1@", r);

        Submitter s = new Submitter();
        s.getWwwUrls(true);
        s.getFaxNumbers(true);
        s.getEmails(true);
        g.getSubmitters().put("@S1@", s);

        CharacterSet cs = new CharacterSet();
        g.getHeader().setCharacterSet(cs);

        classUnderTest.checkVersionCompatibility55();
    }

    /**
     * Test combining GEDCOM 5.5 and no character set
     * 
     * @throws GedcomWriterVersionDataMismatchException
     *             if there's a version compatibility problem
     */
    @Test
    public void testGedcom55NoCharacterSet() throws GedcomWriterVersionDataMismatchException {
        g.getHeader().setCharacterSet(null);
        GedcomVersion gv = new GedcomVersion();
        gv.setVersionNumber(SupportedVersion.V5_5);
        g.getHeader().setGedcomVersion(gv);

        classUnderTest.checkVersionCompatibility55();
    }

    /**
     * Test combining GEDCOM 5.5 and no character set
     * 
     * @throws GedcomWriterVersionDataMismatchException
     *             if there's a version compatibility problem
     */
    @Test
    public void testGedcom55NoCharacterSetName() throws GedcomWriterVersionDataMismatchException {
        CharacterSet cs = new CharacterSet();
        cs.setCharacterSetName((String) null);
        g.getHeader().setCharacterSet(cs);

        GedcomVersion gv = new GedcomVersion();
        gv.setVersionNumber(SupportedVersion.V5_5);
        g.getHeader().setGedcomVersion(gv);

        classUnderTest.checkVersionCompatibility55();
    }

    /**
     * Test combining GEDCOM 5.5 and Emails on the repository
     * 
     * @throws GedcomWriterVersionDataMismatchException
     *             if there's a version compatibility problem
     */
    @Test
    public void testGedcom55RepositoryEmails() throws GedcomWriterVersionDataMismatchException {
        GedcomVersion gv = new GedcomVersion();
        gv.setVersionNumber(SupportedVersion.V5_5);
        g.getHeader().setGedcomVersion(gv);

        // Emails on the repository and Gedcom 5.5 are not a valid combination
        Repository r = new Repository();
        r.getEmails(true).add(new StringWithCustomFacts("support@gedcom4j.org"));
        g.getRepositories().put("@SUBM1@", r);

        thrown.expect(GedcomWriterVersionDataMismatchException.class);
        thrown.expectMessage("email");
        classUnderTest.checkVersionCompatibility55();
    }

    /**
     * Test combining GEDCOM 5.5 and Fax Numbers on the repository
     * 
     * @throws GedcomWriterVersionDataMismatchException
     *             if there's a version compatibility problem
     */
    @Test
    public void testGedcom55RepositoryFaxNumbers() throws GedcomWriterVersionDataMismatchException {
        GedcomVersion gv = new GedcomVersion();
        gv.setVersionNumber(SupportedVersion.V5_5);
        g.getHeader().setGedcomVersion(gv);

        // Fax Numbers on the repository and Gedcom 5.5 are not a valid combination
        Repository r = new Repository();
        r.getFaxNumbers(true).add(new StringWithCustomFacts("800-555-1212"));
        g.getRepositories().put("@SUBM1@", r);

        thrown.expect(GedcomWriterVersionDataMismatchException.class);
        thrown.expectMessage("fax numbers");
        classUnderTest.checkVersionCompatibility55();
    }

    /**
     * Test combining GEDCOM 5.5 and WWW URLs on the repository
     * 
     * @throws GedcomWriterVersionDataMismatchException
     *             if there's a version compatibility problem
     */
    @Test
    public void testGedcom55RepositoryWwwUrls() throws GedcomWriterVersionDataMismatchException {
        GedcomVersion gv = new GedcomVersion();
        gv.setVersionNumber(SupportedVersion.V5_5);
        g.getHeader().setGedcomVersion(gv);

        // WWW URLs on the repository and Gedcom 5.5 are not a valid combination
        Repository r = new Repository();
        r.getWwwUrls(true).add(new StringWithCustomFacts("http://gedcom4j.org"));
        g.getRepositories().put("@SUBM1@", r);

        thrown.expect(GedcomWriterVersionDataMismatchException.class);
        thrown.expectMessage("www urls");
        classUnderTest.checkVersionCompatibility55();
    }

    /**
     * Test combining GEDCOM 5.5 and emails on the submitter
     * 
     * @throws GedcomWriterVersionDataMismatchException
     *             if there's a version compatibility problem
     */
    @Test
    public void testGedcom55SubmitterEmails() throws GedcomWriterVersionDataMismatchException {
        GedcomVersion gv = new GedcomVersion();
        gv.setVersionNumber(SupportedVersion.V5_5);
        g.getHeader().setGedcomVersion(gv);

        // Emails on the submitter and Gedcom 5.5 are not a valid combination
        Submitter s = new Submitter();
        s.getEmails(true).add(new StringWithCustomFacts("support@gedcom4j.org"));
        g.getSubmitters().put("@SUBM1@", s);

        thrown.expect(GedcomWriterVersionDataMismatchException.class);
        thrown.expectMessage("email");
        classUnderTest.checkVersionCompatibility55();
    }

    /**
     * Test combining GEDCOM 5.5 and Fax Numbers on the submitter
     * 
     * @throws GedcomWriterVersionDataMismatchException
     *             if there's a version compatibility problem
     */
    @Test
    public void testGedcom55SubmitterFaxNumbers() throws GedcomWriterVersionDataMismatchException {
        GedcomVersion gv = new GedcomVersion();
        gv.setVersionNumber(SupportedVersion.V5_5);
        g.getHeader().setGedcomVersion(gv);

        // Fax Numbers on the submitter and Gedcom 5.5 are not a valid combination
        Submitter s = new Submitter();
        s.getFaxNumbers(true).add(new StringWithCustomFacts("800-555-1212"));
        g.getSubmitters().put("@SUBM1@", s);

        thrown.expect(GedcomWriterVersionDataMismatchException.class);
        thrown.expectMessage("fax numbers");
        classUnderTest.checkVersionCompatibility55();
    }

    /**
     * Test combining GEDCOM 5.5 and WWW URLs on the submitter
     * 
     * @throws GedcomWriterVersionDataMismatchException
     *             if there's a version compatibility problem
     */
    @Test
    public void testGedcom55SubmitterWwwUrls() throws GedcomWriterVersionDataMismatchException {
        GedcomVersion gv = new GedcomVersion();
        gv.setVersionNumber(SupportedVersion.V5_5);
        g.getHeader().setGedcomVersion(gv);

        // WWW URLs on the submitter and Gedcom 5.5 are not a valid combination
        Submitter s = new Submitter();
        s.getWwwUrls(true).add(new StringWithCustomFacts("http://gedcom4j.org"));
        g.getSubmitters().put("@SUBM1@", s);

        thrown.expect(GedcomWriterVersionDataMismatchException.class);
        thrown.expectMessage("www urls");
        classUnderTest.checkVersionCompatibility55();
    }

    /**
     * Test combining GEDCOM 5.5 and UTF-8
     * 
     * @throws GedcomWriterVersionDataMismatchException
     *             if there's a version compatibility problem
     */
    @Test
    public void testGedcom55Utf8() throws GedcomWriterVersionDataMismatchException {
        GedcomVersion gv = new GedcomVersion();
        gv.setVersionNumber(SupportedVersion.V5_5);
        g.getHeader().setGedcomVersion(gv);

        // UTF-8 and Gedcom 5.5 are not a valid combination
        CharacterSet cs = new CharacterSet();
        cs.setCharacterSetName("UTF-8");
        g.getHeader().setCharacterSet(cs);

        thrown.expect(GedcomWriterVersionDataMismatchException.class);
        thrown.expectMessage("UTF-8");
        classUnderTest.checkVersionCompatibility55();
    }

}
