package se.metria.mapcms.restapi.publik;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import se.metria.mapcms.commons.utils.HttpUtils;
import se.metria.mapcms.openapi.api.MinaSidorApi;
import se.metria.mapcms.openapi.model.*;
import se.metria.mapcms.service.publik.PublikDialogService;
import se.metria.mapcms.service.publik.PublikMeddelandeService;

import java.util.List;
import java.util.UUID;

@RequestMapping(value = "/api")
@RestController
@RequiredArgsConstructor
public class MinaSidorController implements MinaSidorApi {


    @NonNull
    private final PublikDialogService dialogPublikService;

    @NonNull
    private final PublikMeddelandeService meddelandePublikService;

    @Override
    public ResponseEntity<DialogRspDto> createDialogWithMessageForUser(UUID kundId, UUID projektId, DialogReqDto dialog, MeddelandeReqDto meddelande, List<MultipartFile> filer) throws Exception {

        DialogRspDto dialogRspDto=dialogPublikService.startDialog(kundId, projektId, dialog, meddelande, filer);
        return ResponseEntity.status(HttpStatus.CREATED).body(dialogRspDto);
    }

    @Override
    public ResponseEntity<MeddelandeRspDto> createMeddelandeInDialogForUser(UUID kundId, UUID projektId, UUID dialogId, MeddelandeReqDto meddelande, List<MultipartFile> filer) throws Exception {
        MeddelandeRspDto meddelandeRspDto= meddelandePublikService.createMeddelandeForUser(kundId, projektId, dialogId, meddelande, filer);
        return ResponseEntity.status(HttpStatus.CREATED).body(meddelandeRspDto);
    }

    @Override
    public ResponseEntity<DialogKomplettDto> getDialogForUser(UUID kundId, UUID projektId, UUID dialogId) throws Exception {
        DialogKomplettDto dialogKomplettDto= dialogPublikService.getDialogForUserWithId(kundId, projektId, dialogId);
        return ResponseEntity.ok().body(dialogKomplettDto);
    }

    @Override
    public ResponseEntity<Resource> getFilForDialogForUser(UUID kundId, UUID projektId, UUID dialogId, UUID meddelandeId, UUID filId) throws Exception {
        var entity = meddelandePublikService.getDialogFilForUserMeddelande(kundId,projektId,dialogId,meddelandeId,filId);
        var header= HttpUtils.setFilResponseHeaders(entity.getFilnamn(), MediaType.APPLICATION_OCTET_STREAM);

        ByteArrayResource resource = new ByteArrayResource(entity.getFil());
        return ResponseEntity.ok()
                .headers(header)
                .body(resource);
    }

    @Override
    public ResponseEntity<List<DialogRspDto>> listDialogerForUser(UUID kundId, UUID projektId) throws Exception {
        List<DialogRspDto> dialogRspDtoList= dialogPublikService.listDialogerForUser(kundId, projektId);
        return ResponseEntity.ok().body(dialogRspDtoList);
    }
}
