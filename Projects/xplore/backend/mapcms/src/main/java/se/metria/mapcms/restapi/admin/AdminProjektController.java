package se.metria.mapcms.restapi.admin;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import se.metria.mapcms.commons.keycloakroles.KeycloakRoles;
import se.metria.mapcms.commons.utils.HttpUtils;
import se.metria.mapcms.openapi.adminapi.ProjektAdminApi;
import se.metria.mapcms.openapi.model.*;
import se.metria.mapcms.service.admin.AdminProjektService;
import se.metria.mapcms.service.admin.filer.ProjektFilService;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.UUID;

@RequestMapping(value = "/api/admin")
@RestController
@RequiredArgsConstructor
public class AdminProjektController implements ProjektAdminApi {

    @NonNull
    private final AdminProjektService projektService;

    @NonNull
    private final ProjektFilService projektFilService;

    @NonNull
    private final ModelMapper modelMapper;

    @Override
    @RolesAllowed(KeycloakRoles.ADMIN_API_USER)
    public ResponseEntity<FilRspDto> addFilForProjekt(UUID kundId, UUID projektId, FilReqDto metadata, MultipartFile fil) throws Exception {
        var filRsp= projektFilService.saveProjektFil(kundId,projektId,metadata,fil);
        return ResponseEntity.ok().body(filRsp);
    }

    @Override
    @RolesAllowed(KeycloakRoles.ADMIN_API_USER)
    public ResponseEntity<ProjektOversattningDto> createOversattningForProjekt(UUID kundId, UUID projektId, ProjektOversattningDto projektOversattningDto) throws Exception {
        return ResponseEntity.ok().body(projektService.oversattaProjekt(kundId,projektId,projektOversattningDto));
    }

    @Override
    @RolesAllowed(KeycloakRoles.ADMIN_API_USER)
    public ResponseEntity<ProjektRspDto> createProjekt(UUID kundId, ProjektReqDto projektReqDto) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(projektService.createProjektForKund(kundId, projektReqDto));
    }

    @Override
    @RolesAllowed(KeycloakRoles.ADMIN_API_USER)
    public ResponseEntity<Void> deleteFilForProjekt(UUID kundId, UUID projektId, UUID filId) throws Exception {

        projektFilService.deleteProjektFile(kundId,projektId,filId);
        return ResponseEntity.ok().build();
    }

    @Override
    @RolesAllowed(KeycloakRoles.ADMIN_API_USER)
    public ResponseEntity<Void> deleteOversattningForProjekt(UUID kundId, UUID projektId, String sprakkod) throws Exception {
        projektService.deleteOversattningForKundProjekt(kundId,projektId,sprakkod);
        return ResponseEntity.ok().build();
    }

    @Override
    @RolesAllowed(KeycloakRoles.ADMIN_API_USER)
    public ResponseEntity<Void> deleteProjekt(UUID kundId, UUID projektId) throws Exception {

        projektService.deleteProjekt(kundId,projektId);
        return ResponseEntity.ok().build();
    }

    @Override
    @RolesAllowed(KeycloakRoles.ADMIN_API_USER)
    public ResponseEntity<FilRspDto> getFilForProjekt(UUID kundId, UUID projektId, UUID filId) throws Exception {

        var entity = projektFilService.getProjektFilRspById(kundId,projektId,filId);
        return ResponseEntity.ok().body(entity);
    }

    @Override
    @RolesAllowed(KeycloakRoles.ADMIN_API_USER)
    public ResponseEntity<Resource> getFilInnehallForProjekt(UUID kundId, UUID projektId, UUID filId) throws Exception {

        var entity = projektFilService.getProjektFilById(kundId,projektId,filId);
        var header= HttpUtils.setFilResponseHeaders(entity.getFilnamn(), MediaType.APPLICATION_OCTET_STREAM);

        ByteArrayResource resource = new ByteArrayResource(entity.getFil());
        return ResponseEntity.ok()
                .headers(header)
                .body(resource);
    }

    @Override
    @RolesAllowed(KeycloakRoles.ADMIN_API_USER)
    public ResponseEntity<ProjektOversattningDto> getOversattningForProjekt(UUID kundId, UUID projektId, String sprakkod) throws Exception {
        return ResponseEntity.ok().body(projektService.getOversattningById(kundId, projektId,sprakkod));
    }

    @Override
    @RolesAllowed(KeycloakRoles.ADMIN_API_USER)
    public ResponseEntity<ProjektRspDto> getProjekt(UUID kundId, UUID projektId, String lang) throws Exception {
        return ResponseEntity.ok(projektService.getProjektForKund(kundId, projektId, lang));
    }

    @Override
    @RolesAllowed(KeycloakRoles.ADMIN_API_USER)
    public ResponseEntity<List<FilRspDto>> listFilerForProjekt(UUID kundId, UUID projektId) throws Exception {
        List<FilRspDto> list=projektFilService.listProjektFiler(kundId,projektId);
        return ResponseEntity.ok().body(list);
    }

    @Override
    @RolesAllowed(KeycloakRoles.ADMIN_API_USER)
    public ResponseEntity<List<ProjektOversattningDto>> listOversattningarForProjekt(UUID kundId, UUID projektId) throws Exception {
        return ResponseEntity.ok().body(projektService.listOversattningarForKundProjekt(kundId,projektId));
    }

    @Override
    @RolesAllowed(KeycloakRoles.ADMIN_API_USER)
    public ResponseEntity<List<ProjektRspDto>> listProjekt(UUID kundId, String lang) throws Exception {
        return ResponseEntity.ok(projektService.listProjektForKund(kundId, lang));
    }

    @Override
    @RolesAllowed(KeycloakRoles.ADMIN_API_USER)
    public ResponseEntity<FilRspDto> updateFilForProjekt(UUID kundId, UUID projektId, UUID filId, FilReqDto metadata, MultipartFile fil) throws Exception {
        return ProjektAdminApi.super.updateFilForProjekt(kundId, projektId, filId, metadata, fil);
    }

    @Override
    @RolesAllowed(KeycloakRoles.ADMIN_API_USER)
    public ResponseEntity<ProjektOversattningDto> updateOversattningForProjekt(UUID kundId, UUID projektId, String sprakkod, ProjektTextDto projektTextDto) throws Exception {
        return ResponseEntity.ok().body(projektService.updateOversattningForKundProjekt(kundId,projektId,sprakkod,projektTextDto));
    }

    @Override
    @RolesAllowed(KeycloakRoles.ADMIN_API_USER)
    public ResponseEntity<ProjektRspDto> updateProjekt(UUID kundId, UUID projektId, ProjektReqDto projektReqDto) throws Exception {
        return ResponseEntity.ok().body(projektService.updateProjektForKund(kundId,projektId,projektReqDto));

    }
}
