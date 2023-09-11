package se.metria.matdatabas.security;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("När användaren har viss behörighet skall hen ha rätt roller")
class MatdatabasRoleTest {
	@ParameterizedTest(name = "När användare har behörighet {0} skall hen ha rollen ROLE_OBSERVATÖR")
	@ValueSource(ints = {-1, 0, 1, 2})
	void När_användare_har_rätt_behörighet_skall_hen_ha_rollen_ROLE_OBSERVATÖR(int behorighet) {
		assertTrue(MatdatabasRole.getRoles(behorighet).contains(MatdatabasRole.OBSERVATOR));
	}

	@ParameterizedTest(name = "När användare har behörighet {0} skall hen ha rollen ROLE_MÄTRAPPORTÖR")
	@ValueSource(ints = {0, 1, 2}) 
	void När_användare_har_rätt_behörighet_skall_hen_ha_rollen_ROLE_MÄTRAPPORTÖR(int behorighet) {
		assertTrue(MatdatabasRole.getRoles(behorighet).contains(MatdatabasRole.MATRAPPORTOR));
	}

	@ParameterizedTest(name = "När användare har behörighet {0} skall hen ha rollen ROLE_TILLSTÅNDSHANDLÄGGARE")
	@ValueSource(ints = {1, 2}) 
	void När_användare_har_rätt_behörighet_skall_hen_ha_rollen_ROLE_TILLSTÅNDSHANDLÄGGARE(int behorighet) {
		assertTrue(MatdatabasRole.getRoles(behorighet).contains(MatdatabasRole.TILLSTANDSHANDLAGGARE));
	}

	@ParameterizedTest(name = "När användare har behörighet {0} skall hen ha inte rollen ROLE_TILLSTÅNDSHANDLÄGGARE")
	@ValueSource(ints = {-1, 0})
	void När_användare_har_fel_behörighet_skall_hen_inte_ha_rollen_ROLE_TILLSTÅNDSHANDLÄGGARE(int behorighet) {
		assertFalse(MatdatabasRole.getRoles(behorighet).contains(MatdatabasRole.TILLSTANDSHANDLAGGARE));
	}

	@ParameterizedTest(name = "När användare har behörighet {0} skall hen ha rollen ROLE_ADMINISTRATÖR")
	@ValueSource(ints = {2}) 
	void När_användare_har_rätt_behörighet_skall_hen_ha_rollen_ROLE_ADMINISTRATÖR(int behorighet) {
		assertTrue(MatdatabasRole.getRoles(behorighet).contains(MatdatabasRole.ADMINISTRATOR));
	}

	@ParameterizedTest(name = "När användare har behörighet {0} skall hen inte ha rollen ROLE_ADMINISTRATÖR")
	@ValueSource(ints = {-1, 0, 1})
	void När_användare_har_fel_behörighet_skall_hen_inte_ha_rollen_ROLE_ADMINISTRATÖR(int behorighet) {
		assertFalse(MatdatabasRole.getRoles(behorighet).contains(MatdatabasRole.ADMINISTRATOR));
	}
	
	@ParameterizedTest(name = "När användare har behörighet {0} skall hen inte ha någon roll")
	@ValueSource(ints = {-2, 3, Integer.MAX_VALUE})
	void När_användare_har_annan_behörighet_skall_hen_inte_ha_någon_roll(int behorighet) {
		assertTrue(MatdatabasRole.getRoles(behorighet).isEmpty());
	}
}
