package org.gedcom4j.writer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * Test for {@link GedcomWriterVersionDataMismatchException}
 * 
 * @author frizbog1
 */
public class GedcomWriterVersionDataMismatchExceptionTest {

    /**
     * Test no arg constructor
     */
    @Test
    public void testGedcomWriterVersionDataMismatchException() {
        try {
            throw new GedcomWriterVersionDataMismatchException();
        } catch (GedcomWriterVersionDataMismatchException e) {
            assertNotNull(e);
            assertNull(e.getCause());
            assertNull(e.getMessage());
        }
    }

    /**
     * Test single string constructor
     */
    @Test
    public void testGedcomWriterVersionDataMismatchExceptionString() {
        try {
            throw new GedcomWriterVersionDataMismatchException("Foo");
        } catch (GedcomWriterVersionDataMismatchException e) {
            assertNotNull(e);
            assertNull(e.getCause());
            assertEquals("Foo", e.getMessage());
        }
    }

    /**
     * Test constructor that takes string and throwable
     */
    @Test
    public void testGedcomWriterVersionDataMismatchExceptionStringThrowable() {
        try {
            throw new GedcomWriterVersionDataMismatchException("Foo", new RuntimeException());
        } catch (GedcomWriterVersionDataMismatchException e) {
            assertNotNull(e);
            assertNotNull(e.getCause());
            assertEquals("Foo", e.getMessage());
        }
    }

    /**
     * Test constructor that takes throwable
     */
    @Test
    public void testGedcomWriterVersionDataMismatchExceptionThrowable() {
        try {
            throw new GedcomWriterVersionDataMismatchException(new RuntimeException());
        } catch (GedcomWriterVersionDataMismatchException e) {
            assertNotNull(e);
            assertNotNull(e.getCause());
            assertNotNull(e.getMessage());
        }
    }

}
