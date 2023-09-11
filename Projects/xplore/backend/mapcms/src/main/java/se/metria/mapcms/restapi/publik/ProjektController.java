package se.metria.mapcms.restapi.publik;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.metria.mapcms.commons.utils.HttpUtils;
import se.metria.mapcms.openapi.api.ProjektApi;
import se.metria.mapcms.openapi.model.ProjektRspDto;
import se.metria.mapcms.service.publik.PublikProjektService;

import java.util.List;
import java.util.UUID;

import static se.metria.mapcms.commons.utils.UUIDValidator.isValidUUID;

@RequestMapping(value = "/api")
@RestController
@RequiredArgsConstructor
public class ProjektController implements ProjektApi {

    @NonNull
    private final PublikProjektService projektService;

    @Override
    public ResponseEntity<Resource> getFilForProjekt(UUID kundId, UUID projektId, UUID filId) throws Exception {

        var data = projektService.getFilForProjekt(kundId, projektId, filId);
        var header = HttpUtils.setFilResponseHeaders(data.getFilename(), MediaType.APPLICATION_OCTET_STREAM);
        return ResponseEntity.ok().headers(header).body(data);
    }

    @Override
    public ResponseEntity<ProjektRspDto> getProjektWithIdorSlug(UUID kundId, String projektIdOrSlug, String lang) throws Exception {
        ProjektRspDto res=null;
        if(isValidUUID(projektIdOrSlug)){
            res = projektService.getProjektWithId(kundId, UUID.fromString(projektIdOrSlug), lang);
        }else{
            res=projektService.getProjektWithSlug(kundId, projektIdOrSlug, lang);
        }
        return ResponseEntity.ok().body(res);
    }

    @Override
    public ResponseEntity<List<ProjektRspDto>> listProjekt(UUID kundId, String lang) throws Exception {
        return ResponseEntity.ok(projektService.listPublikaProjektForKund(kundId, lang));
    }
}
