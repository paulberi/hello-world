package se.metria.matdatabas.security;

import static java.util.stream.Collectors.toSet;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;;

public class MatdatabasRole {
	public static final String MATRAPPORTOR = "ROLE_MÄTRAPPORTÖR";
	public static final String TILLSTANDSHANDLAGGARE = "ROLE_TILLSTÅNDSHANDLÄGGARE";
	public static final String ADMINISTRATOR = "ROLE_ADMINISTRATÖR";
	public static final String OBSERVATOR = "ROLE_OBSERVATÖR";

	private static Map<Integer, Set<String>> ROLESMAPPING = Map.of(
			-1, Set.of(OBSERVATOR),
			0, Set.of(OBSERVATOR, MATRAPPORTOR),
			1, Set.of(OBSERVATOR, MATRAPPORTOR, TILLSTANDSHANDLAGGARE),
			2, Set.of(OBSERVATOR, MATRAPPORTOR, TILLSTANDSHANDLAGGARE, ADMINISTRATOR)
	);

	private MatdatabasRole() {
	}

	public static Set<String> getRoles(int behorighet) {
		 var roles = ROLESMAPPING.get(behorighet);

		 if (roles == null) {
		 	return new HashSet<String>();
		 } else {
		 	return roles;
		 }
	}
}
