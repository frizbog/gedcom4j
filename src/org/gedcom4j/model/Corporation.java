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
 * Represents a Corporation.
 * </p>
 * 
 * <p>
 * If instantiating one of these programmatically rather than through parsing an
 * existing GEDCOM file, you will probably want to change the value of the
 * {@link Corporation#businessName} field.
 * </p>
 * 
 * @author frizbog1
 * 
 */
public class Corporation extends AbstractElement {
    /**
     * The business name. This field must be valued to pass validation, so the
     * default value is "UNSPECIFIED".
     */
    public String businessName = "UNSPECIFIED";

    /**
     * The address
     */
    public Address address;

    /**
     * The phone numbers
     */
    public List<StringWithCustomTags> phoneNumbers = new ArrayList<StringWithCustomTags>();

    /**
     * Web URL's. New for GEDCOM 5.5.1.
     */
    public List<StringWithCustomTags> wwwUrls = new ArrayList<StringWithCustomTags>();

    /**
     * Fax numbers. New for GEDCOM 5.5.1.
     */
    public List<StringWithCustomTags> faxNumbers = new ArrayList<StringWithCustomTags>();

    /**
     * The emails for this submitter. New for GEDCOM 5.5.1
     */
    public List<StringWithCustomTags> emails = new ArrayList<StringWithCustomTags>();

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
        Corporation other = (Corporation) obj;
        if (address == null) {
            if (other.address != null) {
                return false;
            }
        } else if (!address.equals(other.address)) {
            return false;
        }
        if (businessName == null) {
            if (other.businessName != null) {
                return false;
            }
        } else if (!businessName.equals(other.businessName)) {
            return false;
        }
        if (phoneNumbers == null) {
            if (other.phoneNumbers != null) {
                return false;
            }
        } else if (!phoneNumbers.equals(other.phoneNumbers)) {
            return false;
        }
        if (wwwUrls == null) {
            if (other.wwwUrls != null) {
                return false;
            }
        } else if (!wwwUrls.equals(other.wwwUrls)) {
            return false;
        }
        if (faxNumbers == null) {
            if (other.faxNumbers != null) {
                return false;
            }
        } else if (!faxNumbers.equals(other.faxNumbers)) {
            return false;
        }
        if (emails == null) {
            if (other.emails != null) {
                return false;
            }
        } else if (!emails.equals(other.emails)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (address == null ? 0 : address.hashCode());
        result = prime * result + (businessName == null ? 0 : businessName.hashCode());
        result = prime * result + (phoneNumbers == null ? 0 : phoneNumbers.hashCode());
        result = prime * result + (faxNumbers == null ? 0 : faxNumbers.hashCode());
        result = prime * result + (wwwUrls == null ? 0 : wwwUrls.hashCode());
        result = prime * result + (emails == null ? 0 : emails.hashCode());

        return result;
    }

}
