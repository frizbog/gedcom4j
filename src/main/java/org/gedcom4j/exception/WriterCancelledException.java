package org.gedcom4j.exception;

/**
 * Exception indicating that the file construction and writing operation was cancelled
 * 
 * @author frizbog
 */
public class WriterCancelledException extends GedcomWriterException {

    /**
     * Serial version uid
     */
    private static final long serialVersionUID = -3295979672863551432L;

    /**
     * No-arg constructor
     */
    public WriterCancelledException() {
        super();
    }

    /**
     * Constructor that takes just a message
     * 
     * @param message
     *            the message
     */
    public WriterCancelledException(String message) {
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
    public WriterCancelledException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor that takes just a cause
     * 
     * @param cause
     *            the cause
     */
    public WriterCancelledException(Throwable cause) {
        super(cause);
    }

}
