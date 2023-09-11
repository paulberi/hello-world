package se.metria.matdatabas.restapi.rapport;

import io.swagger.annotations.ApiParam;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.metria.matdatabas.openapi.api.RapportApi;
import se.metria.matdatabas.openapi.model.*;
import se.metria.matdatabas.security.MatdatabasRole;
import se.metria.matdatabas.service.anvandare.exception.AnvandareNotFoundException;
import se.metria.matdatabas.service.rapport.RapportFetcher;
import se.metria.matdatabas.service.rapport.RapportService;
import se.metria.matdatabas.service.rapport.dto.Rapport;
import se.metria.matdatabas.service.rapport.dto.RapportGrafSettings;
import se.metria.matdatabas.service.rapport.dto.RapportMottagare;
import se.metria.matdatabas.service.rapport.dto.RapportSettings;
import se.metria.matdatabas.service.rapport.exception.*;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(value = "/api")
public class RapportController implements RapportApi {

    private ModelMapper mapper;
    private RapportService rapportService;
    private RapportFetcher rapportFetcher;

    public RapportController(RapportService rapportService,
                             RapportFetcher rapportFetcher,
                             ModelMapper mapper)
    {
        this.rapportService = rapportService;
        this.rapportFetcher = rapportFetcher;
        this.mapper = mapper;
    }

    @Override
    public ResponseEntity<RapportPageDto> rapportGet(@NotNull @Valid Integer page, @Valid Integer size, @Valid String sortProperty, @Valid String sortDirection) {
        var rapporter = rapportService.getRapporter(page, size, sortProperty,  Sort.Direction.valueOf(sortDirection.toUpperCase()));

        var rapporterPage = mapper.map(rapporter, RapportPageDto.class);
        return ResponseEntity.ok(rapporterPage);
    }

    @Override
    @RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
    public ResponseEntity<RapportDto> rapportIdGet(Integer id) {
        try {
            Rapport rapport = rapportService.getRapport(id);
            return ResponseEntity.ok(mapper.map(rapport, RapportDto.class));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (RapportSettingsErrorException | AnvandareNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    @RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
    public ResponseEntity<Resource> rapportIdPdfGet(Integer id) {
        Resource pdf = rapportFetcher.fetchReport(id);
        HttpHeaders headers = pdfResponseHeaders("miljokoll_rapport_" + id);
        return ResponseEntity.ok()
                .headers(headers)
                .body(pdf);
    }

    @Override
    @RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
    public ResponseEntity<RapportSettingsDto> rapportSettingsPost(@Valid @RequestBody RapportSettingsDto rapportSettingsDto) {
        try {
            var rapportSettings = rapportService.createRapportSettings(mapper.map(rapportSettingsDto, RapportSettings.class));
            return ResponseEntity.ok(mapper.map(rapportSettings, RapportSettingsDto.class));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (RapportSettingsErrorException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    @RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
    public ResponseEntity<RapportSettingsDto> rapportSettingsIdPut(Integer id, @Valid @RequestBody RapportSettingsDto rapportSettingsDto) {
        try {
            final var rapportSettings = rapportService.updateRapportSettings(id, mapper.map(rapportSettingsDto, RapportSettings.class));
            return ResponseEntity.ok(mapper.map(rapportSettings, RapportSettingsDto.class));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (RapportSettingsErrorException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    @RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
    public ResponseEntity<Void> rapportIdDelete(Integer id) {
        if (rapportService.deleteRapportSettings(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    @RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
    public ResponseEntity<RapportSettingsDto> rapportSettingsIdGet(Integer id) {
        try {
            final var rapportSettings = rapportService.getRapportSettings(id);
            final var dto = mapper.map(rapportSettings, RapportSettingsDto.class);
            return ResponseEntity.ok(dto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
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
