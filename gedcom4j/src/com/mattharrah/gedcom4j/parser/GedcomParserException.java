package com.mattharrah.gedcom4j.parser;

public class GedcomParserException extends Exception {

	public static final long serialVersionUID = 6803960812766915985L;

	public GedcomParserException() {
		super();
	}

	public GedcomParserException(String message, Throwable cause) {
		super(message, cause);
	}

	public GedcomParserException(String message) {
		super(message);
	}

	public GedcomParserException(Throwable cause) {
		super(cause);
	}

}
