package org.gedcom4j.io;

import java.io.IOException;

import org.gedcom4j.parser.GedcomParser;
import org.gedcom4j.parser.GedcomParserException;
import org.junit.Test;

import javafx.fxml.LoadException;

/**
 * Test getting listener notifications from {@link AnselReader}
 * 
 * @author frizbog
 */
public class AnselReaderCancellationTest implements FileProgressListener {

    /**
     * The number of event notifications received
     */
    private int eventCount;

    /**
     * The last event received
     */
    private FileProgressEvent lastEvent;

    /**
     * GedcomParser test fixture
     */
    private final GedcomParser gp = new GedcomParser();

    @Override
    public void progressNotification(FileProgressEvent e) {
        eventCount++;
        lastEvent = e;
        if (eventCount >= 100) {
            gp.cancel();
        }
    }

    /**
     * Test when you've registered as a listener - cancel after 100 notifications
     * 
     * @throws IOException
     *             if the file can't be loaded
     * @throws GedcomParserException
     *             if the file can't be parsed
     */
    @Test(expected = LoadException.class)
    public void testRegistered() throws IOException, GedcomParserException {
        eventCount = 0;
        gp.registerObserver(this);
        gp.load("sample/willis-ansel.ged");
    }

}
