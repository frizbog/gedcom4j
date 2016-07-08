/*
 * Copyright (c) 2009-2016 Matthew R. Harrah
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.gedcom4j.parser;

import java.util.List;

import org.gedcom4j.model.FileReference;
import org.gedcom4j.model.StringTree;
import org.gedcom4j.model.StringWithCustomTags;

/**
 * Parser for GEDCOM 5.5.1 style FILE references
 * 
 * @author frizbog
 */
class FileReference551Parser extends AbstractParser<FileReference> {

    /**
     * Constructor
     * 
     * @param gedcomParser
     *            a reference to the root {@link GedcomParser}
     * @param stringTree
     *            {@link StringTree} to be parsed
     * @param loadInto
     *            the object we are loading data into
     */
    FileReference551Parser(GedcomParser gedcomParser, StringTree stringTree, FileReference loadInto) {
        super(gedcomParser, stringTree, loadInto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void parse() {
        loadInto.setReferenceToFile(new StringWithCustomTags(stringTree));
        List<StringTree> fileChildren = stringTree.getChildren();
        if (fileChildren != null) {
            for (StringTree fileChild : fileChildren) {
                if (Tag.FORM.equalsText(fileChild.getTag())) {
                    loadForm(fileChild);
                } else if (Tag.TITLE.equalsText(fileChild.getTag())) {
                    loadInto.setTitle(new StringWithCustomTags(fileChild));
                } else {
                    unknownTag(fileChild, loadInto);
                }
            }
        }
        if (loadInto.getFormat() == null) {
            addError("FORM tag not found under FILE reference on line " + stringTree.getParent().getLineNum());
        }
    }

    /**
     * Load the form tag and its children
     * 
     * @param form
     *            the form string tree
     */
    private void loadForm(StringTree form) {
        loadInto.setFormat(new StringWithCustomTags(form.getValue()));
        List<StringTree> formChildren = form.getChildren();
        if (formChildren != null) {
            int typeCount = 0;
            for (StringTree formChild : formChildren) {
                if (Tag.TYPE.equalsText(formChild.getTag())) {
                    loadInto.setMediaType(new StringWithCustomTags(formChild));
                    typeCount++;
                } else {
                    unknownTag(formChild, loadInto);
                }
            }
            if (typeCount > 1) {
                addError("TYPE was specified more than once for the FORM tag on line " + form.getLineNum());
            }
        }
    }

}
