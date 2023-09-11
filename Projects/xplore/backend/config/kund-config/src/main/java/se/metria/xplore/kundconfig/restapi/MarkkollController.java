package se.metria.xplore.kundconfig.restapi;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.metria.xplore.kundconfig.openapi.api.MarkkollApi;
import se.metria.xplore.kundconfig.openapi.model.MarkkollUserDto;
import se.metria.xplore.kundconfig.openapi.model.UserInfoDto;
import se.metria.xplore.kundconfig.security.KundConfigRole;
import se.metria.xplore.kundconfig.service.MarkkollService;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api")
@RequiredArgsConstructor
public class MarkkollController implements MarkkollApi {
    @NonNull
    private final MarkkollService markkollService;
    
    @Override
    @RolesAllowed(KundConfigRole.GLOBAL_ADMIN)
    public ResponseEntity<Void> createKundAdmin(String kundId, @Valid UserInfoDto userInfoDto) throws Exception {
        markkollService.createKundAdmin(kundId, userInfoDto);
        return ResponseEntity.ok().build();
    }

    @Override
    @RolesAllowed(KundConfigRole.GLOBAL_ADMIN)
    public ResponseEntity<List<MarkkollUserDto>> getUsersForKund(String kundId) {

        var users = markkollService.getMarkkollKundAdminsForKund(kundId);
        return ResponseEntity.ok(users);
    }

    @Override
    @RolesAllowed(KundConfigRole.GLOBAL_ADMIN)
    public ResponseEntity<Void> deleteUser(String id) {
        markkollService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}
