package se.metria.xplore.samrad.controllers.publicApi;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.metria.xplore.samrad.openapi.api.ProjektApi;
import se.metria.xplore.samrad.openapi.model.ProjektDto;
import se.metria.xplore.samrad.services.ProjektService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins="*")
@RequestMapping("/api")
public class PublicProjektController implements ProjektApi {

    @NonNull
    private ProjektService projektService;


    @Override
    public ResponseEntity<ProjektDto> getProjekt(String kundId, UUID projektId, String lang) throws Exception {
        ProjektDto samradDtoFromDB=projektService.getProjektById(kundId,projektId);
        return ResponseEntity.ok().body(samradDtoFromDB);
    }


    @Override
    public ResponseEntity<List<ProjektDto>> listProjekt(String kundId, String lang) throws Exception {
        List<ProjektDto>projektDtoList=projektService.getProjektForKund(kundId);
        return ResponseEntity.ok().body(projektDtoList);
    }
}
