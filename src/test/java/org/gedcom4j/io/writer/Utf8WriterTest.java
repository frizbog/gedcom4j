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
package org.gedcom4j.io.writer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import org.gedcom4j.exception.WriterCancelledException;
import org.gedcom4j.model.InMemoryGedcom;
import org.gedcom4j.writer.GedcomWriter;
import org.junit.Test;

/**
 * Test for {@link Utf8Writer}
 * 
 * @author frizbog
 */
public class Utf8WriterTest {

    /**
     * Test writing to a cancelled writer
     * 
     * @throws WriterCancelledException
     *             because the writer is marked as cancelled
     * @throws IOException
     *             if we can't write to the output stream
     */
    @Test(expected = WriterCancelledException.class)
    public void testCancellation() throws WriterCancelledException, IOException {
        try (OutputStream nos = new NullOutputStream()) {
            GedcomWriter gw = new GedcomWriter(new InMemoryGedcom());
            gw.cancel();
            Utf8Writer w = new Utf8Writer(gw);
            w.gedcomLines = new ArrayList<>();
            w.gedcomLines.add("foo");
            w.write(nos);
        }
    }

    /**
     * Test for {@link Utf8Writer#writeLine(java.io.OutputStream, String)}
     * 
     * @throws WriterCancelledException
     *             if the writer is cancelled
     * @throws IOException
     *             if we can't write to the output stream
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testNegative1() throws WriterCancelledException, IOException {
        Utf8Writer w = new Utf8Writer(new GedcomWriter(new InMemoryGedcom()));
        try (OutputStream nos = new NullOutputStream()) {
            w.writeLine(nos, "Foo");
        }
    }

    /**
     * Test for {@link Utf8Writer#writeLineTerminator(java.io.OutputStream)}
     * 
     * @throws WriterCancelledException
     *             if the writer is cancelled
     * @throws IOException
     *             if we can't write to the output stream
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testNegative2() throws WriterCancelledException, IOException {
        Utf8Writer w = new Utf8Writer(new GedcomWriter(new InMemoryGedcom()));
        try (OutputStream nos = new NullOutputStream()) {
            w.writeLineTerminator(nos);
        }
    }
}
