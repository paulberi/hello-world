package se.metria.markkoll.restapi;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.metria.markkoll.openapi.api.FilApi;
import se.metria.markkoll.service.FilService;
import se.metria.markkoll.util.HttpUtil;

import java.util.UUID;

@RequestMapping(value = "/api")
@RestController
@RequiredArgsConstructor
public class FilController implements FilApi {
    @NonNull
    private final FilService filService;

    @Override
    @PreAuthorize("hasFilPermission(#filId, 'KUND_READ')")
    public ResponseEntity<Resource> getFilData(UUID filId) {
        var data = filService.getData(filId);

        var header = HttpUtil.responseHeaders(data.getFilename(), MediaType.APPLICATION_OCTET_STREAM);

        return ResponseEntity.ok().headers(header).body(data);
    }
}
