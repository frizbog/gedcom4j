package org.gedcom4j.io.reader;

/**
 * Test getting listener notifications from {@link UnicodeLittleEndianReader}. See {@link AbstractReaderListenerTest}
 * for the real code that does the testing.
 * 
 * @author frizbog
 */
public class UnicodeLittleEndianReaderListenerTest extends AbstractReaderListenerTest {

    /**
     * Constructor
     */
    public UnicodeLittleEndianReaderListenerTest() {
        super("sample/willis-unicode-littleendian.ged", 20036, 201);
    }

}
