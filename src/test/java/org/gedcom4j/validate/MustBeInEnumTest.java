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
package org.gedcom4j.validate;

import org.gedcom4j.model.LdsBaptismDateStatus;
import org.gedcom4j.model.ModelElement;
import org.junit.Test;

/**
 * A class for testing the {@link AbstractValidator#mustBeInEnumIfSpecified(Class, ModelElement, String)} method
 * 
 * @author frizbog
 */
public class MustBeInEnumTest extends AbstractValidatorTestCase {

    /**
     * A test validator
     */
    private class TestValidator extends AbstractValidator {

        /**
         * Serial Version UID
         */
        private static final long serialVersionUID = -1062408778793170154L;

        /** The thing full of enums. */
        private final ThingFullOfEnums t = new ThingFullOfEnums();

        /**
         * Constructor
         * 
         * @param validator
         *            base validator that tracks issues, etc.
         */
        TestValidator(Validator validator) {
            this.validator = validator;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void validate() {
            mustBeInEnumIfSpecified(LdsBaptismDateStatus.class, t, "baptismDateStatus");
            mustBeInEnumIfSpecified(LdsBaptismDateStatus.class, t, "baptismDateStatusString");
        }
    }

    /**
     * Inner class for testing enums
     * 
     * @author frizbog
     */
    private class ThingFullOfEnums implements ModelElement {
        /**
         * Serial Version UID
         */
        private static final long serialVersionUID = 9144030330693132907L;

        /** The baptism date status. */
        private LdsBaptismDateStatus baptismDateStatus;

        /** The baptism date status string. */
        private String baptismDateStatusString;

        /**
         * Get the baptismDateStatus
         * 
         * @return the baptismDateStatus
         */
        @SuppressWarnings("unused")
        public LdsBaptismDateStatus getBaptismDateStatus() {
            return baptismDateStatus;
        }

        /**
         * Get the baptismDateStatusString
         * 
         * @return the baptismDateStatusString
         */
        @SuppressWarnings("unused")
        public String getBaptismDateStatusString() {
            return baptismDateStatusString;
        }

        /**
         * Set the baptismDateStatus
         * 
         * @param baptismDateStatus
         *            the baptismDateStatus to set
         */
        public void setBaptismDateStatus(LdsBaptismDateStatus baptismDateStatus) {
            this.baptismDateStatus = baptismDateStatus;
        }

        /**
         * Set the baptismDateStatusString
         * 
         * @param baptismDateStatusString
         *            the baptismDateStatusString to set
         */
        public void setBaptismDateStatusString(String baptismDateStatusString) {
            this.baptismDateStatusString = baptismDateStatusString;
        }
    };

    /**
     * Test a good enum (since the compiler prevents bad enums) and a bad string value.
     */
    @Test
    public void testGoodEnumBadString() {
        TestValidator v = new TestValidator(validator);
        v.t.setBaptismDateStatus(LdsBaptismDateStatus.CHILD);
        v.t.setBaptismDateStatusString("No such value in enum");
        v.validate();
        assertFindingsContain(Severity.ERROR, v.t, ProblemCode.ILLEGAL_VALUE, "baptismDateStatusString");
    }

    /**
     * Test with good values
     */
    @Test
    public void testGoodValues() {
        TestValidator v = new TestValidator(validator);
        v.t.baptismDateStatus = LdsBaptismDateStatus.CHILD;
        v.t.baptismDateStatusString = "CHILD";
        v.validate();
        assertNoIssues();
    }

    /**
     * Test nulls.
     */
    @Test
    public void testNulls() {
        TestValidator v = new TestValidator(validator);
        v.t.baptismDateStatus = null;
        v.t.baptismDateStatusString = null;
        v.validate();
        assertNoIssues();
    }

}
