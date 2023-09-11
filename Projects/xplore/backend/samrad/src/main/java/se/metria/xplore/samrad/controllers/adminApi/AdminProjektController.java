package se.metria.xplore.samrad.controllers.adminApi;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.metria.xplore.samrad.commons.keycloakroles.KeycloakRoles;
import se.metria.xplore.samrad.openapi.adminapi.ProjektApi;
import se.metria.xplore.samrad.openapi.model.ProjektDto;
import se.metria.xplore.samrad.services.ProjektService;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/api/admin")
public class AdminProjektController implements ProjektApi {

    @NonNull
    private ProjektService projektService;


    @Override
    @RolesAllowed(KeycloakRoles.ADMIN_API_USER)
    public ResponseEntity<ProjektDto> addProjekt(String kundId, ProjektDto projektDto) throws Exception {
        ProjektDto samradDtoFromDB=projektService.saveProjekt(kundId, projektDto);
        return ResponseEntity.ok().body(samradDtoFromDB);
    }


    @Override
    @RolesAllowed(KeycloakRoles.ADMIN_API_USER)
    public ResponseEntity<ProjektDto> getProjektWithId(String kundId, UUID projektId) throws Exception {
        ProjektDto samradDtoFromDB = projektService.getProjektById(kundId, projektId);
        return ResponseEntity.ok().body(samradDtoFromDB);
    }


    @Override
    @RolesAllowed(KeycloakRoles.ADMIN_API_USER)
    public ResponseEntity<List<ProjektDto>> listProjekt(String kundId) throws Exception {
            List<ProjektDto> projektDtoList=projektService.getProjektForKund(kundId);
            return ResponseEntity.ok().body(projektDtoList);
    }


    @Override
    @RolesAllowed(KeycloakRoles.ADMIN_API_USER)
    public ResponseEntity<ProjektDto> updateProjekt(String kundId, UUID projektId, ProjektDto projektDto) throws Exception {
        ProjektDto samradDtoFromDB=projektService.updateProjekt(projektDto,kundId,projektId);
        return ResponseEntity.ok().body(samradDtoFromDB);
    }

    @Override
    public ResponseEntity<Void> deleteSamrad(String kundId, UUID projektId) throws Exception {
        projektService.deleteProjekt(kundId, projektId);
        return ResponseEntity.ok().build();
    }
}
