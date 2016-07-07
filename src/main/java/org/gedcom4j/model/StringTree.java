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
package org.gedcom4j.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.gedcom4j.Options;

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
public class StringTree implements Serializable {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 7858738185646682895L;

    /**
     * All the elements that are child elements of this element
     */
    private List<StringTree> children = getChildren(Options.isCollectionInitializationEnabled());

    /**
     * The ID number of this element
     */
    private String id;

    /**
     * The level of this element
     */
    private int level;

    /**
     * The line number of the GEDCOM from which this element was derived
     */
    private int lineNum;

    /**
     * <p>
     * The element to which this element is a child.
     * </p>
     * <p>
     * <em>Note:</em> Should not be incuded in {@link #equals(Object)} or {@link #hashCode()} implementations because
     * that leads to infinite recursion. See Issue #60.
     * </p>
     */
    private StringTree parent = null;

    /**
     * The tag for this element
     */
    private String tag;

    /**
     * The value for this element (basically everything after the tag)
     */
    private String value;

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
     * Get the children
     * 
     * @return the children
     */
    public List<StringTree> getChildren() {
        return children;
    }

    /**
     * Get the children
     * 
     * @param initializeIfNeeded
     *            initialize the collection, if needed?
     * @return the children
     */
    public List<StringTree> getChildren(boolean initializeIfNeeded) {
        if (initializeIfNeeded && children == null) {
            children = new ArrayList<StringTree>(0);
        }
        return children;
    }

    /**
     * Get the id
     * 
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Get the level
     * 
     * @return the level
     */
    public int getLevel() {
        return level;
    }

    /**
     * Get the lineNum
     * 
     * @return the lineNum
     */
    public int getLineNum() {
        return lineNum;
    }

    /**
     * Get the parent
     * 
     * @return the parent
     */
    public StringTree getParent() {
        return parent;
    }

    /**
     * Get the tag
     * 
     * @return the tag
     */
    public String getTag() {
        return tag;
    }

    /**
     * Get the value
     * 
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (children == null ? 0 : children.hashCode());
        result = prime * result + (id == null ? 0 : id.hashCode());
        result = prime * result + level;
        result = prime * result + lineNum;
        result = prime * result + (tag == null ? 0 : tag.hashCode());
        result = prime * result + (value == null ? 0 : value.hashCode());
        return result;
    }

    /**
     * Set the id
     * 
     * @param id
     *            the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Set the level
     * 
     * @param level
     *            the level to set
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * Set the lineNum
     * 
     * @param lineNum
     *            the lineNum to set
     */
    public void setLineNum(int lineNum) {
        this.lineNum = lineNum;
    }

    /**
     * Set the parent
     * 
     * @param parent
     *            the parent to set
     */
    public void setParent(StringTree parent) {
        this.parent = parent;
    }

    /**
     * Set the tag
     * 
     * @param tag
     *            the tag to set
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * Set the value
     * 
     * @param value
     *            the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Line " + lineNum + ": " + level + (id == null ? "" : " " + id) + " " + (tag == null ? "(null tag)" : tag) + " "
                + (value == null ? "(null value)" : value));
        if (children != null) {
            for (StringTree ch : children) {
                sb.append("\n").append(ch);
            }
        }
        return sb.toString();
    }

}
