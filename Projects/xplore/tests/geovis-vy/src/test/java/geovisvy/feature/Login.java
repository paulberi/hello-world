package geovisvy.feature;

import common.LoginSession;
import cucumber.api.java.sv.Givet;
import cucumber.api.java.sv.När;
import cucumber.api.java.sv.Så;
import geovisvy.shared.Config;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Login {
	private final Config config;
	private final LoginSession session;
	private final String tokenUrl;

	private List<String> roles;

	public Login(Config config, LoginSession session) {
		this.config = config;
		this.session = session;
		this.tokenUrl = config.getProperty("baseUrl") + config.getProperty("tokenPath");
	}

	@Givet("att jag är inloggad som en admin")
	public void inloggad_som_en_admin() {
		String username = config.getProperty("adminUser");
		String password = config.getProperty("adminUserPassword");
		session.login(tokenUrl, "geovis-vy", username, password);
	}

	@Givet("att jag är inloggad som en vanlig användare")
	public void inloggad_som_en_vanlig_användare() {
		String username = config.getProperty("readOnlyUser");
		String password = config.getProperty("readOnlyUserPassword");
		session.login(tokenUrl, "geovis-vy", username, password);
	}

	@När("jag undersöker tillgängliga funktioner")
	public void tillgängliga_funktioner() throws IOException {
		Map realmAccess = (Map) session.getClaims().get("realm_access");
		roles = (List<String>) realmAccess.get("roles");
	}

	@Så("har jag tillgång till admin-specifika funktioner")
	public void tillgång_till_admin_specifika_funktioner() {
		assertTrue(roles.contains("geovis_admin"));
	}


	@Så("har jag bara tillgång till funktioner för vanliga användare")
	public void bara_tillgång_till_funktioner_för_vanliga_användare() {
		assertFalse(roles.contains("geovis_admin"));
		assertTrue(roles.contains("geovis_user"));
	}
}
