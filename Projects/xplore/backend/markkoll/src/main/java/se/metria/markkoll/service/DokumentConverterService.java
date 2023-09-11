package se.metria.markkoll.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import se.metria.markkoll.openapi.dokument_converter.api.PdfApi;
import se.metria.markkoll.util.FileUtil;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class DokumentConverterService {

    @NonNull
    private final PdfApi pdfApi;

    @Value("${dokument-converter.url}")
    String dokumentConverterUrl;

    public Resource convertToPdf(Resource doc) throws IOException {
        log.info("Anropar convertToPdf() för dokumentkonverterare [{}]", dokumentConverterUrl);
        var tmpFile = FileUtil.saveTempFile(doc.getInputStream().readAllBytes(), "pdf", null);

        var pdfFile = pdfApi.convertToPDF(tmpFile);

        return new FileSystemResource(pdfFile);
    }
}
