/*
 * Copyright (c) 2009-2016 Matthew R. Harrah
 *
 * MIT License
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package org.gedcom4j.model;

/**
 * <p>
 * A source system. Corresponds to the SOUR structure in HEADER in the GEDCOM file.
 * </p>
 * <p>
 * If instantiating one of these programmatically rather than through parsing an existing GEDCOM file, you will probably
 * want to change the value of the {@link SourceSystem#systemId} field.
 * </p>
 * 
 * @author frizbog1
 * 
 */
public class SourceSystem extends AbstractElement {
    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -1121533380857695596L;

    /**
     * The corporation that owns this source system
     */
    private Corporation corporation;

    /**
     * The product name for this source system
     */
    private StringWithCustomTags productName;

    /**
     * Header source data for this source system.
     */
    private HeaderSourceData sourceData;

    /**
     * The system ID for this source system. This field must be valued to pass validation, so the default value is
     * "UNSPECIFIED".
     */
    private String systemId = "UNSPECIFIED";

    /**
     * The version number of this source system
     */
    private StringWithCustomTags versionNum;

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

    /**
     * Gets the corporation.
     *
     * @return the corporation
     */
    public Corporation getCorporation() {
        return corporation;
    }

    /**
     * Gets the product name.
     *
     * @return the product name
     */
    public StringWithCustomTags getProductName() {
        return productName;
    }

    /**
     * Gets the source data.
     *
     * @return the source data
     */
    public HeaderSourceData getSourceData() {
        return sourceData;
    }

    /**
     * Gets the system id.
     *
     * @return the system id
     */
    public String getSystemId() {
        return systemId;
    }

    /**
     * Gets the version num.
     *
     * @return the version num
     */
    public StringWithCustomTags getVersionNum() {
        return versionNum;
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

    /**
     * Sets the corporation.
     *
     * @param corporation
     *            the new corporation
     */
    public void setCorporation(Corporation corporation) {
        this.corporation = corporation;
    }

    /**
     * Sets the product name.
     *
     * @param productName
     *            the new product name
     */
    public void setProductName(StringWithCustomTags productName) {
        this.productName = productName;
    }

    /**
     * Sets the source data.
     *
     * @param sourceData
     *            the new source data
     */
    public void setSourceData(HeaderSourceData sourceData) {
        this.sourceData = sourceData;
    }

    /**
     * Sets the system id.
     *
     * @param systemId
     *            the new system id
     */
    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    /**
     * Sets the version num.
     *
     * @param versionNum
     *            the new version num
     */
    public void setVersionNum(StringWithCustomTags versionNum) {
        this.versionNum = versionNum;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SourceSystem [");
        if (corporation != null) {
            builder.append("corporation=");
            builder.append(corporation);
            builder.append(", ");
        }
        if (productName != null) {
            builder.append("productName=");
            builder.append(productName);
            builder.append(", ");
        }
        if (sourceData != null) {
            builder.append("sourceData=");
            builder.append(sourceData);
            builder.append(", ");
        }
        if (systemId != null) {
            builder.append("systemId=");
            builder.append(systemId);
            builder.append(", ");
        }
        if (versionNum != null) {
            builder.append("versionNum=");
            builder.append(versionNum);
            builder.append(", ");
        }
        if (customTags != null) {
            builder.append("customTags=");
            builder.append(customTags);
        }
        builder.append("]");
        return builder.toString();
    }
}
