package com.example.api.demoapi;

import com.example.api.demoapi.controller.ExamController;
import com.example.api.demoapi.entity.ExamResult;
import com.example.api.demoapi.entity.Question;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DemoapiApplicationTests {

	@LocalServerPort
	int randomServerPort;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private ExamController examController;

	@Test
	@DisplayName("Controller loaded")
	void contextLoads() {
		assertThat(examController).isNotNull();
	}

	@Test
	@DisplayName("Answering test")
	void tesDefaultProduct() {
		String answer = this.restTemplate.withBasicAuth("admin", "admin").getForObject("/api/v1/exam/answer?question=What is JVM", String.class);
		assertEquals("Java Virtual Machine", answer);
	}

	// Tester för studerande
	@Test
	@DisplayName("Student test 2: Success endpoint")
	void testSuccessEndpoint() {
		String successResponse = this.restTemplate.withBasicAuth("admin", "admin").getForObject("/api/v1/exam/success?name=Jane", String.class);
		assertEquals("Success Jane", successResponse);
	}

	@Test
	@DisplayName("Student test 3: Adding question")
	public void testAddProductMissingHeader() throws URISyntaxException
	{
		final String baseUrl = "http://localhost:"+randomServerPort+"/api/v1/exam/addquestion";
		URI uri = new URI(baseUrl);
		Question question = new Question("What is Jakarta EE", "Eclipse project");

		HttpHeaders headers = new HttpHeaders();

		HttpEntity<Question> request = new HttpEntity<>(question, headers);

		ResponseEntity<String> result = this.restTemplate.withBasicAuth("admin", "admin").postForEntity(uri, request, String.class);

		assertEquals(200, result.getStatusCodeValue());
		assertEquals(true, result.getBody().equals("Question added"));
	}

	@Test
	@DisplayName("Student test 4: Get results for students")
	void testGetResultsEndpoint() {
		try {
			ResponseEntity<List<ExamResult>> responseEntityUser = this.restTemplate.withBasicAuth("user", "password").exchange("/api/v1/exam/examresults", HttpMethod.GET, null, new ParameterizedTypeReference<List<ExamResult>>() {});
			assertNotEquals(200, responseEntityUser.getStatusCodeValue());
		} catch (Exception e) {
			System.out.println("Failed as expected");
		}
		try{
			ResponseEntity<List<ExamResult>> responseEntityAdmin = this.restTemplate.withBasicAuth("admin", "admin").exchange("/api/v1/exam/examresults", HttpMethod.GET, null, new ParameterizedTypeReference<List<ExamResult>>() {});
			assertEquals(200, responseEntityAdmin.getStatusCodeValue());
		}catch(Exception e) {
			assert(false); // Failed due to not being implemented or correct
		}
	}


}
