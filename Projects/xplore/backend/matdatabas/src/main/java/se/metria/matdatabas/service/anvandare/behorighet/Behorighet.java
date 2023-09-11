package se.metria.matdatabas.service.anvandare.behorighet;

public enum Behorighet {
	OBSERVATOR((short)-1, "Observatör"),
	MATRAPPORTOR((short)0, "Mätrapportör"),
	TILLSTANDSHANDLAGGARE((short)1, "Tillståndshandläggare"),
	ADMINISTRATOR((short)2, "Administratör");

	private Short id;
	private String name;

	Behorighet(Short id, String name) {
		this.id = id;
		this.name = name;
	}

	public Short getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public static String getNameById(Short id) {
		for (Behorighet es : Behorighet.values()) {
			if (es.id.equals(id)) {
				return es.getName();
			}
		}
		throw new IllegalArgumentException();
	}
}
