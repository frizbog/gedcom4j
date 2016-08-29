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

import java.util.ArrayList;
import java.util.List;

import org.gedcom4j.Options;

/**
 * A base class for an item that has "addresses" - real addresses, fax and phone numbers, email addresses
 * 
 * @author frizbog
 */
public abstract class AbstractAddressableElement extends AbstractNotesElement implements HasAddresses {

    /**
     * Serial version uid
     */
    private static final long serialVersionUID = -7717631828689707841L;

    /**
     * The address for this repository
     */
    protected Address address;

    /**
     * The emails for this submitter. New for GEDCOM 5.5.1
     */
    protected List<StringWithCustomTags> emails = getEmails(Options.isCollectionInitializationEnabled());

    /**
     * Fax numbers. New for GEDCOM 5.5.1.
     */
    protected List<StringWithCustomTags> faxNumbers = getFaxNumbers(Options.isCollectionInitializationEnabled());

    /**
     * The phone numbers for this submitter
     */
    protected List<StringWithCustomTags> phoneNumbers = getPhoneNumbers(Options.isCollectionInitializationEnabled());

    /**
     * Web URL's. New for GEDCOM 5.5.1.
     */
    protected List<StringWithCustomTags> wwwUrls = getWwwUrls(Options.isCollectionInitializationEnabled());

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof AbstractAddressableElement)) {
            return false;
        }
        AbstractAddressableElement other = (AbstractAddressableElement) obj;
        if (address == null) {
            if (other.address != null) {
                return false;
            }
        } else if (!address.equals(other.address)) {
            return false;
        }
        if (emails == null) {
            if (other.emails != null) {
                return false;
            }
        } else if (!emails.equals(other.emails)) {
            return false;
        }
        if (faxNumbers == null) {
            if (other.faxNumbers != null) {
                return false;
            }
        } else if (!faxNumbers.equals(other.faxNumbers)) {
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
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Address getAddress() {
        return address;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<StringWithCustomTags> getEmails() {
        return emails;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<StringWithCustomTags> getEmails(boolean initializeIfNeeded) {
        if (initializeIfNeeded && emails == null) {
            emails = new ArrayList<>(0);
        }

        return emails;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<StringWithCustomTags> getFaxNumbers() {
        return faxNumbers;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<StringWithCustomTags> getFaxNumbers(boolean initializeIfNeeded) {
        if (initializeIfNeeded && faxNumbers == null) {
            faxNumbers = new ArrayList<>(0);
        }
        return faxNumbers;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<StringWithCustomTags> getPhoneNumbers() {
        return phoneNumbers;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<StringWithCustomTags> getPhoneNumbers(boolean initializeIfNeeded) {
        if (initializeIfNeeded && phoneNumbers == null) {
            phoneNumbers = new ArrayList<>(0);
        }
        return phoneNumbers;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<StringWithCustomTags> getWwwUrls() {
        return wwwUrls;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<StringWithCustomTags> getWwwUrls(boolean initializeIfNeeded) {
        if (initializeIfNeeded && wwwUrls == null) {
            wwwUrls = new ArrayList<>(0);
        }
        return wwwUrls;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((address == null) ? 0 : address.hashCode());
        result = prime * result + ((emails == null) ? 0 : emails.hashCode());
        result = prime * result + ((faxNumbers == null) ? 0 : faxNumbers.hashCode());
        result = prime * result + ((phoneNumbers == null) ? 0 : phoneNumbers.hashCode());
        result = prime * result + ((wwwUrls == null) ? 0 : wwwUrls.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     * Append the address-like fields to the string builder, to assist toString() implementations
     * 
     * @param builder
     *            the string builder
     * @param moreFieldsAfterThis
     *            are there more fields coming after this? This way we know whether to put in a trailing comma.
     */
    protected void appendAddressFields(StringBuilder builder, boolean moreFieldsAfterThis) {
        if (address != null) {
            builder.append("address=");
            builder.append(address);
            builder.append(", ");
        }
        if (emails != null) {
            builder.append("emails=");
            builder.append(emails);
            builder.append(", ");
        }
        if (faxNumbers != null) {
            builder.append("faxNumbers=");
            builder.append(faxNumbers);
            builder.append(", ");
        }
        if (phoneNumbers != null) {
            builder.append("phoneNumbers=");
            builder.append(phoneNumbers);
            builder.append(", ");
        }
        if (wwwUrls != null) {
            builder.append("wwwUrls=");
            builder.append(wwwUrls);
            if (moreFieldsAfterThis) {
                builder.append(", ");
            }
        }
    }

}
