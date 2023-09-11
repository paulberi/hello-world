package se.metria.skogsmaskindata;


import java.util.List;
import java.util.stream.Collectors;

import com.jcraft.jsch.JSchException;

import se.metria.skogsmaskindata.openapi.api.ReadAllApi;
import se.metria.skogsmaskindata.openapi.model.ImportDto;
import se.metria.skogsmaskindata.service.forestlink.ForestLinkService;
import se.metria.skogsmaskindata.service.sftp.exceptions.ListRemoteFilesException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImportsController implements ReadAllApi {

	private ForestLinkService forestLinkService;

	public ImportsController(ForestLinkService forestLinkService) {
		this.forestLinkService = forestLinkService;
	}

	@Override
	public ResponseEntity<List<ImportDto>> readAllPost() {
		try {
			List<ImportDto> importDtos = forestLinkService.importPackages().stream().map(importToMap -> new ImportDto().
					objektnummer(importToMap.getObjektnummer()).
					organisation(importToMap.getOrganisation())
			).collect(Collectors.toList());
			return new ResponseEntity<>(importDtos, HttpStatus.OK);

		} catch (ListRemoteFilesException | JSchException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
