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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

/**
 * @author frizbog1
 * 
 */
public class RepositoryTest {

    /**
     * Test method for {@link org.gedcom4j.model.Repository#equals(java.lang.Object)} .
     */
    @Test
    @SuppressWarnings("PMD.EqualsNull")
    public void testEqualsObject() {
        Repository r1 = new Repository();
        assertEquals(r1, r1);

        Repository r2 = new Repository();
        assertEquals(r1, r2);

        r1.setAddress(new Address());
        assertFalse(r1.equals(r2));
        r2.setAddress(new Address());
        assertEquals(r1, r2);
        r1.setAddress(null);
        assertFalse(r1.equals(r2));
        r2.setAddress(null);
        assertEquals(r1, r2);

        r1.setChangeDate(new ChangeDate());
        assertFalse(r1.equals(r2));
        r2.setChangeDate(new ChangeDate());
        assertEquals(r1, r2);
        r1.setChangeDate(null);
        assertFalse(r1.equals(r2));
        r2.setChangeDate(null);
        assertEquals(r1, r2);

        r1.getEmails(true).add(new StringWithCustomTags("Frying Pan"));
        assertFalse(r1.equals(r2));
        r2.getEmails(true).add(new StringWithCustomTags("Frying Pan"));
        assertEquals(r1, r2);
        r1.getEmails().clear();
        assertFalse(r1.equals(r2));
        r2.getEmails().clear();
        assertEquals(r1, r2);

        r1.setName("Frying Pan");
        assertFalse(r1.equals(r2));
        r2.setName("Frying Pan");
        assertEquals(r1, r2);
        r1.setName((String) null);
        assertFalse(r1.equals(r2));
        r2.setName((String) null);
        assertEquals(r1, r2);

        r1.getNotes(true).add(new Note());
        assertFalse(r1.equals(r2));
        r2.getNotes(true).add(new Note());
        assertEquals(r1, r2);
        r1.getNotes().clear();
        assertFalse(r1.equals(r2));
        r2.getNotes().clear();
        assertEquals(r1, r2);

        r1.getPhoneNumbers(true).add(new StringWithCustomTags("Frying Pan"));
        assertFalse(r1.equals(r2));
        r2.getPhoneNumbers(true).add(new StringWithCustomTags("Frying Pan"));
        assertEquals(r1, r2);
        r1.getPhoneNumbers().clear();
        assertFalse(r1.equals(r2));
        r2.getPhoneNumbers().clear();
        assertEquals(r1, r2);

        r1.setRecIdNumber("Frying Pan");
        assertFalse(r1.equals(r2));
        r2.setRecIdNumber("Frying Pan");
        assertEquals(r1, r2);
        r1.setRecIdNumber((String) null);
        assertFalse(r1.equals(r2));
        r2.setRecIdNumber((String) null);
        assertEquals(r1, r2);

        assertFalse(r1.equals(null));
        assertFalse(r1.equals(this));
    }

    /**
     * Test method for {@link org.gedcom4j.model.Repository#hashCode()}.
     */
    @Test
    public void testHashCode() {
        Repository r1 = new Repository();
        Repository r2 = new Repository();
        assertEquals(r1.hashCode(), r2.hashCode());

        r1.setAddress(new Address());
        assertFalse(r1.hashCode() == r2.hashCode());
        r2.setAddress(new Address());
        assertEquals(r1.hashCode(), r2.hashCode());
        r1.setAddress(null);
        assertFalse(r1.hashCode() == r2.hashCode());
        r2.setAddress(null);
        assertEquals(r1.hashCode(), r2.hashCode());

        r1.setChangeDate(new ChangeDate());
        assertFalse(r1.hashCode() == r2.hashCode());
        r2.setChangeDate(new ChangeDate());
        assertEquals(r1.hashCode(), r2.hashCode());
        r1.setChangeDate(null);
        assertFalse(r1.hashCode() == r2.hashCode());
        r2.setChangeDate(null);
        assertEquals(r1.hashCode(), r2.hashCode());

        r1.getEmails(true).add(new StringWithCustomTags("Frying Pan"));
        assertFalse(r1.hashCode() == r2.hashCode());
        r2.getEmails(true).add(new StringWithCustomTags("Frying Pan"));
        assertEquals(r1.hashCode(), r2.hashCode());
        r1.getEmails().clear();
        assertFalse(r1.hashCode() == r2.hashCode());
        r2.getEmails().clear();
        assertEquals(r1.hashCode(), r2.hashCode());

        r1.setName("Frying Pan");
        assertFalse(r1.hashCode() == r2.hashCode());
        r2.setName("Frying Pan");
        assertEquals(r1.hashCode(), r2.hashCode());
        r1.setName((String) null);
        assertFalse(r1.hashCode() == r2.hashCode());
        r2.setName((String) null);
        assertEquals(r1.hashCode(), r2.hashCode());

        r1.getNotes(true).add(new Note());
        assertFalse(r1.hashCode() == r2.hashCode());
        r2.getNotes(true).add(new Note());
        assertEquals(r1.hashCode(), r2.hashCode());
        r1.getNotes().clear();
        assertFalse(r1.hashCode() == r2.hashCode());
        r2.getNotes().clear();
        assertEquals(r1.hashCode(), r2.hashCode());

        r1.getPhoneNumbers(true).add(new StringWithCustomTags("Frying Pan"));
        assertFalse(r1.hashCode() == r2.hashCode());
        r2.getPhoneNumbers(true).add(new StringWithCustomTags("Frying Pan"));
        assertEquals(r1.hashCode(), r2.hashCode());
        r1.getPhoneNumbers().clear();
        assertFalse(r1.hashCode() == r2.hashCode());
        r2.getPhoneNumbers().clear();
        assertEquals(r1.hashCode(), r2.hashCode());

        r1.setRecIdNumber("Frying Pan");
        assertFalse(r1.hashCode() == r2.hashCode());
        r2.setRecIdNumber("Frying Pan");
        assertEquals(r1.hashCode(), r2.hashCode());
        r1.setRecIdNumber((String) null);
        assertFalse(r1.hashCode() == r2.hashCode());
        r2.setRecIdNumber((String) null);
        assertEquals(r1.hashCode(), r2.hashCode());

        assertFalse(r1.equals(Integer.valueOf(hashCode())));
    }

}
