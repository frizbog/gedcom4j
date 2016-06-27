package org.gedcom4j.io.reader;

/**
 * Test getting listener notifications from {@link AsciiReader}. See {@link AbstractReaderListenerTest} for the real
 * code that does the testing.
 * 
 * @author frizbog
 */
public class AsciiReaderListenerTest extends AbstractReaderListenerTest {

    /**
     * Constructor
     */
    public AsciiReaderListenerTest() {
        super("sample/willis-ascii.ged", 20036, 201);
    }

}
