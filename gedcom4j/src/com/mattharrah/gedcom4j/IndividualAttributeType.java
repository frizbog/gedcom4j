package com.mattharrah.gedcom4j;

public enum IndividualAttributeType {
	CASTE_NAME("CAST", "Caste Type"), PHYSICAL_DESCRIPTION("DSCR",
			"Physical Description"), SCHOLASTIC_ACHIEVEMENT("EDUC",
			"Scholastic Achievement"), NATIONAL_ID_NUMBER("IDNO",
			"National ID Number"), NATIONAL_OR_TRIBAL_ORIGIN("NATI",
			"National or Tribal Origin"), COUNT_OF_CHILDREN("NCHI",
			"Number of Children"), COUNT_OF_MARRIAGES("NMR",
			"Number of Marriages"), OCCUPATION("OCCU", "Occupation"), POSSESSIONS(
			"PROP", "Possessions"), RELIGIOUS_AFFILIATION("RELI",
			"Religious Affiliation"), RESIDENCE("RESI", "Residence"), SOCIAL_SECURITY_NUMBER(
			"SSN", "Social Security Number"), NOBILITY_TYPE_TITLE("TITL",
			"Nobility Type Title");

	public static IndividualAttributeType getFromTag(String tag) {
		for (IndividualAttributeType t : values()) {
			if (t.tag.equals(tag)) {
				return t;
			}
		}
		return null;
	}

	public static boolean isValidTag(String tag) {
		return (getFromTag(tag) != null);
	}

	public final String tag;

	public final String display;

	private IndividualAttributeType(String tag, String display) {
		this.tag = tag;
		this.display = display;
	}
}
