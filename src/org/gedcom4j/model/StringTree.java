/*
 * Copyright (c) 2009-2014 Matthew R. Harrah
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
package org.gedcom4j.model;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * A structure to hold an element (as string data) from a GEDCOM file in such a way as to preserve and recognize the
 * hierarchical structure of GEDCOMs (based on the level field of each line)
 * </p>
 * <p>
 * This is tree structure represents the parsed fields from a single line of text from the gedcom file, and all the
 * parsed fields of all the child structures as well.
 * </p>
 * <p>
 * This class is used by the parser for temporary storage of the text of the gedcom. The class and its members are
 * deliberately package private so only the parser will reference it.
 * </p>
 * 
 * @author frizbog1
 */
public class StringTree {
    /**
     * The level of this element
     */
    public int level;

    /**
     * The ID number of this element
     */
    public String id;

    /**
     * The tag for this element
     */
    public String tag;

    /**
     * The value for this element (basically everything after the tag)
     */
    public String value;

    /**
     * All the elements that are child elements of this element
     */
    public List<StringTree> children = new ArrayList<StringTree>();

    /**
     * The element to which this element is a child
     */
    public StringTree parent = null;

    /**
     * The line number of the GEDCOM from which this element was derived
     */
    public int lineNum;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        StringTree other = (StringTree) obj;
        if (children == null) {
            if (other.children != null) {
                return false;
            }
        } else if (!children.equals(other.children)) {
            return false;
        }
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        if (level != other.level) {
            return false;
        }
        if (lineNum != other.lineNum) {
            return false;
        }
        if (parent == null) {
            if (other.parent != null) {
                return false;
            }
        } else if (!parent.equals(other.parent)) {
            return false;
        }
        if (tag == null) {
            if (other.tag != null) {
                return false;
            }
        } else if (!tag.equals(other.tag)) {
            return false;
        }
        if (value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!value.equals(other.value)) {
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((children == null) ? 0 : children.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + level;
        result = prime * result + lineNum;
        result = prime * result + ((parent == null) ? 0 : parent.hashCode());
        result = prime * result + ((tag == null) ? 0 : tag.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Line " + lineNum + ": " + level + (id == null ? "" : " " + id) + " "
                + tag + " " + value);
        for (StringTree ch : children) {
            sb.append("\n").append(ch);
        }
        return sb.toString();
    }

}
