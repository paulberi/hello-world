package se.metria.dokument_converter.restapi;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import se.metria.dokument_converter.openapi.api.PdfApi;
import se.metria.dokument_converter.service.DokumentConverterService;

import javax.validation.Valid;

@RequestMapping(value = "/api")
@RestController
@RequiredArgsConstructor
public class DokumentConverterController implements PdfApi {
    @NonNull
    private final DokumentConverterService dokumentConverterService;

    @Override
    public ResponseEntity<Resource> convertToPDF(@Valid MultipartFile file) throws Exception {
        var pdf = dokumentConverterService.convertDocxToPdf(file.getResource());

        return ResponseEntity.ok(pdf);
    }
}
