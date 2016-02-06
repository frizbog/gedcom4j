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

/**
 * <p>
 * Information about the header source data.
 * </p>
 * <p>
 * If instantiating one of these programmatically rather than through parsing an existing GEDCOM file, you will probably
 * want to change the value of the {@link HeaderSourceData#name} field.
 * </p>
 * 
 * @author frizbog1
 * 
 */
public class HeaderSourceData extends AbstractElement {
    /**
     * The name of the source data. This field must be valued to pass validation, so the default value is "UNSPECIFIED".
     */
    public String name = "UNSPECIFIED";

    /**
     * The publish date
     */
    public StringWithCustomTags publishDate;

    /**
     * Copyright information
     */
    public StringWithCustomTags copyright;

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
        HeaderSourceData other = (HeaderSourceData) obj;
        if (copyright == null) {
            if (other.copyright != null) {
                return false;
            }
        } else if (!copyright.equals(other.copyright)) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (publishDate == null) {
            if (other.publishDate != null) {
                return false;
            }
        } else if (!publishDate.equals(other.publishDate)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (copyright == null ? 0 : copyright.hashCode());
        result = prime * result + (name == null ? 0 : name.hashCode());
        result = prime * result + (publishDate == null ? 0 : publishDate.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "HeaderSourceData [" + (name != null ? "name=" + name + ", " : "") + (publishDate != null ? "publishDate=" + publishDate + ", " : "")
                + (copyright != null ? "copyright=" + copyright + ", " : "") + (customTags != null ? "customTags=" + customTags : "") + "]";
    }
}
