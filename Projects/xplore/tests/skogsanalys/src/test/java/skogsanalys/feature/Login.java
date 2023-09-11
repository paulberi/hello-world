package skogsanalys.feature;

import common.LoginSession;
import cucumber.api.java.sv.Givet;
import cucumber.api.java.sv.När;
import cucumber.api.java.sv.Så;
import skogsanalys.shared.Config;

import static org.junit.Assert.assertTrue;

public class Login {
	private final Config config;
	private final LoginSession session;
	private final String tokenUrl;

	private String username;
	private String password;

	public Login(Config config, LoginSession session) {
		this.config = config;
		this.session = session;
		this.tokenUrl = config.getProperty("baseUrl") + config.getProperty("tokenPath");
	}

	@Givet("att jag har användaruppgifter till systemet för en vanlig användare")
	public void har_användaruppgifter() {
		username = config.getProperty("username");
		password = config.getProperty("password");
	}

	// Användbar för andra features
	@Givet("att jag är inloggad som en vanlig användare")
	public void inloggad_som_vanlig_användare() {
		username = config.getProperty("username");
		password = config.getProperty("password");
		session.login(tokenUrl, "xplore-map", username, password);
	}

	@När("jag loggar in")
	public void logga_in() {
		session.login(tokenUrl, "xplore-map", username, password);
	}

	@Så("ska det lyckas")
	public void inloggning_lyckas() {
		assertTrue(!session.isAnonymous());
	}
}
