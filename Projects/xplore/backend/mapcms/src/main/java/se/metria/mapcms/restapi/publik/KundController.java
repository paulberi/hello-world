package se.metria.mapcms.restapi.publik;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.metria.mapcms.commons.utils.HttpUtils;
import se.metria.mapcms.openapi.api.KundApi;
import se.metria.mapcms.openapi.model.KundRspDto;
import se.metria.mapcms.service.publik.PublikKundService;

@RequestMapping(value = "/api")
@RestController
@RequiredArgsConstructor
public class KundController implements KundApi {

    @NonNull
    private final PublikKundService kundService;

    @Override
    public ResponseEntity<KundRspDto> getKundWithSlug(String slug) throws Exception {
        var kund = kundService.getBySlug(slug);
        return ResponseEntity.ok(kund);
    }

    @Override
    public ResponseEntity<Resource> getLogoForKund(String slug) throws Exception {
        var data = kundService.getLogotypBySlug(slug);
        // TODO: Få med rätt content type, ex: "image/png"
        var header = HttpUtils.setFilResponseHeaders(data.getFilename(), MediaType.APPLICATION_OCTET_STREAM);
        return ResponseEntity.ok().headers(header).body(data);
    }
}
