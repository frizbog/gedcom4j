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
package org.gedcom4j.io.reader;

import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

/**
 * Test for the {@link ProgressTrackingInputStream} class
 * 
 * @author frizbog
 */
public class ProgressTrackingInputStreamTest {

    /**
     * Test reading byte by byte
     * 
     * @throws FileNotFoundException
     *             if the file can't be found
     * @throws IOException
     *             if the file can't be read
     */
    @Test
    public void testByteArray() throws FileNotFoundException, IOException {
        try (FileInputStream fis = new FileInputStream("sample/willis.ged");
                ProgressTrackingInputStream ptis = new ProgressTrackingInputStream(fis)) {
            for (int i = 0; i < 100; i++) {
                assertEquals(i * 10, ptis.getBytesRead());
                ptis.read(new byte[10]);
                assertEquals(i * 10 + 10, ptis.getBytesRead());
            }
        }
    }

    /**
     * Test reading byte by byte
     * 
     * @throws FileNotFoundException
     *             if the file can't be found
     * @throws IOException
     *             if the file can't be read
     */
    @Test
    public void testByteByByte() throws FileNotFoundException, IOException {
        try (FileInputStream fis = new FileInputStream("sample/willis.ged");
                ProgressTrackingInputStream ptis = new ProgressTrackingInputStream(fis)) {
            for (int i = 0; i < 100; i++) {
                assertEquals(i, ptis.getBytesRead());
                ptis.read();
                assertEquals(i + 1, ptis.getBytesRead());
            }
        }
    }
}
