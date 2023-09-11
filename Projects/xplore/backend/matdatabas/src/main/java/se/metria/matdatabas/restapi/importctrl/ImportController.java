package se.metria.matdatabas.restapi.importctrl;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import se.metria.matdatabas.openapi.api.ImportApi;
import se.metria.matdatabas.openapi.model.ImportMatningDto;
import se.metria.matdatabas.security.MatdatabasRole;
import se.metria.matdatabas.service.kallsystem.KallsystemService;
import se.metria.matdatabas.service.kallsystem.dto.Kallsystem;
import se.metria.matdatabas.service.matning.AlreadyGodkandException;
import se.metria.matdatabas.service.matning.MatningService;
import se.metria.matdatabas.service.matning.dto.SaveMatning;
import se.metria.matdatabas.service.matning.dto.csvImport.ImportError;
import se.metria.matdatabas.service.matning.dto.csvImport.ImportFormat;
import se.metria.matdatabas.service.matning.dto.csvImport.ImportMatning;
import se.metria.matdatabas.service.matning.exception.MatningIllegalMatvarde;
import se.metria.matdatabas.service.matningstyp.MatningstypService;
import se.metria.matdatabas.service.matningstyp.dto.Matningstyp;
import se.metria.matdatabas.service.matobjekt.MatobjektService;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RequestMapping(value = "/api")
@RestController
public class ImportController implements ImportApi {
    private MatobjektService matobjektService;
    private MatningstypService matningstypService;
    private MatningService matningService;
    private KallsystemService kallsystemService;
    private ModelMapper mapper;

    public ImportController(MatobjektService matobjektService,
                            MatningstypService matningstypService,
                            KallsystemService kallsystemService,
                            MatningService matningService) {

        this.matobjektService = matobjektService;
        this.matningstypService = matningstypService;
        this.matningService = matningService;
        this.kallsystemService = kallsystemService;

        this.mapper = new ModelMapper();
    }

    class ImportMatningDateComparator implements Comparator<ImportMatningDto> {
        @Override
        public int compare(ImportMatningDto o1, ImportMatningDto o2) {
            // Allow optional seconds using pattern matching in DateTimeFormatter.
            LocalDateTime t1 = LocalDateTime.parse(o1.getAvlastDatum(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm[:ss]"));
            LocalDateTime t2 = LocalDateTime.parse(o2.getAvlastDatum(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm[:ss]"));
            return t1.compareTo(t2);
        }
    }

    @Override
    @RolesAllowed(MatdatabasRole.MATRAPPORTOR)
    @Transactional
    public ResponseEntity<Void> matningarExecuteImport(String matrapportor,
                                                       String kallsystemNamn,
                                                       List<ImportMatningDto> importMatningDto) {

        Optional<Kallsystem> kallsystem = kallsystemService.findById(kallsystemNamn);

        if (kallsystem.isEmpty() || !kallsystem.get().isManuellImport()) {
            throw new IllegalArgumentException("Fel källsystem");
        }

        try {
            Map<String, List<Matningstyp>> matningstypCache = new HashMap<>();

            /* For computations of 'beräknat värde', some of the computed values depend on earlier measurements.
            * Therefore, ensure the imports are sorted in chronological order. */
            importMatningDto.sort(new ImportMatningDateComparator());

            for (ImportMatningDto matning : importMatningDto) {
                List<Matningstyp> matningstyper = matningstypCache.get(matning.getMatobjekt());
                if (matningstyper == null) {
                    var matobjektId = matobjektService.getMatobjektIdByNamn(matning.getMatobjekt());
                    matningstyper = matningstypService.getMatningstyperForMatobjekt(matobjektId.get());
                    matningstypCache.put(matning.getMatobjekt(), matningstyper);
                }
                Matningstyp matningstyp = null;
                for (var mt : matningstyper) {
                    if (mt.getId().equals(matning.getMatningstypId())) {
                        matningstyp = mt;
                    }
                }

                if (matningstyp == null) {
                    return ResponseEntity.badRequest().build();
                }

                var create = new SaveMatning();

                parseDoubleVarde(matning.getAvlastVarde()).ifPresentOrElse(
                        varde -> create.setAvlastVarde(varde),
                        () -> create.setAvlastVarde(null)
                );

                parseDoubleVarde(matning.getBeraknatVarde()).ifPresentOrElse(
                        varde -> create.setBeraknatVarde(varde),
                        () -> create.setBeraknatVarde(null)
                );

                create.setAvlastDatum(LocalDateTime.parse(matning.getAvlastDatum().replace(" ", "T")));
                create.setKommentar(matning.getKommentar());
                create.setFelkod(matning.getFelkod());
                create.setRapportor(matrapportor);

                Short detektionsomrade = null;

                if (matning.getInomDetektionsomrade() != null) {
                    switch (matning.getInomDetektionsomrade()) {
                        case "":
                            detektionsomrade = 1;
                            break;
                        case "<":
                            detektionsomrade = 2;
                            break;
                        case ">":
                            detektionsomrade = 0;
                            break;
                        default:
                            return ResponseEntity.badRequest().build();
                    }
                }

                create.setInomDetektionsomrade(detektionsomrade);

                try {
                    matningService.create(matningstyp, create, true, kallsystemNamn);
                } catch (MatningIllegalMatvarde e) {
                    return ResponseEntity.badRequest().build();
                } catch (AlreadyGodkandException e) {
                    return ResponseEntity.badRequest().build();
                }
            }
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().build();
        }

        return null;
    }

    @Override
    @RolesAllowed(MatdatabasRole.MATRAPPORTOR)
    public ResponseEntity<List<ImportMatningDto>> matningarParseImport(String format,
                                                                       String kallsystemNamn,
                                                                       MultipartFile file) {
        Optional<Kallsystem> kallsystem = kallsystemService.findById(kallsystemNamn);

        if (kallsystem.isEmpty() || !kallsystem.get().isManuellImport()) {
            throw new IllegalArgumentException("Fel källsystem");
        }

        try (var csv = new ByteArrayInputStream(file.getBytes())) {
            var matningar = matobjektService.parseCsvImport(csv, importFormat(format), kallsystem.get());

            return ResponseEntity.ok(matningar.stream()
                    .map(m -> mapper.map(m, ImportMatningDto.class))
                    .collect(Collectors.toList()));
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    importErrorParsing(e).stream()
                                         .map(m -> mapper.map(m, ImportMatningDto.class))
                                         .collect(Collectors.toList())
            );
        }
    }


    private List<ImportMatning> importErrorParsing(IllegalArgumentException e) {
        var error = ImportError.builder()
                               .property("")
                               .error(e.getMessage())
                               .build();

        var importFelList = new ArrayList<ImportError>();
        importFelList.add(error);


        var importMatning = ImportMatning.builder()
                                         .importFel(importFelList)
                                         .build();

        var importMatningList = new ArrayList<ImportMatning>();
        importMatningList.add(importMatning);

        return importMatningList;
    }

    private ImportFormat importFormat(String format) throws IOException {
        switch (format) {
            case "STANDARD":
                return ImportFormat.STANDARD;
            case "INSTRUMENT":
                return ImportFormat.INSTRUMENT;
            default:
                throw new IOException("Unrecognized format: " + format);
        }
    }

    private Optional<Double> parseDoubleVarde(String vardeString) throws NumberFormatException{
        if (vardeString == null || vardeString.equalsIgnoreCase("")) {
            return Optional.empty();
        } else {
            return Optional.of(Double.parseDouble(vardeString.replace(",", ".")));
        }
    }

}
