package se.metria.skogsmaskindata.service.imports.shapefile;

public enum ShapefileType {
	INFORMATIONSLINJE("Informationslinje", "^IL_[^_]+\\.(?i)(shp)$", false),
	INFORMATIONSPOLYGON("Informationspolygon", "^IPOL_.[^_]+\\.(?i)(shp)$", false),
	INFORMATIONSPUNKT("Informationspunkt", "^IP_[^_]+\\.(?i)(shp)$", false),
	KORSPAR("Körspår", "^[^_]+\\.(?i)(shp)$", true),
	KORSPAR_FOR_TRANSPORT("Körspår för transport", "^Transport_to_[^_]+\\.(?i)(shp)$", false),
	PROVYTA("Provyta", "^[^_]+_PY\\.(?i)(shp)$", false),
	RESULTAT("Resultat", "^Result_[GU]_[^_]+\\.(?i)(shp)$", true);

	private String name;
	private String regex;
	private boolean required;

	ShapefileType(String name, String regex, boolean required) {
		this.name = name;
		this.regex = regex;
		this.required = required;
	}

	public String getName() {
		return name;
	}

	public String getRegex() {
		return regex;
	}

	public boolean isRequired() {
		return required;
	}
}