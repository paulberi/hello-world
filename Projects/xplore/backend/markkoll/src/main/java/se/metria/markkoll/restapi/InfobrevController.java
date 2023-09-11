package se.metria.markkoll.restapi;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.metria.markkoll.openapi.api.InfobrevApi;
import se.metria.markkoll.openapi.model.InfobrevsjobbProgressDto;
import se.metria.markkoll.service.InfobrevService;

import java.util.UUID;

import static se.metria.markkoll.util.HttpUtil.responseHeaders;

@RequestMapping(value = "/api")
@RestController
@RequiredArgsConstructor
@Slf4j
public class InfobrevController implements InfobrevApi {

    @NonNull
    private final InfobrevService infobrevService;

    @Override
    @PreAuthorize("hasInfobrevsjobbPermission(#infobrevsjobbId, 'READ')")
    public ResponseEntity<InfobrevsjobbProgressDto> infobrevsjobbProgress(UUID infobrevsjobbId) {
        var progress = infobrevService.getInfobrevsjobbProgress(infobrevsjobbId);

        return ResponseEntity.ok(progress);
    }

    @Override
    @PreAuthorize("hasInfobrevsjobbPermission(#infobrevsjobbId, 'WRITE')")
    public ResponseEntity<Void> cancelInfobrevsjobb(UUID infobrevsjobbId) throws Exception {
        infobrevService.cancelInfobrevsjobb(infobrevsjobbId);

        return ResponseEntity.ok().build();
    }

    @Override
    @PreAuthorize("hasInfobrevsjobbPermission(#infobrevsjobbId, 'READ')")
    public ResponseEntity<Resource> getInfobrevForProjekt(UUID infobrevsjobbId) {
        var infobrev = this.infobrevService.getInfobrev(infobrevsjobbId);

        if (infobrev.isPresent()) {
            HttpHeaders headers = responseHeaders(infobrev.get().getFilename(),
                    MediaType.APPLICATION_OCTET_STREAM);
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(infobrev.get());
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}
