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
    public void testEqualsObject() {
        Repository r1 = new Repository();
        assertEquals(r1, r1);

        Repository r2 = new Repository();
        assertEquals(r1, r2);

        r1.address = new Address();
        assertFalse(r1.equals(r2));
        r2.address = new Address();
        assertEquals(r1, r2);
        r1.address = null;
        assertFalse(r1.equals(r2));
        r2.address = null;
        assertEquals(r1, r2);

        r1.changeDate = new ChangeDate();
        assertFalse(r1.equals(r2));
        r2.changeDate = new ChangeDate();
        assertEquals(r1, r2);
        r1.changeDate = null;
        assertFalse(r1.equals(r2));
        r2.changeDate = null;
        assertEquals(r1, r2);

        r1.emails.add(new StringWithCustomTags("Frying Pan"));
        assertFalse(r1.equals(r2));
        r2.emails.add(new StringWithCustomTags("Frying Pan"));
        assertEquals(r1, r2);
        r1.emails = null;
        assertFalse(r1.equals(r2));
        r2.emails = null;
        assertEquals(r1, r2);

        r1.name = new StringWithCustomTags("Frying Pan");
        assertFalse(r1.equals(r2));
        r2.name = new StringWithCustomTags("Frying Pan");
        assertEquals(r1, r2);
        r1.name = null;
        assertFalse(r1.equals(r2));
        r2.name = null;
        assertEquals(r1, r2);

        r1.notes.add(new Note());
        assertFalse(r1.equals(r2));
        r2.notes.add(new Note());
        assertEquals(r1, r2);
        r1.notes = null;
        assertFalse(r1.equals(r2));
        r2.notes = null;
        assertEquals(r1, r2);

        r1.phoneNumbers.add(new StringWithCustomTags("Frying Pan"));
        assertFalse(r1.equals(r2));
        r2.phoneNumbers.add(new StringWithCustomTags("Frying Pan"));
        assertEquals(r1, r2);
        r1.phoneNumbers = null;
        assertFalse(r1.equals(r2));
        r2.phoneNumbers = null;
        assertEquals(r1, r2);

        r1.recIdNumber = new StringWithCustomTags("Frying Pan");
        assertFalse(r1.equals(r2));
        r2.recIdNumber = new StringWithCustomTags("Frying Pan");
        assertEquals(r1, r2);
        r1.recIdNumber = null;
        assertFalse(r1.equals(r2));
        r2.recIdNumber = null;
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

        r1.address = new Address();
        assertFalse(r1.hashCode() == r2.hashCode());
        r2.address = new Address();
        assertEquals(r1.hashCode(), r2.hashCode());
        r1.address = null;
        assertFalse(r1.hashCode() == r2.hashCode());
        r2.address = null;
        assertEquals(r1.hashCode(), r2.hashCode());

        r1.changeDate = new ChangeDate();
        assertFalse(r1.hashCode() == r2.hashCode());
        r2.changeDate = new ChangeDate();
        assertEquals(r1.hashCode(), r2.hashCode());
        r1.changeDate = null;
        assertFalse(r1.hashCode() == r2.hashCode());
        r2.changeDate = null;
        assertEquals(r1.hashCode(), r2.hashCode());

        r1.emails.add(new StringWithCustomTags("Frying Pan"));
        assertFalse(r1.hashCode() == r2.hashCode());
        r2.emails.add(new StringWithCustomTags("Frying Pan"));
        assertEquals(r1.hashCode(), r2.hashCode());
        r1.emails = null;
        assertFalse(r1.hashCode() == r2.hashCode());
        r2.emails = null;
        assertEquals(r1.hashCode(), r2.hashCode());

        r1.name = new StringWithCustomTags("Frying Pan");
        assertFalse(r1.hashCode() == r2.hashCode());
        r2.name = new StringWithCustomTags("Frying Pan");
        assertEquals(r1.hashCode(), r2.hashCode());
        r1.name = null;
        assertFalse(r1.hashCode() == r2.hashCode());
        r2.name = null;
        assertEquals(r1.hashCode(), r2.hashCode());

        r1.notes.add(new Note());
        assertFalse(r1.hashCode() == r2.hashCode());
        r2.notes.add(new Note());
        assertEquals(r1.hashCode(), r2.hashCode());
        r1.notes = null;
        assertFalse(r1.hashCode() == r2.hashCode());
        r2.notes = null;
        assertEquals(r1.hashCode(), r2.hashCode());

        r1.phoneNumbers.add(new StringWithCustomTags("Frying Pan"));
        assertFalse(r1.hashCode() == r2.hashCode());
        r2.phoneNumbers.add(new StringWithCustomTags("Frying Pan"));
        assertEquals(r1.hashCode(), r2.hashCode());
        r1.phoneNumbers = null;
        assertFalse(r1.hashCode() == r2.hashCode());
        r2.phoneNumbers = null;
        assertEquals(r1.hashCode(), r2.hashCode());

        r1.recIdNumber = new StringWithCustomTags("Frying Pan");
        assertFalse(r1.hashCode() == r2.hashCode());
        r2.recIdNumber = new StringWithCustomTags("Frying Pan");
        assertEquals(r1.hashCode(), r2.hashCode());
        r1.recIdNumber = null;
        assertFalse(r1.hashCode() == r2.hashCode());
        r2.recIdNumber = null;
        assertEquals(r1.hashCode(), r2.hashCode());

        assertFalse(r1.equals(Integer.valueOf(hashCode())));
    }

}
