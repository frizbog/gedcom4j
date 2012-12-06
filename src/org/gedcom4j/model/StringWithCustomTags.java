package org.gedcom4j.model;

/**
 * Represents a string value from a tag, and allows for user-defined child tags
 * 
 * @author frizbog
 */
public class StringWithCustomTags extends AbstractElement {

    /**
     * The string value itself.
     */
    public String value;

    /**
     * Default constructor
     */
    public StringWithCustomTags() {
        super();
    }

    /**
     * Constructor that takes a String as a parameter
     * 
     * @param string
     *            the string
     */
    public StringWithCustomTags(String string) {
        value = string;
    }

    /**
     * Constructor that takes a StringTree as a parameter
     * 
     * @param s
     *            the stringtree with the value of the string, plus optional custom tags
     */
    public StringWithCustomTags(StringTree s) {
        value = s.value;
        customTags = s.children;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        StringWithCustomTags other = (StringWithCustomTags) obj;
        if (value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!value.equals(other.value)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return value;
    }
}
