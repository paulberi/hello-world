package se.metria.markkoll.restapi;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import se.metria.markkoll.exception.MarkkollError;
import se.metria.markkoll.exception.MarkkollException;
import se.metria.markkoll.openapi.api.DokumentmallApi;
import se.metria.markkoll.openapi.model.DokumentInfoDto;
import se.metria.markkoll.openapi.model.DokumentmallDto;
import se.metria.markkoll.service.dokument.DokumentGeneratorService;
import se.metria.markkoll.service.dokument.DokumentmallService;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static se.metria.markkoll.util.HttpUtil.responseHeaders;

@RequestMapping(value = "/api")
@RestController
@RequiredArgsConstructor
@Slf4j
public class DokumentmallController implements DokumentmallApi {
    @NonNull
    private final DokumentmallService dokumentmallService;

    @NonNull
    private final DokumentGeneratorService dokumentGeneratorService;

    @Override
    @Transactional
    @PreAuthorize("hasKundPermission(#kundId, 'ADMINISTRATION')")
    public ResponseEntity<DokumentmallDto>
    createDokumentmall(String kundId, MultipartFile file, DokumentInfoDto dokumentInfo)
    {
        try {
            var dokument = dokumentmallService.create(dokumentInfo, kundId, file);
            return ResponseEntity.ok(dokument);
        } catch (IOException e) {
            throw new MarkkollException(MarkkollError.DOKUMENT_SKAPA_ERROR);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasDokumentPermission(#dokumentId, 'ADMINISTRATION')")
    public ResponseEntity<Void> deleteDokumentmall(String kundId, UUID dokumentId){
        dokumentmallService.delete(dokumentId);

        return ResponseEntity.ok().build();
    }

    @Override
    @PreAuthorize("hasKundPermission(#kundId, 'KUND_READ')")
    public ResponseEntity<List<DokumentmallDto>> getKundDokumentmallar(String kundId) {
        var dokument = dokumentmallService.getKundDokumentmallar(kundId);
        return ResponseEntity.ok(dokument);
    }

    @Override
    @PreAuthorize("hasDokumentPermission(#dokumentId, 'KUND_READ')")
    public ResponseEntity<Resource> getDokumentmallData(String kundId, UUID dokumentId) {
        var doc = dokumentmallService.getFileData(dokumentId);
        var name = dokumentmallService.getFileName(dokumentId);

        var headers = responseHeaders(name, MediaType.APPLICATION_OCTET_STREAM);
        return ResponseEntity.ok().headers(headers).body(doc);
    }

    @Override
    @PreAuthorize("hasKundPermission(#kundId, 'ADMINISTRATION')")
    public ResponseEntity<Resource> prepareDokumentmall(String kundId, MultipartFile file) throws Exception {
        var docNew = dokumentGeneratorService.addDocProperties(file.getResource());

        var headers = responseHeaders(file.getOriginalFilename(), MediaType.APPLICATION_OCTET_STREAM);
        return ResponseEntity.ok().headers(headers).body(docNew);
    }

    @Override
    @Transactional
    @PreAuthorize("hasDokumentPermission(#dokumentmallId, 'ADMINISTRATION')")
    public ResponseEntity<DokumentmallDto>
    updateDokumentmall(String kundId, UUID dokumentmallId, @Valid DokumentmallDto dokumentDto) {
        dokumentmallService.update(dokumentmallId, dokumentDto);
        return ResponseEntity.ok(dokumentDto);
    }
}
