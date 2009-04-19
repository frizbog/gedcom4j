package com.mattharrah.gedcom4j;

public enum FamilyEventType {
	ANNULMENT("ANUL", "Annulment"), CENSUS("CENS", "Census"), DIVORCE("DIV",
			"Divorce"), DIVORCE_FILED("DIVF", "Divorce filed"), ENGAGEMENT(
			"ENGA", "Engagement"), MARRIAGE("MARR", "Marriage"), MARRIAGE_BANNER(
			"MARB", "Marriage banner"), MARRIAGE_CONTRACT("MARC",
			"Marriage contract"), MARRIAGE_LICENSE("MARL", "Marriage license"), MARRIAGE_SETTLEMENT(
			"MARS", "Marriage settlement"), EVENT("EVEN", "Event");

	public static FamilyEventType getFromTag(String tag) {
		for (FamilyEventType t : values()) {
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

	private FamilyEventType(String tag, String display) {
		this.tag = tag;
		this.display = display;
	}

}
