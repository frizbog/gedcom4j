package org.gedcom4j.io.reader;

/**
 * Test getting listener notifications from {@link UnicodeBigEndianReader}. See {@link AbstractReaderListenerTest} for
 * the real code that does the testing.
 * 
 * @author frizbog
 */
public class UnicodeBigEndianReaderListenerTest extends AbstractReaderListenerTest {

    /**
     * Constructor
     */
    public UnicodeBigEndianReaderListenerTest() {
        super("sample/willis-unicode-bigendian.ged", 20036, 201);
    }

}
