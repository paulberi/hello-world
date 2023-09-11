package se.metria.matdatabas;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import se.metria.matdatabas.openapi.model.SaveMatningDto;

import java.time.LocalDateTime;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static se.metria.matdatabas.TestUtils.matrapportor;

public class Requests {
	private MockMvc mvc;
	private ObjectMapper objectMapper;

	public Requests(MockMvc mvc, ObjectMapper objectMapper) {
		this.mvc = mvc;
		this.objectMapper = objectMapper;
	}

	public void addMatningar(Integer matobjektId, Integer matningstypId, LocalDateTime startDate, int count, Double avlastVarde) throws Exception {
		for (int i = 0; i < count; i++) {
			var saveMatningDto = new SaveMatningDto();
			saveMatningDto.avlastVarde(avlastVarde);
			saveMatningDto.avlastDatum(startDate.plusMinutes(i));
			saveMatningDto.rapportor("Test AB");

			mvc.perform(post("/api/matobjekt/{matobjekt}/matningstyper/{matningstyp}/matningar", matobjektId, matningstypId)
					.content(objectMapper.writeValueAsString(saveMatningDto))
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.with(csrf())
					.with(user(matrapportor())))
					.andExpect(status().isCreated());
		}
	}
}
