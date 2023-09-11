package se.metria.matdatabas.service.systemlogg.handelse;

public enum Handelse {
	USER_LOGGED_IN(HandelseTyp.INLOGGNING, "Inloggning"),
	USER_CREATED(HandelseTyp.ANVANDARE, "Ny användare #%d upplagd med behörighet: %s"),
	USER_MODIFIED(HandelseTyp.ANVANDARE, "Ändrade inställningar för användare #%d med behörighet: %s. Inställningar: %s"),
	USER_REMOVED(HandelseTyp.ANVANDARE, "Tog bort användare #%d med behörighet: %s"),
	USER_ANONYMIZED(HandelseTyp.ANVANDARE, "Anonymiserade användare #%d med behörighet: %s"),
	MATOBJEKT_CREATED(HandelseTyp.MATNINGSTYP, "Nytt mätobjekt #%d '%s' skapat"),
	MATOBJEKT_MODIFIED(HandelseTyp.MATNINGSTYP, "Inställningar ändrade för mätobjekt #%d '%s': %s"),
	MATOBJEKT_REMOVED(HandelseTyp.MATNINGSTYP, "Mätobjekt #%d '%s' raderades"),
	MATOBJEKTGRUPP_CREATED(HandelseTyp.MATNINGSTYP, "Ny mätobjektgrupp #%d '%s' skapad"),
	MATOBJEKTGRUPP_MODIFIED(HandelseTyp.MATNINGSTYP, "Inställningar ändrade för mätobjektgrupp #%d '%s': %s"),
	MATOBJEKTGRUPP_REMOVED(HandelseTyp.MATNINGSTYP, "Mätobjektgrupp #%d '%s' raderades"),
	MATRUNDA_CREATED(HandelseTyp.MATNINGSTYP, "Ny mätrunda #%d '%s' skapad"),
	MATRUNDA_MODIFIED(HandelseTyp.MATNINGSTYP, "Inställningar ändrade för mätrunda #%d '%s': %s"),
	MATRUNDA_REMOVED(HandelseTyp.MATNINGSTYP, "Mätrunda #%d '%s' raderades"),
	STATUS_MAIL_SENT(HandelseTyp.SYSTEM, "Status mail skickad till användare #%d, adress: %s"),
	STATUS_MAIL_FAILED(HandelseTyp.SYSTEM, "Status mail misslyckades till användare #%d, adress: %s"),
	IMPORT_FAILED(HandelseTyp.IMPORT, "Automatisk inläsning misslyckades, importtyp: %s, fel: %s"),
	IMPORT_DONE(HandelseTyp.IMPORT, "Automatisk inläsning lyckades, importtyp: %s, antal: %d"),
	RAPPORT_CREATED(HandelseTyp.RAPPORT, "Ny rapportinställning #%d '%s' skapad"),
	RAPPORT_MODIFIED(HandelseTyp.RAPPORT, "Rapportinställning #%d '%s' ändrad"),
	RAPPORT_REMOVED(HandelseTyp.RAPPORT, "Rapportinställning #%d '%s' raderades"),
	MATNING_REMOVED(HandelseTyp.MATNING, "Raderade mätning #%d\r\nRapportör: %s\r\nDatum: %s\r\nMätvärde: %s\r\nBeräknat värde: %s\r\nMätobjekt: #mobj:%d");

	private HandelseTyp typ;
	private String beskrivning;

	Handelse(HandelseTyp typ, String beskrivning) {
		this.typ = typ;
		this.beskrivning = beskrivning;
	}

	public HandelseTyp getTyp() {
		return typ;
	}

	public String getBeskrivning() {
		return beskrivning;
	}
}
