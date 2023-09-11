package se.metria.skogsanalys.restapi.rapport;

import lombok.NonNull;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.metria.skogsanalys.openapi.api.RapportApi;
import se.metria.skogsanalys.service.rapport.RapportFetcher;

import javax.annotation.security.RolesAllowed;
import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping(value = "/api")
public class RapportController implements RapportApi {
    @NonNull
    private final RapportFetcher rapportFetcher;

    public RapportController(RapportFetcher rapportFetcher) {
        this.rapportFetcher = rapportFetcher;
    }

    @Override
    @RolesAllowed("skogsanalys_analys_rapport")
    public ResponseEntity<Resource> rapportSkogligPdfGet(String skoglig, String lay, String ext, Boolean inverteraFastigheter, BigDecimal fastighetsgranserOpacity, String title) {
        Resource pdf = rapportFetcher.fetchReport(skoglig, lay, ext, title, inverteraFastigheter, fastighetsgranserOpacity);
        HttpHeaders headers = pdfResponseHeaders("Skogsanalys "+title);
        return ResponseEntity.ok()
                .headers(headers)
                .body(pdf);
    }

    HttpHeaders pdfResponseHeaders(String filenamePrefix) {
        HttpHeaders headers = new HttpHeaders();
        var date = LocalDate.now().toString();
        String filename = filenamePrefix + "_" + date + ".pdf";
        headers.set("Content-Disposition", "inline;filename=" + filename);
        headers.setContentType(MediaType.APPLICATION_PDF);

        return headers;
    }
}
