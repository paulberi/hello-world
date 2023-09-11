package se.metria.xplore.admin.keycloak;

import org.keycloak.KeycloakSecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.metria.xplore.admin.exceptions.IncompatibleOutputTypeException;
import se.metria.xplore.admin.model.*;
import se.metria.xplore.admin.openapi.api.KeycloakApi;
import se.metria.xplore.admin.openapi.model.*;
import se.metria.xplore.admin.security.AdminApiRole;
import se.metria.xplore.admin.util.ExcelReader;
import se.metria.xplore.admin.util.WorkbookFactoryWrapper;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.ArrayList;

@RolesAllowed(AdminApiRole.ADMIN_API_USER)
@RequestMapping(value = "/api")
@RestController
@ConditionalOnProperty(prefix = "admin.console", name = "enabled", matchIfMissing = true, havingValue="false")
@CrossOrigin(origins = "*")
public class KeycloakController implements KeycloakApi {
    Logger logger = LoggerFactory.getLogger(KeycloakController.class);

    //Autowired for value to be set for every request. Used to get jwt to pass on to KeyCloak.
    @Autowired
    private HttpServletRequest request;
    private KeycloakService service;

    public KeycloakController(KeycloakService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<RealmListDto> keycloakRealmsGet() {
        KeycloakSecurityContext securityContext = (KeycloakSecurityContext) request
                .getAttribute(KeycloakSecurityContext.class.getName());
        String jwt = securityContext.getTokenString();

        ArrayList<KeycloakRealm> realms = service.listRealms(jwt);

        RealmListDto res = new RealmListDto();
        for (KeycloakRealm realm : realms) {
            res.addRealmsItem(new RealmInfoDto().displayName(realm.getDisplayName()).name(realm.getName()));
        }

        return ResponseEntity.ok(res);
    }

    @Override
    public ResponseEntity<UserListDto> keycloakRealmsRealmUsersGet(String realm,
                                                                   Integer first,
                                                                   Integer max) {
        KeycloakSecurityContext securityContext = (KeycloakSecurityContext) request
                .getAttribute(KeycloakSecurityContext.class.getName());
        String jwt = securityContext.getTokenString();

        KeycloakUserList usersList = service.listUsers(realm, jwt, first, max);

        UserListDto res = new UserListDto();

        res.setTotalElements(usersList.getTotalElements());

        for (KeycloakUser user : usersList.getUsers()) {
            ArrayList<RoleInfoDto> roles = new ArrayList<>();
            for (KeycloakRole role : user.getRoles()) {
                roles.add(new RoleInfoDto().name(role.getName()).description(role.getDescription()));
            }

            res.addUsersItem(new UserDetailsDto()
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .firstname(user.getFirstname())
                    .lastname(user.getLastname())
                    .roles(roles)
                    .enabled(user.isEnabled()));
        }

        return ResponseEntity.ok(res);
    }

    @Override
    public ResponseEntity<CreateUsersReportDto> keycloakRealmsRealmUsersPost(
            String realm,
            @NotNull @Valid Boolean dryRun,
            @Valid Integer skipFirstRows,
            @Valid Integer numberOfSheets,
            @Valid Resource body) {
        //Get request jwt for passing to Keycloak
        KeycloakSecurityContext securityContext = (KeycloakSecurityContext) request
                .getAttribute(KeycloakSecurityContext.class.getName());
        String jwt = securityContext.getTokenString();

        int skip = (skipFirstRows == null) ? 0 : skipFirstRows;
        int sheets = (numberOfSheets == null) ? -1 : numberOfSheets;

        //Get users from file
        ExcelReader reader = new ExcelReader(new WorkbookFactoryWrapper());
        ArrayList<KeycloakUser> users;
        try {
            users = reader.excelToObjects(body.getInputStream(), skip, sheets, KeycloakUser.class);
        } catch (IOException | IncompatibleOutputTypeException e) {
            this.logger.error("Failed to read input file", e);
            return ResponseEntity.badRequest().body(new CreateUsersReportDto().message(CreateUsersReportResponses.Message.READ_FILE_ERROR));
        }

        //Validate user input data
        ArrayList<KeycloakUserIssue> issues = this.service.validateUsers(users, realm, jwt);

        CreateUsersReportDto res = new CreateUsersReportDto();
        if (dryRun) {
            boolean errors = false;
            for (KeycloakUser user : users) {
                CreateUserStatusDto status = new CreateUserStatusDto();

                status.user(this.buildUserDetailsObject(user));

                status.message("");

                //Handle conflicts
                boolean update = false;
                boolean fail = false;
                for (KeycloakUserIssue issue : issues) {
                    if (issue.getUser().getUuid().equals(user.getUuid())) {
                        if (issue.hasIssue(KeycloakUserIssue.USERNAME_CONFLICT)) {
                            update = true;
                        }

                        if (issue.hasIssue(KeycloakUserIssue.EMAIL_CONFLICT)) {
                            fail = true;
                            status.message(status.getMessage() + CreateUsersReportResponses.Message.VALIDATE_EMAIL_CONFLICT + "\n");
                        }

                        if (issue.hasIssue(KeycloakUserIssue.ROLES_MISSING)) {
                            fail = true;
                            status.message(status.getMessage() + CreateUsersReportResponses.Message.VALIDATE_ROLES_MISSING + issue.getMessage() + ".\n");
                        }

                        if (issue.hasIssue(KeycloakUserIssue.FILE_USERNAME_DUPLICATION)) {
                            fail = true;
                            status.message(status.getMessage() + CreateUsersReportResponses.Message.VALIDATE_DUPLICATE_USERNAME_FILE_CONFLICT + "\n");
                        }

                        if (issue.hasIssue(KeycloakUserIssue.FILE_EMAIL_DUPLICATION)) {
                            fail = true;
                            status.message(status.getMessage() + CreateUsersReportResponses.Message.VALIDATE_DUPLICATE_EMAIL_FILE_CONFLICT + "\n");
                        }
                    }
                }

                if (update) {
                    status.action(CreateUserStatusDto.ActionEnum.valueOf(CreateUsersReportResponses.Action.UPDATE.name()));
                } else {
                    status.action(CreateUserStatusDto.ActionEnum.valueOf(CreateUsersReportResponses.Action.CREATE.name()));
                }

                if (fail) {
                    status.status(CreateUserStatusDto.StatusEnum.valueOf(CreateUsersReportResponses.Status.FAIL.name()));
                    errors = true;
                } else {
                    status.status(CreateUserStatusDto.StatusEnum.valueOf(CreateUsersReportResponses.Status.SUCCESS.name()));
                }

                res.addStatusesItem(status);
            }

            if (errors) {
                res.message(CreateUsersReportResponses.Message.VALIDATE_FAIL);
            } else {
                res.message(CreateUsersReportResponses.Message.VALIDATE_SUCCESS);
            }
        } else {
            ArrayList<KeycloakCreateUserResponse> responses = this.service.createUsers(users, realm, jwt);
            boolean errors = false;
            for (KeycloakCreateUserResponse response : responses) {
                CreateUserStatusDto status = new CreateUserStatusDto()
                        .user(this.buildUserDetailsObject(response.getUser()))
                        .action(CreateUserStatusDto.ActionEnum.valueOf(response.getAction().name()))
                        .status(CreateUserStatusDto.StatusEnum.valueOf(response.getStatus().name()))
                        .message(response.getMessage());

                res.addStatusesItem(status);

                if (response.isError()) {
                    errors = true;
                }
            }

            if (errors) {
                res.message(CreateUsersReportResponses.Message.CREATE_ERROR);
            } else {
                res.message(CreateUsersReportResponses.Message.CREATE_SUCCESS);
            }
        }

        return ResponseEntity.ok(res);
    }

    private UserDetailsDto buildUserDetailsObject(KeycloakUser user) {
        UserDetailsDto userDetails = new UserDetailsDto();

        ArrayList<RoleInfoDto> roles = new ArrayList<>();
        for (KeycloakRole role : user.getRoles()) {
            roles.add(new RoleInfoDto().name(role.getName()).description(role.getDescription()));
        }

        userDetails
                .username(user.getUsername())
                .email(user.getEmail())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .roles(roles)
                .enabled(user.isEnabled())
                .resetPassword(!user.getTempPassword().isEmpty());

        return userDetails;
    }
}
