package se.metria.markkoll.restapi;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.metria.markkoll.openapi.api.UserApi;
import se.metria.markkoll.openapi.model.MarkkollUserDto;
import se.metria.markkoll.openapi.model.UserInfoDto;
import se.metria.markkoll.service.admin.UserService;

import javax.validation.Valid;

@RequestMapping(value = "/api")
@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController implements UserApi {

    @NonNull
    private final UserService userService;

    @Override
    @PreAuthorize("hasUserPermission(#userId, 'ADMINISTRATION') || hasRole('admin')")
    public ResponseEntity<Void> deleteUser(String userId) {
        userService.delete(userId);

        return ResponseEntity.ok().build();
    }

    @Override
    @PreAuthorize("hasUserPermission(#userId, 'KUND_READ')")
    public ResponseEntity<MarkkollUserDto> getUser(String userId) {
        var user = userService.get(userId);

        return ResponseEntity.ok(user);
    }

    @Override
    @PreAuthorize("hasUserPermission(#userId, 'ADMINISTRATION')")
    public ResponseEntity<MarkkollUserDto> updateUserInfo(String userId, @Valid UserInfoDto updatedUserInfo) {
        var updated = userService.updateUserInfo(userId, updatedUserInfo);

        return ResponseEntity.ok(updated);
    }
}
