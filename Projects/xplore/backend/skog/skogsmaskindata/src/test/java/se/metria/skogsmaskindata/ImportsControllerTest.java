package se.metria.skogsmaskindata;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import com.jcraft.jsch.JSchException;

import se.metria.skogsmaskindata.openapi.model.ImportDto;
import se.metria.skogsmaskindata.service.forestlink.ForestLinkService;
import se.metria.skogsmaskindata.service.imports.model.Import;
import se.metria.skogsmaskindata.service.sftp.exceptions.ListRemoteFilesException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class ImportsControllerTest {

	private ForestLinkService forestlinkService;
	private ImportsController importsController;

	@BeforeEach
	void setup() {
		forestlinkService = mock(ForestLinkService.class);
		importsController = new ImportsController(forestlinkService);
	}

	@Test
	void readAllPost_successful() throws JSchException, ListRemoteFilesException {

		given(forestlinkService.importPackages()).willReturn(Arrays.asList(new Import("", "", OffsetDateTime.now(), ""), new Import("", "", OffsetDateTime.now(), "")));

		ResponseEntity<List<ImportDto>> responseEntity = importsController.readAllPost();

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(2, responseEntity.getBody().size());

	}

}