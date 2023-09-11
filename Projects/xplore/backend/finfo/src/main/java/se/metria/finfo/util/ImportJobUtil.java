package se.metria.finfo.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

public class ImportJobUtil {
    public static ResponseEntity<Void> jobCreatedResponseEntity(UriComponentsBuilder uriComponentsBuilder, UUID jobId) {
        var location = uriComponentsBuilder
            .replacePath("/api/importjob/{jobId}")
            .buildAndExpand(jobId)
            .toUri().toString();

        return ResponseEntity.status(HttpStatus.ACCEPTED).header(HttpHeaders.LOCATION, location).build();
    }
}
