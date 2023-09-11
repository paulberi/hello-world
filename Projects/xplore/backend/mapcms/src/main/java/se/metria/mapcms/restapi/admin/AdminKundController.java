package se.metria.mapcms.restapi.admin;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import se.metria.mapcms.commons.keycloakroles.KeycloakRoles;
import se.metria.mapcms.commons.utils.HttpUtils;
import se.metria.mapcms.openapi.adminapi.KundAdminApi;
import se.metria.mapcms.openapi.model.KundReqDto;
import se.metria.mapcms.openapi.model.KundRspDto;
import se.metria.mapcms.openapi.model.SprakvalDto;
import se.metria.mapcms.openapi.model.SprakvalRspDto;
import se.metria.mapcms.service.admin.AdminKundService;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.UUID;

@RequestMapping(value = "/api/admin")
@RestController
@RequiredArgsConstructor
public class AdminKundController implements KundAdminApi {

    @NonNull
    private final AdminKundService kundService;

    @Override
    @RolesAllowed(KeycloakRoles.ADMIN_API_USER)
    public ResponseEntity<KundRspDto> getKundBySlugOrVhtNyckel(String kundIdOrVhtNyckel) throws Exception {
        var kund = kundService.getByIdOrVhtNyckel(kundIdOrVhtNyckel);
        return ResponseEntity.ok(kund);
    }

    @Override
    @RolesAllowed(KeycloakRoles.ADMIN_API_USER)
    public ResponseEntity<Resource> getLogoForKund(UUID kundId) throws Exception {
        var data = kundService.getLogotyp(kundId);
        // TODO: Få med rätt content type, ex: "image/png"
        var header = HttpUtils.setFilResponseHeaders(data.getFilename(), MediaType.APPLICATION_OCTET_STREAM);
        return ResponseEntity.ok().headers(header).body(data);
    }

    @Override
    @RolesAllowed(KeycloakRoles.ADMIN_API_USER)
    public ResponseEntity<List<KundRspDto>> listKunderForAdmin() throws Exception {
        return KundAdminApi.super.listKunderForAdmin();
    }

    @Override
    @RolesAllowed(KeycloakRoles.ADMIN_API_USER)
    public ResponseEntity<List<SprakvalRspDto>> listSprakForKund(UUID kundId) throws Exception {
        return ResponseEntity.ok(kundService.listSprak(kundId));
    }

    @Override
    @RolesAllowed(KeycloakRoles.ADMIN_API_USER)
    public ResponseEntity<KundRspDto> updateKund(String kundIdOrVhtNyckel, KundReqDto kund, MultipartFile logotyp) throws Exception {
        return KundAdminApi.super.updateKund(kundIdOrVhtNyckel, kund, logotyp);
    }

    @Override
    @RolesAllowed(KeycloakRoles.ADMIN_API_USER)
    public ResponseEntity<List<SprakvalRspDto>> updateSprakForKund(UUID kundId, List<SprakvalDto> sprakvalDto) throws Exception {
        return ResponseEntity.ok().body(kundService.updateSprak(kundId, sprakvalDto));
    }
}
