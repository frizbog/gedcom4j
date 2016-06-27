package org.gedcom4j.io.reader;

/**
 * Test getting listener notifications from {@link Utf8Reader}. See {@link AbstractReaderListenerTest} for the real code
 * that does the testing.
 * 
 * @author frizbog
 */
public class Utf8ReaderListenerTest extends AbstractReaderListenerTest {

    /**
     * Constructor
     */
    public Utf8ReaderListenerTest() {
        super("sample/willis.ged", 23552, 236);
    }

}
