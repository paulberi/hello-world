package se.metria.xplore.samrad.controllers.publicApi;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.metria.xplore.samrad.openapi.api.KundApi;
import se.metria.xplore.samrad.openapi.model.KundDto;
import se.metria.xplore.samrad.services.KundService;

import java.util.UUID;


@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class PublicKundController implements KundApi {

    @NonNull
    private KundService kundService;


    @Override
    public ResponseEntity<Resource> getFil(String kundId, UUID filId) throws Exception {
        return KundApi.super.getFil(kundId, filId);
    }


    @Override
    public ResponseEntity<KundDto> getKundWithSlug(String kundSlug) throws Exception {
        KundDto kundDto=kundService.getKundBySlug(kundSlug);
        return ResponseEntity.ok().body(kundDto);
    }
}
