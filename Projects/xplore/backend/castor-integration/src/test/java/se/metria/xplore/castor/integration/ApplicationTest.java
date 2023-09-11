package se.metria.xplore.castor.integration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = {CastorIntegrationApplication.class})
public class ApplicationTest {

	@Autowired
	private Client client;

	@Test
	public void testClient() throws Exception {
		String name = "test name";
		assertThat(client.upload(name)).isNotNull();
	}
}
