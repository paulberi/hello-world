package se.metria.markhandlaggning.restapi.projekt;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartFile;
import se.metria.markhandlaggning.auth.MarkhandlaggningRole;
import se.metria.markhandlaggning.openapi.api.ProjektApi;
import se.metria.markhandlaggning.openapi.model.FmeResponseDto;
import se.metria.markhandlaggning.openapi.model.ProjektDto;
import se.metria.markhandlaggning.openapi.model.ProjektPageDto;
import se.metria.markhandlaggning.service.avtal.AvtalService;
import se.metria.markhandlaggning.service.bifogadfil.BifogadfilService;
import se.metria.markhandlaggning.service.bifogadfil.dto.Bifogadfil;
import se.metria.markhandlaggning.service.bifogadfil.dto.SaveBifogadfil;
import se.metria.markhandlaggning.service.fme.FmeService;
import se.metria.markhandlaggning.service.projekt.ProjektService;
import se.metria.markhandlaggning.service.projekt.dto.SaveProjekt;
import se.metria.markhandlaggning.service.projekt.entity.ProjektEntity;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequestMapping(value = "/api")
@RestController
public class ProjektController implements ProjektApi {

    private ModelMapper mapper = new ModelMapper();
    private ProjektService projektService;
    private BifogadfilService bifogadfilService;
    private FmeService fmeService;
    private AvtalService avtalService;

    public ProjektController(ProjektService projektService,
                             BifogadfilService bifogadfilService,
                             FmeService fmeService,
                             AvtalService avtalService){
        this.projektService = projektService;
        this.bifogadfilService = bifogadfilService;
        this.fmeService = fmeService;
        this.avtalService = avtalService;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    @Override
    @RolesAllowed(MarkhandlaggningRole.MARKHANDLAGGARE)
    public ResponseEntity<ProjektDto> projektIdGet(UUID id) {
        try{
            return ResponseEntity.ok(mapper.map(projektService.getProjekt(id), ProjektDto.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    @RolesAllowed(MarkhandlaggningRole.MARKHANDLAGGARE)
    public ResponseEntity<ProjektPageDto> projektGet(@Valid Integer page, @Valid Integer pagesize) {
        var projekt = this.projektService.getAllaProjekt(page, pagesize);
        return ResponseEntity.ok(mapper.map(projekt, ProjektPageDto.class));
    }

    @Override
    @Transactional
    @RolesAllowed(MarkhandlaggningRole.MARKHANDLAGGARE)
    public ResponseEntity<ProjektDto> projektPost(@RequestPart("projektnr") String projektnr, @RequestPart("avtalstyp") String avtalstyp, @Valid @RequestPart("shape") MultipartFile shape, @Valid @RequestPart("markagarlista") MultipartFile markagarlista) {
        try {
            SaveBifogadfil shapeFile = SaveBifogadfil.builder()
                    .filnamn(shape.getOriginalFilename())
                    .mimeTyp(shape.getContentType())
                    .fil(shape.getBytes())
                    .build();

            SaveBifogadfil markagarlistaFile = SaveBifogadfil.builder()
                    .filnamn(markagarlista.getOriginalFilename())
                    .mimeTyp(markagarlista.getContentType())
                    .fil(markagarlista.getBytes())
                    .build();

            Bifogadfil bifogadShape = bifogadfilService.createBifogadfil(shapeFile);
            Bifogadfil bifogadMarkagarlista = bifogadfilService.createBifogadfil(markagarlistaFile);

            SaveProjekt saveProjekt = SaveProjekt.builder()
                    .projektnr(projektnr)
                    .avtalstyp(avtalstyp)
                    .status("skapat")
                    .shapeId(bifogadShape.getId())
                    .markagarlistaId(bifogadMarkagarlista.getId())
                    .build();
             return ResponseEntity.ok(mapper.map(projektService.createProjekt(saveProjekt), ProjektDto.class));
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    @Transactional
    @RolesAllowed(MarkhandlaggningRole.MARKHANDLAGGARE)
    public ResponseEntity<Void> projektIdDelete(UUID id) {
        ProjektEntity projekt = projektService.getProjekt(id);
        projektService.deleteProjekt(id);

        List<UUID> ids = new ArrayList<UUID>();
        ids.add(projekt.getShapeId());
        ids.add(projekt.getMarkagarlistaId());
        bifogadfilService.deleteBifogadfilByIds(ids);

        UUID avtalsid = projekt.getAvtalsId();
        if(avtalsid != null){
            avtalService.deleteAvtal(avtalsid);
        }

        return ResponseEntity.noContent().build();
    }

    @Override
    @RolesAllowed(MarkhandlaggningRole.MARKHANDLAGGARE)
    public ResponseEntity<ProjektDto> projektIdStatusStatusPut(UUID id, String status) {
        return ResponseEntity.ok(mapper.map(this.projektService.updateProjektStatus(id, status), ProjektDto.class));
    }

    @Override
    @RolesAllowed(MarkhandlaggningRole.MARKHANDLAGGARE)
    public ResponseEntity<FmeResponseDto> projektIdSubmitJobPost(UUID id) {
        return fmeService.submitJob(id);
    }
}
