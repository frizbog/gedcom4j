package org.gedcom4j.io.reader;

/**
 * Test getting listener notifications from {@link AnselReader}. See {@link AbstractReaderListenerTest} for the real
 * code that does the testing.
 * 
 * @author frizbog
 */
public class AnselReaderListenerTest extends AbstractReaderListenerTest {

    /**
     * Constructor
     */
    public AnselReaderListenerTest() {
        super("sample/willis-ansel.ged", 20035, 201);
    }

}
