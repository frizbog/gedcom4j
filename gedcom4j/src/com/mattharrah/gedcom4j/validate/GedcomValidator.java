package com.mattharrah.gedcom4j.validate;

import com.mattharrah.gedcom4j.Gedcom;

/**
 * A class to validate the contents of a {@link Gedcom} structure
 * 
 * @author frizbog1
 */
public class GedcomValidator {

	/**
	 * The gedcom structure being validated
	 */
	private Gedcom gedcom = null;

	/**
	 * Constructor
	 * 
	 * @param gedcom
	 *            the gedcom structure being validated
	 */
	public GedcomValidator(Gedcom gedcom) {
		this.gedcom = gedcom;
	}

	/**
	 * Validate the gedcom file
	 * 
	 * @throws GedcomValidationException
	 */
	public void validate() throws GedcomValidationException {
		if (gedcom == null) {
			throw new GedcomValidationException(
			        "Gedcom structure is null - nothing to validate");
		}
	}

}
