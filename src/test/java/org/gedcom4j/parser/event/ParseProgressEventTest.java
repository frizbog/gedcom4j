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
package org.gedcom4j.parser.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.gedcom4j.model.Gedcom;
import org.junit.Test;

/**
 * Test for {@link ParseProgressEvent}
 * 
 * @author frizbog
 */
public class ParseProgressEventTest {

    /**
     * Test for {@link ParseProgressEvent}
     */
    @Test
    public void test() {
        Gedcom g = new Gedcom();
        ParseProgressEvent ppe = new ParseProgressEvent(this, g, false, 100);
        assertNotNull(ppe);
        assertEquals(this, ppe.getSource());
        assertFalse(ppe.isComplete());
        assertEquals(100, ppe.getLinesParsed());
        assertEquals("ParseProgressEvent [complete=false, linesParsed=100, familiesProcessed=0, individualsProcessed=0, "
                + "multimediaProcessed=0, notesProcessed=0, repositoriesProcessed=0, sourcesProcessed=0, submittersProcessed=0]",
                ppe.toString());
        assertEquals(0, ppe.getFamiliesProcessed());
        assertEquals(0, ppe.getIndividualsProcessed());
        assertEquals(0, ppe.getMultimediaProcessed());
        assertEquals(0, ppe.getNotesProcessed());
        assertEquals(0, ppe.getRepositoriesProcessed());
        assertEquals(0, ppe.getSourcesProcessed());
        assertEquals(0, ppe.getSubmittersProcessed());

    }

}
