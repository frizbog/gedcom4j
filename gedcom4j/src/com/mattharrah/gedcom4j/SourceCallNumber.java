package com.mattharrah.gedcom4j;

/**
 * A source call number, used in {@link RepositoryCitation} classes.
 * 
 * @author frizbog1
 * 
 */
public class SourceCallNumber {
	/**
	 * The call number. Corresponds to SOURCE_CALL_NUMBER in the Gedcom spec.
	 */
	public String callNumber;
	/**
	 * The media type, corresponds to SOURCE_MEDIA_TYPE in the Gedcom spec
	 */
	public String mediaType;
}
