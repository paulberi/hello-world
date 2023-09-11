package se.metria.markkoll.restapi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.metria.markkoll.openapi.api.InstallningarApi;
import se.metria.markkoll.openapi.model.NisKallaDto;
import se.metria.markkoll.service.InstallningarService;

@RequestMapping(value = "/api")
@RestController
@RequiredArgsConstructor
@Slf4j
public class InstallningarController implements InstallningarApi {

    private final InstallningarService installningarService;
    @Override
    public ResponseEntity<NisKallaDto> getNisKalla(String kundId) throws Exception {
        return ResponseEntity.ok(installningarService.getNisKalla(kundId));
    }

    @Override
    public ResponseEntity<NisKallaDto> updateNisKalla(String kundId, NisKallaDto NISKallaDto) throws Exception {
        return ResponseEntity.ok(installningarService.updateNisKalla(kundId, NISKallaDto));
    }
}
