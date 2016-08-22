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
 * Utility class for helping with tests
 * 
 * @author frizbog1
 * 
 */
public final class TestHelper {

    /**
     * Factory method to get a minimally populated Gedcom structure for use in tests. Creates a bogus submission and submitter
     * record and makes sure the references that are needed are there.
     * 
     * @return a minimally populated Gedcom with fake data
     */
    public static Gedcom getMinimalGedcom() {
        Gedcom g = new Gedcom();
        g.setSubmission(new Submission("@SUBN0001@"));
        g.getHeader().setSubmission(g.getSubmission());
        Submitter s = new Submitter();
        s.setXref("@SUBM0001@");
        s.setName(new StringWithCustomTags("Joe Tester"));
        g.getSubmitters().put(s.getXref(), s);
        g.getHeader().setSubmitter(s);
        return g;
    }

    /**
     * Private constructor to prevent instantiation and subclassing
     */
    private TestHelper() {
    }
}
