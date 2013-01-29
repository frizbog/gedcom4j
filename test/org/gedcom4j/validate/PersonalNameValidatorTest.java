package org.gedcom4j.validate;

import java.util.ArrayList;

import org.gedcom4j.model.Individual;
import org.gedcom4j.model.Note;
import org.gedcom4j.model.PersonalName;
import org.gedcom4j.model.StringWithCustomTags;
import org.gedcom4j.model.TestHelper;
import org.junit.Before;
import org.junit.Test;

public class PersonalNameValidatorTest extends AbstractValidatorTestCase {

    /**
     * Test fixture - the personal name being tested
     */
    private PersonalName pn;
    /**
     * The individual whose name(s) we are working with
     */
    private Individual ind;

    /*
     * (non-Javadoc)
     * 
     * @see org.gedcom4j.validate.AbstractValidatorTestCase#setUp()
     */
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        gedcom = TestHelper.getMinimalGedcom();
        rootValidator.gedcom = gedcom;
        rootValidator.autorepair = false;

        ind = new Individual();
        ind.xref = "@I00001@";
        gedcom.individuals.put(ind.xref, ind);

        pn = new PersonalName();
        ind.names.add(pn);
        pn.basic = "Joe /Schmo/";
        rootValidator.validate();
        assertNoIssues();
    }

    /**
     * Test for when basic name is blank or null
     */
    @Test
    public void testBasic() {
        pn.basic = null;
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "basic", "name", "required");

        pn.basic = "";
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "basic", "name", "required");

        pn.basic = "       "; // whitespace gets trimmed
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "basic", "name", "required");

        pn.basic = "Joe /Schmo/";
        rootValidator.validate();
        assertNoIssues();
    }

    /**
     * Test for when given name is null
     */
    @Test
    public void testGivenName() {
        pn.givenName = new StringWithCustomTags((String) null);
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "given name", "no value");

        pn.givenName = new StringWithCustomTags("");
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "given name", "no value");

        pn.givenName = new StringWithCustomTags("Fred");
        rootValidator.validate();
        assertNoIssues();
    }

    /**
     * Test for when nickname is null
     */
    @Test
    public void testNickname() {
        pn.nickname = new StringWithCustomTags((String) null);
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "nickname", "no value");

        pn.nickname = new StringWithCustomTags("");
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "nickname", "no value");

        pn.nickname = new StringWithCustomTags("Bubba");
        rootValidator.validate();
        assertNoIssues();
    }

    /**
     * Test for when surname prefix is null
     */
    @Test
    public void testNotes() {
        pn.notes = null;
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "notes", "null");

        pn.notes = new ArrayList<Note>();
        rootValidator.validate();
        assertNoIssues();
    }

    /**
     * Test for when name is null
     */
    @Test
    public void testNullNameObject() {

        ind.names.add(null);
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "name", "null");
    }

    /**
     * Test for when prefix is null
     */
    @Test
    public void testPrefix() {
        pn.prefix = new StringWithCustomTags((String) null);
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "prefix", "no value");

        pn.prefix = new StringWithCustomTags("");
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "prefix", "no value");

        pn.prefix = new StringWithCustomTags("Mr.");
        rootValidator.validate();
        assertNoIssues();
    }

    /**
     * Test for when suffix is null
     */
    @Test
    public void testSuffix() {
        pn.suffix = new StringWithCustomTags((String) null);
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "suffix", "no value");

        pn.suffix = new StringWithCustomTags("");
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "suffix", "no value");

        pn.suffix = new StringWithCustomTags("Jr.");
        rootValidator.validate();
        assertNoIssues();
    }

    /**
     * Test for when surname is null
     */
    @Test
    public void testSurname() {
        pn.surname = new StringWithCustomTags((String) null);
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "surname", "no value");

        pn.surname = new StringWithCustomTags("");
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "surname", "no value");

        pn.surname = new StringWithCustomTags("Johnson");
        rootValidator.validate();
        assertNoIssues();
    }

    /**
     * Test for when surname prefix is null
     */
    @Test
    public void testSurnamePrefix() {
        pn.surnamePrefix = new StringWithCustomTags((String) null);
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "surname prefix", "no value");

        pn.surnamePrefix = new StringWithCustomTags("");
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "surname prefix", "no value");

        pn.surnamePrefix = new StringWithCustomTags("van");
        rootValidator.validate();
        assertNoIssues();
    }

}
