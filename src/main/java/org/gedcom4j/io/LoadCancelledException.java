package org.gedcom4j.io;

import org.gedcom4j.parser.GedcomParserException;

/**
 * Exception indicating that the file load/parse operation was cancelled
 * 
 * @author frizbog
 */
public class LoadCancelledException extends GedcomParserException {

    /**
     * Serial version uid
     */
    private static final long serialVersionUID = -3295979672863551432L;

    /**
     * No-arg constructor
     */
    public LoadCancelledException() {
        super();
    }

    /**
     * Constructor that takes just a message
     * 
     * @param message
     *            the message
     */
    public LoadCancelledException(String message) {
        super(message);
    }

    /**
     * Constructor that takes a message and a cause
     * 
     * @param message
     *            the message
     * @param cause
     *            the cause
     */
    public LoadCancelledException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor that takes just a cause
     * 
     * @param cause
     *            the cause
     */
    public LoadCancelledException(Throwable cause) {
        super(cause);
    }

}
