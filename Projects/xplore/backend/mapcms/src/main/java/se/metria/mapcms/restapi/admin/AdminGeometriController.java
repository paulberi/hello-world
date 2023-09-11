package se.metria.mapcms.restapi.admin;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.metria.mapcms.commons.keycloakroles.KeycloakRoles;
import se.metria.mapcms.openapi.adminapi.GeometriAdminApi;
import se.metria.mapcms.openapi.model.GeometriReqDto;
import se.metria.mapcms.openapi.model.GeometriRspDto;
import se.metria.mapcms.service.admin.AdminGeometriService;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.UUID;

@RequestMapping(value = "/api/admin")
@RestController
@RequiredArgsConstructor
public class AdminGeometriController implements GeometriAdminApi {

    private final AdminGeometriService geometriService;

    @Override
    @RolesAllowed(KeycloakRoles.ADMIN_API_USER)
    public ResponseEntity createGeometri(UUID kundId, UUID projektId, GeometriReqDto geometriReqDto) {
            GeometriRspDto rsp = geometriService.createGeometri(kundId, projektId, geometriReqDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(rsp);
    }

    @Override
    @RolesAllowed(KeycloakRoles.ADMIN_API_USER)
    public ResponseEntity deleteGeometri(UUID kundId, UUID projektId, UUID geometriId) {
            geometriService.deleteGeometri(kundId, projektId, geometriId);
            return ResponseEntity.status(204).build();
    }

    @Override
    @RolesAllowed(KeycloakRoles.ADMIN_API_USER)
    public ResponseEntity getGeometri(UUID kundId, UUID projektId, UUID geometriId) {
            final GeometriRspDto geometri = geometriService.getGeometri(kundId, projektId, geometriId);
            return ResponseEntity.ok(geometri);
    }

    @Override
    @RolesAllowed(KeycloakRoles.ADMIN_API_USER)
    public ResponseEntity listGeometrier(UUID kundId, UUID projektId) {
            final List<GeometriRspDto> geometriList = geometriService.listGeometrier(kundId, projektId);
            return ResponseEntity.ok().body(geometriList);
    }

}
