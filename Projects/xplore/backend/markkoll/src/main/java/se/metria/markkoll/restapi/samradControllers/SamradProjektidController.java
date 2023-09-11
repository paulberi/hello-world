package se.metria.markkoll.restapi.samradControllers;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.metria.markkoll.openapi.api.SamradProjektApi;
import se.metria.markkoll.openapi.model.SamradDto;
import se.metria.markkoll.security.MarkkollRole;
import se.metria.markkoll.service.samradService.SamradProjektidService;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.UUID;

@RequestMapping(value = "/api")
@RestController
@RequiredArgsConstructor
public class SamradProjektidController implements SamradProjektApi {

    @NonNull
    SamradProjektidService samradProjektIdService;

    @Override
    @RolesAllowed(MarkkollRole.SAMRAD_ADMIN)
    public ResponseEntity<Void> deleteSamrad(String kundId, String samradId) throws Exception {
                samradProjektIdService.deleteBySamradId(samradId);
                return ResponseEntity.ok().build();
    }

    @Override
    @RolesAllowed(MarkkollRole.SAMRAD_ADMIN)
    public ResponseEntity<SamradDto> getByMarkkollId(String kundId, String markkollId) throws Exception {

        SamradDto samradDto=samradProjektIdService.getByMarkkollId(markkollId);
        return ResponseEntity.ok().body(samradDto);
    }

    @Override
    @RolesAllowed(MarkkollRole.SAMRAD_ADMIN)
    public ResponseEntity<List<SamradDto>> listSamradiD(String kundId) throws Exception {
        List<SamradDto> samradDtoList=samradProjektIdService.listAllSamradId(kundId);
        return ResponseEntity.ok().body(samradDtoList);
    }

    @Override
    @RolesAllowed(MarkkollRole.SAMRAD_ADMIN)
    public ResponseEntity<SamradDto> saveSamradiD(String kundId, UUID markkollId, SamradDto samradDto) throws Exception {
        samradDto=samradProjektIdService.addSamradId(kundId,markkollId,samradDto);
        return ResponseEntity.ok().body(samradDto);
    }
}
