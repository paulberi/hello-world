package se.metria.matdatabas;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import se.metria.matdatabas.security.MatdatabasUser;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static se.metria.matdatabas.security.MatdatabasRole.getRoles;

public class TestUtils {

	public static MatdatabasUser admin() {
		List<SimpleGrantedAuthority> roles = getRoles(2).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
		return new MatdatabasUser("admin", "{noop}asdf", true, true, true, true, roles, "Admin", "Admins", null, 1, LocalDateTime.now());
	}

	public static MatdatabasUser tillstandshandlaggare() {
		List<SimpleGrantedAuthority> roles = getRoles(1).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
		return new MatdatabasUser("tillstandshandlaggare", "{noop}asdf", true, true, true, true, roles, "Handläggare", "Tillståndshandläggare", null, 1, LocalDateTime.now());
	}

	public static MatdatabasUser matrapportor() {
		List<SimpleGrantedAuthority> roles = getRoles(0).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
		return new MatdatabasUser("matrapportor", "{noop}asdf", true, true, true, true, roles, "Rapportör", "Mätrapportörer", null, 1, LocalDateTime.now());
	}
}
