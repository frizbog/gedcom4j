package org.gedcom4j.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class UnsupportedVersionExceptionTest {

    @Test
    public void testUnsupportedVersionException() {
        try {
            throw new UnsupportedVersionException();
        } catch (UnsupportedVersionException e) {
            assertNotNull(e);
            assertNull(e.getCause());
            assertNull(e.getMessage());
        }
    }

    @Test
    public void testUnsupportedVersionExceptionString() {
        try {
            throw new UnsupportedVersionException("Frying Pan");
        } catch (UnsupportedVersionException e) {
            assertNotNull(e);
            assertNull(e.getCause());
            assertEquals("Frying Pan", e.getMessage());
        }
    }

    @Test
    public void testUnsupportedVersionExceptionStringThrowable() {
        try {
            throw new UnsupportedVersionException("Frying Pan", new RuntimeException());
        } catch (UnsupportedVersionException e) {
            assertNotNull(e);
            assertNotNull(e.getCause());
            assertEquals("Frying Pan", e.getMessage());
        }
    }

    @Test
    public void testUnsupportedVersionExceptionThrowable() {
        try {
            throw new UnsupportedVersionException(new RuntimeException());
        } catch (UnsupportedVersionException e) {
            assertNotNull(e);
            assertNotNull(e.getCause());
            assertNotNull(e.getMessage());
        }
    }

}
