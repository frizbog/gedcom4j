package com.mattharrah.gedcom4j;

public enum LdsIndividualOrdinanceType {
	BAPTISM("BAPL", "LDS Baptism"), CONFIRMATION("CONL", "LDS Confirmation"), ENDOWMENT(
			"ENDL", "LDS Endowment"), CHILD_SEALING("SLGC", "LDS Child Sealing");

	public static LdsIndividualOrdinanceType getFromTag(String tag) {
		for (LdsIndividualOrdinanceType t : values()) {
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

	private LdsIndividualOrdinanceType(String tag, String display) {
		this.tag = tag;
		this.display = display;
	}
}
