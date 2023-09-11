package se.metria.xplore.samrad.controllers.adminApi;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.metria.xplore.samrad.commons.keycloakroles.KeycloakRoles;
import se.metria.xplore.samrad.openapi.adminapi.AdminApi;
import se.metria.xplore.samrad.openapi.model.KundDto;
import se.metria.xplore.samrad.services.KundService;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/api/admin")
public class AdminKundController implements AdminApi {


    @NonNull
    private KundService kundService;


    @Override
    @RolesAllowed(KeycloakRoles.ADMIN_API_USER)
    public ResponseEntity<KundDto> createKund(KundDto kundDto) throws Exception {
        KundDto kundDtoFromDB=kundService.saveKund(kundDto);
        return ResponseEntity.ok().body(kundDtoFromDB);
    }


    @Override
    @RolesAllowed(KeycloakRoles.ADMIN_API_USER)
    public ResponseEntity<KundDto> getKundWithSlug(String kundSlug) throws Exception {
        KundDto kundDto=kundService.getKundBySlug(kundSlug);
        return ResponseEntity.ok().body(kundDto);
    }

    @Override
    @RolesAllowed(KeycloakRoles.ADMIN_API_USER)
    public ResponseEntity<List<KundDto>> listKunder() throws Exception {
        List<KundDto> kundDtoList= kundService.KundList();
        return ResponseEntity.ok().body(kundDtoList);
    }
}
