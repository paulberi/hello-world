package se.metria.matdatabas.service.systemlogg.handelse;

public enum HandelseTyp {
	MATNINGSTYP((short)0, "Mätningstyp"),
	INLOGGNING((short)1, "Inloggning"),
	ANVANDARE((short)2, "Användare"),
	SYSTEM((short)3, "System"),
	IMPORT((short)4, "Import"),
	RAPPORT((short)5, "Rapport"),
	MATNING((short)6, "Mätning");

	private Short id;
	private String name;

	HandelseTyp(Short id, String name)
	{
		this.id = id;
		this.name = name;
	}

	public Short getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
