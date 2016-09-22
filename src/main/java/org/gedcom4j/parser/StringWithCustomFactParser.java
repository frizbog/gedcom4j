package org.gedcom4j.parser;

import org.gedcom4j.model.HasCustomFacts;
import org.gedcom4j.model.StringTree;
import org.gedcom4j.model.StringWithCustomFacts;

/**
 * @author frizbog
 */
public class StringWithCustomFactParser extends AbstractParser<HasCustomFacts> {

    /**
     * Constructor.
     * 
     * @param gedcomParser
     *            a reference to the root {@link GedcomParser}
     * @param stringTree
     *            {@link StringTree} to be parsed
     */
    public StringWithCustomFactParser(GedcomParser gedcomParser, StringTree st) {
        super(gedcomParser, st, new StringWithCustomFacts(st.getValue()));
    }

    @Override
    void parse() {
    }

}
