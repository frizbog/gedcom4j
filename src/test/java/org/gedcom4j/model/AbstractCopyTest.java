package org.gedcom4j.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A base class for the copy constructor tests, to reduce copy-and-pasted code
 * 
 * @author frizbog
 */
public abstract class AbstractCopyTest {

    /**
     * Helper method to get a test address
     * 
     * @return the address
     */
    protected Address getTestAddress() {
        Address result = new Address();
        result.setAddr1(new StringWithCustomTags("123 Main St."));
        result.setCity(new StringWithCustomTags("Anytown"));
        result.setStateProvince(new StringWithCustomTags("ME"));
        result.setCountry(new StringWithCustomTags("USA"));
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
        cd.setDate(new StringWithCustomTags("1 JAN 1980"));
        cd.setTime(new StringWithCustomTags("12:00 AM"));
        Note n = new Note();
        n.getLines(true).add("Note on change date");
        cd.getNotes(true).add(n);
        result.setChangeDate(cd);
        result.getCustomTags(true).add(getTestCustomTags());
        result.setRecIdNumber(new StringWithCustomTags("YYY"));
        result.setXref("@N0001@");
        result.getLines(true).add("Test Note Line 1");
        return result;
    }

}