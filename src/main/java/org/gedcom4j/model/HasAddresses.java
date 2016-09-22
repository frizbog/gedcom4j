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

import java.util.List;

/**
 * An item that has "addresses" - real addresses, fax and phone numbers, email addresses
 * 
 * @author frizbog
 */
public interface HasAddresses {

    /**
     * Gets the address.
     *
     * @return the address
     */
    Address getAddress();

    /**
     * Gets the emails.
     *
     * @return the emails
     */
    List<StringWithCustomFacts> getEmails();

    /**
     * Get the emails
     * 
     * @param initializeIfNeeded
     *            initialize the collection, if needed?
     * @return the emails
     */
    List<StringWithCustomFacts> getEmails(boolean initializeIfNeeded);

    /**
     * Gets the fax numbers.
     *
     * @return the fax numbers
     */
    List<StringWithCustomFacts> getFaxNumbers();

    /**
     * Get the fax numbers
     * 
     * @param initializeIfNeeded
     *            initialize the collection, if needed?
     * @return the fax numbers
     */
    List<StringWithCustomFacts> getFaxNumbers(boolean initializeIfNeeded);

    /**
     * Gets the phone numbers.
     *
     * @return the phone numbers
     */
    List<StringWithCustomFacts> getPhoneNumbers();

    /**
     * Get the phone numbers
     * 
     * @param initializeIfNeeded
     *            initialize the collection, if needed?
     * @return the phone numbers
     */
    List<StringWithCustomFacts> getPhoneNumbers(boolean initializeIfNeeded);

    /**
     * Gets the www urls.
     *
     * @return the www urls
     */
    List<StringWithCustomFacts> getWwwUrls();

    /**
     * Get the www urls
     * 
     * @param initializeIfNeeded
     *            initialize the collection, if needed?
     * @return the www urls
     */
    List<StringWithCustomFacts> getWwwUrls(boolean initializeIfNeeded);

    /**
     * Sets the address.
     *
     * @param address
     *            the new address
     */
    void setAddress(Address address);

}
