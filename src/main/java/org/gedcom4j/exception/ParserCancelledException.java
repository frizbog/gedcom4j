package org.gedcom4j.exception;

/**
 * Exception indicating that the file load/parse operation was cancelled
 * 
 * @author frizbog
 */
public class ParserCancelledException extends GedcomParserException {

    /**
     * Serial version uid
     */
    private static final long serialVersionUID = -3295979672863551432L;

    /**
     * No-arg constructor
     */
    public ParserCancelledException() {
        super();
    }

    /**
     * Constructor that takes just a message
     * 
     * @param message
     *            the message
     */
    public ParserCancelledException(String message) {
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
    public ParserCancelledException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor that takes just a cause
     * 
     * @param cause
     *            the cause
     */
    public ParserCancelledException(Throwable cause) {
        super(cause);
    }

}
