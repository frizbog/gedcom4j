package com.mattharrah.gedcom4j;

public enum IndividualEventType {
	ADOPTION("ADOP", "Adoption"), BIRTH("BIRT", "Birth"), BAPTISM("BAPM",
			"Baptism"), BAR_MITZVAH("BARM", "Bar Mitzvah"), BAS_MITZVAH("BASM",
			"Bas Miztvah"), BLESSING("BLES", "Blessing"), BURIAL("BURI",
			"Burial"), CENSUS("CENS", "Census"), CHRISTENING("CHR",
			"Christening"), CHRISTENING_ADULT("CHRA", "Christening (Adult)"), CONFIRMATION(
			"CONF", "Confirmation"), CREMATION("CREM", "Cremation"), DEATH(
			"DEAT", "Death"), EMIGRATION("EMIG", "Emigration"), FIRST_COMMUNION(
			"FCOM", "First Communion"), GRADUATION("GRAD", "Graduation"), IMMIGRATION(
			"IMMI", "Immigration"), NATURALIZATION("NATU", "Naturalization"), ORDINATION(
			"ORDN", "Ordination"), RETIREMENT("RETI", "Retirement"), PROBATE(
			"PROB", "Probate"), WILL("WILL", "Will"), EVENT("EVEN", "Event");
	public static IndividualEventType getFromTag(String tag) {
		for (IndividualEventType t : values()) {
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

	private IndividualEventType(String tag, String display) {
		this.tag = tag;
		this.display = display;
	}
}
