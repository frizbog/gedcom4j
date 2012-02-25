/*
 * Copyright (c) 2009-2012 Matthew R. Harrah
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
package com.mattharrah.gedcom4j.parser;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * A structure to hold an element (as string data) from a GEDCOM file in such a
 * way as to preserve and recognize the hierarchical structure of GEDCOMs (based
 * on the level field of each line)
 * </p>
 * <p>
 * This is tree structure represents the parsed fields from a single line of
 * text from the gedcom file, and all the parsed fields of all the child
 * structures as well.
 * </p>
 * <p>
 * This class is used by the parser for temporary storage of the text of the
 * gedcom. The class and its members are deliberately package private so only
 * the parser will reference it.
 * </p>
 * 
 * @author frizbog1
 */
class StringTree {
    /**
     * The level of this element
     */
    int level;
    /**
     * The ID number of this element
     */
    String id;
    /**
     * The tag for this element
     */
    String tag;
    /**
     * The value for this element (basically everything after the tag)
     */
    String value;
    /**
     * All the elements that are child elements of this element
     */
    List<StringTree> children = new ArrayList<StringTree>();
    /**
     * The element to which this element is a child
     */
    StringTree parent = null;
    /**
     * The line number of the GEDCOM from which this element was derived
     */
    int lineNum;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Line " + lineNum + ": " + level
                + (id != null ? " " + id : "") + " " + tag + " " + value);
        for (StringTree ch : children) {
            sb.append("\n").append(ch);
        }
        return sb.toString();
    }

}
