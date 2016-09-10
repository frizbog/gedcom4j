package org.gedcom4j.model;

/**
 * A base class for the copy constructor tests, to reduce copy-and-pasted code
 * 
 * @author frizbog
 */
public abstract class AbstractCopyTest {

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

}