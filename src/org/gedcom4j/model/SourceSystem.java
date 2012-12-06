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
package org.gedcom4j.model;

/**
 * A source system. Corresponds to the SOUR structure in HEADER in the GEDCOM file.
 * 
 * @author frizbog1
 * 
 */
public class SourceSystem extends AbstractElement {
    /**
     * The system ID for this source system
     */
    public String systemId;

    /**
     * The version number of this source system
     */
    public StringWithCustomTags versionNum;

    /**
     * The product name for this source system
     */
    public StringWithCustomTags productName;

    /**
     * The corporation that owns this source system
     */
    public Corporation corporation;

    /**
     * Header source data for this source system.
     */
    public HeaderSourceData sourceData;

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
        SourceSystem other = (SourceSystem) obj;
        if (corporation == null) {
            if (other.corporation != null) {
                return false;
            }
        } else if (!corporation.equals(other.corporation)) {
            return false;
        }
        if (productName == null) {
            if (other.productName != null) {
                return false;
            }
        } else if (!productName.equals(other.productName)) {
            return false;
        }
        if (sourceData == null) {
            if (other.sourceData != null) {
                return false;
            }
        } else if (!sourceData.equals(other.sourceData)) {
            return false;
        }
        if (systemId == null) {
            if (other.systemId != null) {
                return false;
            }
        } else if (!systemId.equals(other.systemId)) {
            return false;
        }
        if (versionNum == null) {
            if (other.versionNum != null) {
                return false;
            }
        } else if (!versionNum.equals(other.versionNum)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (corporation == null ? 0 : corporation.hashCode());
        result = prime * result + (productName == null ? 0 : productName.hashCode());
        result = prime * result + (sourceData == null ? 0 : sourceData.hashCode());
        result = prime * result + (systemId == null ? 0 : systemId.hashCode());
        result = prime * result + (versionNum == null ? 0 : versionNum.hashCode());
        return result;
    }
}
