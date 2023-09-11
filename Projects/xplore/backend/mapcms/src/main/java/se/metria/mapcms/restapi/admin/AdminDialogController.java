package se.metria.mapcms.restapi.admin;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import se.metria.mapcms.commons.keycloakroles.KeycloakRoles;
import se.metria.mapcms.openapi.adminapi.DialogAdminApi;
import se.metria.mapcms.openapi.model.DialogReqAdminDto;
import se.metria.mapcms.openapi.model.DialogRspDto;
import se.metria.mapcms.openapi.model.MeddelandeReqAdminDto;
import se.metria.mapcms.openapi.model.MeddelandeRspDto;
import se.metria.mapcms.service.admin.AdminDialogService;
import se.metria.mapcms.service.admin.AdminMeddelandeService;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.UUID;

@RequestMapping(value = "/api/admin")
@RestController
@RequiredArgsConstructor
public class AdminDialogController implements DialogAdminApi {

    @NonNull
    private final AdminDialogService dialogService;

    @NonNull
    private final AdminMeddelandeService meddelandeService;


    @Override
    @RolesAllowed(KeycloakRoles.ADMIN_API_USER)
    public ResponseEntity<DialogRspDto> createDialogWithMessage(UUID kundId, UUID projektId, DialogReqAdminDto dialog, MeddelandeReqAdminDto meddelande, List<MultipartFile> filer) throws Exception {

        DialogRspDto dialogRspDto=dialogService.startDialog(kundId,projektId,dialog, meddelande, filer);
        return ResponseEntity.status(HttpStatus.CREATED).body(dialogRspDto);
    }

    @Override
    @RolesAllowed(KeycloakRoles.ADMIN_API_USER)
    public ResponseEntity<MeddelandeRspDto> createMeddelandeForDialog(UUID kundId, UUID projektId, UUID dialogId, MeddelandeReqAdminDto meddelande, List<MultipartFile> filer) throws Exception {

        MeddelandeRspDto meddelandeRspDto=meddelandeService.createMeddelande(kundId,projektId,dialogId,meddelande,filer);
        return ResponseEntity.status(HttpStatus.CREATED).body(meddelandeRspDto);
    }

    @Override
    @RolesAllowed(KeycloakRoles.ADMIN_API_USER)
    public ResponseEntity<Void> deleteDialog(UUID kundId, UUID projektId, UUID dialogId) throws Exception {
        dialogService.deleteDialogWithId(kundId, projektId, dialogId);
        return ResponseEntity.ok().build();
    }

    @Override
    @RolesAllowed(KeycloakRoles.ADMIN_API_USER)
    public ResponseEntity<Void> deleteMeddelandeForDialog(UUID kundId, UUID projektId, UUID dialogId, UUID meddelandeId) throws Exception {
        meddelandeService.deleteMeddelandeById(kundId,projektId,dialogId,meddelandeId);
        return ResponseEntity.ok().build();
    }

    @Override
    @RolesAllowed(KeycloakRoles.ADMIN_API_USER)
    public ResponseEntity<DialogRspDto> getDialog(UUID kundId, UUID projektId, UUID dialogId) throws Exception {

        return ResponseEntity.ok().body(dialogService.getDialogWithId(kundId, projektId, dialogId));
    }

    @Override
    @RolesAllowed(KeycloakRoles.ADMIN_API_USER)
    public ResponseEntity<MeddelandeRspDto> getMeddelandeForDialog(UUID kundId, UUID projektId, UUID dialogId, UUID meddelandeId) throws Exception {
        return ResponseEntity.ok().body(meddelandeService.getMeddelandeById(kundId,projektId,dialogId,meddelandeId));
    }

    @Override
    @RolesAllowed(KeycloakRoles.ADMIN_API_USER)
    public ResponseEntity<List<DialogRspDto>> listDialoger(UUID kundId, UUID projektId) throws Exception {
        return ResponseEntity.ok().body(dialogService.listDialogForProjekt(kundId, projektId));
    }

    @Override
    @RolesAllowed(KeycloakRoles.ADMIN_API_USER)
    public ResponseEntity<List<MeddelandeRspDto>> listMeddelandenForDialog(UUID kundId, UUID projektId, UUID dialogId) throws Exception {

        List<MeddelandeRspDto> meddelandeRspDtoList= meddelandeService.listMeddelanden(kundId,projektId,dialogId);
        return ResponseEntity.ok().body(meddelandeRspDtoList);
    }

    @Override
    @RolesAllowed(KeycloakRoles.ADMIN_API_USER)
    public ResponseEntity<DialogRspDto> updateDialog(UUID kundId, UUID projektId, UUID dialogId, DialogReqAdminDto dialogReqAdminDto) throws Exception {
        return ResponseEntity.ok().body(dialogService.updateDialogWithId(kundId,projektId,dialogId,dialogReqAdminDto));
    }

    @Override
    @RolesAllowed(KeycloakRoles.ADMIN_API_USER)
    public ResponseEntity<MeddelandeRspDto> updateMeddelandeForDialog(UUID kundId, UUID projektId, UUID dialogId, UUID meddelandeId, MeddelandeReqAdminDto meddelandeReqAdminDto) throws Exception {

        MeddelandeRspDto meddelandeRspDto=meddelandeService.updateMeddelande(kundId,projektId,dialogId,meddelandeId, meddelandeReqAdminDto);
        return ResponseEntity.ok().body(meddelandeRspDto);
    }
}
