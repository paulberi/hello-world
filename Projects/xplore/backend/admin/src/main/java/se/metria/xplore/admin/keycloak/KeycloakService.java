package se.metria.xplore.admin.keycloak;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import se.metria.xplore.admin.AdminService;
import se.metria.xplore.admin.exceptions.KeycloakAddRoleException;
import se.metria.xplore.admin.exceptions.KeycloakCreateUserException;
import se.metria.xplore.admin.exceptions.KeycloakResetPasswordException;
import se.metria.xplore.admin.exceptions.KeycloakUpdateUserException;
import se.metria.xplore.admin.model.*;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
public class KeycloakService {
    Logger logger = LoggerFactory.getLogger(AdminService.class);

    private KeycloakProperties properties;
    private KeycloakFactory factory;

    public KeycloakService(KeycloakProperties properties, KeycloakFactory factory) {
        this.properties = properties;
        this.factory = factory;
    }

    /**
     * Lists realms on KeyCloak server.
     * @param username (String) Admin username for KeyCloak.
     * @param password (String) Admin password for Keycloak.
     * @return ArrayList(KeycloakRealm) List of realms present on server.
     */
    public ArrayList<KeycloakRealm> listRealms(String username, String password) {
        Keycloak keycloak = this.factory.getMasterClient(this.properties, username, password);
        ArrayList<KeycloakRealm> serverRealms = listRealms(keycloak);
        keycloak.close();

        return serverRealms;
    }

    /**
     * Lists realms on KeyCloak server.
     * @param token (String) Access token from KeyCloak server.
     * @return ArrayList(KeycloakRealm) List of realms present on server.
     */
    public ArrayList<KeycloakRealm> listRealms(String token) {
        Keycloak keycloak = this.factory.getMasterClient(this.properties, token);
        ArrayList<KeycloakRealm> serverRealms = listRealms(keycloak);
        keycloak.close();

        return serverRealms;
    }

    /**
     * Lists users in realm on KeyCloak server.
     * @param realm (String) Name of realm to list.
     * @param username (String) Admin username for KeyCloak.
     * @param password (String) Admin password for KeyCloak.
     * @return ArrayList(KeycloakUser) List of users in realm present on server.
     */
    public ArrayList<KeycloakUser> listUsers(String realm, String username, String password) {
        Keycloak keycloak = this.factory.getClient(this.properties, realm, username, password);
        KeycloakUserList serverUsers = listUsers(keycloak, realm, 0, 100);
        keycloak.close();

        return serverUsers.getUsers();
    }

    /**
     * Lists users in realm on KeyCloak server.
     * @param realm (String) Name of realm to list.
     * @param token (String) Access token from KeyCloak server.
     * @return ArrayList(KeycloakUser) List of users in realm present on server.
     */
    public KeycloakUserList listUsers(String realm, String token, Integer first, Integer max) {
        Keycloak keycloak = this.factory.getClient(this.properties, realm, token);
        KeycloakUserList serverUsers = listUsers(keycloak, realm, first, max);
        keycloak.close();

        return serverUsers;
    }

    /**
     * Lists users on KeyCloak server with same username as input list.
     * @param fileUsers ArrayList(KeycloakUser) List of users to compare to users on server.
     * @param realm (String) Name of realm to check.
     * @param username (String) Admin username for KeyCloak.
     * @param password (String) Admin password for KeyCloak.
     * @return ArrayList(KeycloakUserIssue) List of users in input with issues such as name conflict or missing roles.
     */
    public ArrayList<KeycloakUserIssue> validateUsers(ArrayList<KeycloakUser> fileUsers, String realm, String username, String password) {
        Keycloak keycloak = this.factory.getClient(this.properties, realm, username, password);
        ArrayList<KeycloakUserIssue> conflicts = validateUsers(fileUsers, keycloak, realm);
        keycloak.close();

        return conflicts;
    }

    /**
     * Lists users on KeyCloak server with same username as input list.
     * @param fileUsers ArrayList(KeycloakUser) List of users to compare to users on server.
     * @param realm (String) Name of realm to check.
     * @param token (String) Access token from KeyCloak server.
     * @return ArrayList(KeycloakUserIssue) List of users in input with issues such as name conflict or missing roles.
     */
    public ArrayList<KeycloakUserIssue> validateUsers(ArrayList<KeycloakUser> fileUsers, String realm, String token) {
        Keycloak keycloak = this.factory.getClient(this.properties, realm, token);
        ArrayList<KeycloakUserIssue> conflicts = validateUsers(fileUsers, keycloak, realm);
        keycloak.close();

        return conflicts;
    }

    /**
     * Creates users on KeyCloak server, updates if they already exist.
     * @param fileUsers ArrayList(KeycloakUser) List of users to add.
     * @param realm (String) Name of realm to add users.
     * @param username (String) Admin username for KeyCloak.
     * @param password (String) Admin password for KeyCloak.
     * @return ArrayList(KeycloakCreateUserResponse) List of responses from create user requests.
     */
    public ArrayList<KeycloakCreateUserResponse> createUsers(ArrayList<KeycloakUser> fileUsers, String realm, String username, String password) {
        Keycloak keycloak = this.factory.getClient(this.properties, realm, username, password);
        ArrayList<KeycloakCreateUserResponse> responses = createUsers(fileUsers, keycloak, realm);
        keycloak.close();

        return responses;
    }

    /**
     * Creates users on KeyCloak server, updates if they already exist.
     * @param fileUsers ArrayList(KeycloakUser) List of users to add.
     * @param realm (String) Name of realm to add users.
     * @param token (String) Access token from KeyCloak server.
     * @return ArrayList(KeycloakCreateUserResponse) List of responses from create user requests.
     */
    public ArrayList<KeycloakCreateUserResponse> createUsers(ArrayList<KeycloakUser> fileUsers, String realm, String token) {
        Keycloak keycloak = this.factory.getClient(this.properties, realm, token);
        ArrayList<KeycloakCreateUserResponse> responses = createUsers(fileUsers, keycloak, realm);
        keycloak.close();

        return responses;
    }

    /**
     * Lists roles in realm on KeyCloak server.
     * @param realm (String) Name of realm to list.
     * @param username (String) Admin username for KeyCloak.
     * @param password (String) Admin password for KeyCloak.
     * @return ArrayList(String) List of roles present in realm on server.
     */
    public ArrayList<String> listRoles(String realm, String username, String password) {
        Keycloak keycloak = this.factory.getClient(this.properties, realm, username, password);
        ArrayList<String> roles = listRoles(keycloak, realm);
        keycloak.close();

        return roles;
    }

    /**
     * Lists roles in realm on KeyCloak server.
     * @param realm (String) Name of realm to list.
     * @param token (String) Access token from KeyCloak server.
     * @return ArrayList(String) List of roles present in realm on server.
     */
    public ArrayList<String> listRoles(String realm, String token) {
        Keycloak keycloak = this.factory.getClient(this.properties, realm, token);
        ArrayList<String> roles = listRoles(keycloak, realm);
        keycloak.close();

        return roles;
    }

    private ArrayList<KeycloakRealm> listRealms(Keycloak keycloak) {
        ArrayList<KeycloakRealm> realms = new ArrayList<>();

        List<RealmRepresentation> realmRepresentations = keycloak.realms().findAll();

        for (RealmRepresentation representation : realmRepresentations) {
            String name = representation.getRealm();
            String displayName = representation.getDisplayName();
            realms.add(new KeycloakRealm(name, displayName));
        }

        return realms;
    }

    private KeycloakUserList listUsers(Keycloak keycloak, String realm, Integer first, Integer max) {
        ArrayList<KeycloakUser> serverUsers = new ArrayList<>();

        UsersResource usersResource = keycloak.realm(realm).users();

        Integer count = usersResource.count();

        List<UserRepresentation> users = usersResource.list(first,max);

        for (UserRepresentation user : users) {
            String uname = user.getUsername();
            String email = (user.getEmail() == null) ? "" : user.getEmail();
            String fname = (user.getFirstName() == null) ? "" : user.getFirstName();
            String lname = (user.getLastName() == null) ? "" : user.getLastName();

            //Roles currently needs to be fetched separately as they are not present in the user listing
            List<RoleRepresentation> roleRepresentations = usersResource.get(user.getId()).roles().realmLevel().listAll();
            ArrayList<KeycloakRole> roles = new ArrayList<>();
            for (RoleRepresentation roleRepresentation : roleRepresentations) {
                roles.add(new KeycloakRole(roleRepresentation.getName(), roleRepresentation.getDescription()));
            }

            boolean enabled = (user.isEnabled() == null) ? false : user.isEnabled();

            serverUsers.add(new KeycloakUser(uname, email, fname, lname, roles, enabled, ""));
        }
        keycloak.close();

        return new KeycloakUserList(serverUsers, count);
    }

    private ArrayList<KeycloakUserIssue> validateUsers(ArrayList<KeycloakUser> fileUsers, Keycloak keycloak, String realm) {
        ArrayList<KeycloakUserIssue> issues = new ArrayList<>();
        HashMap<String, String> usernames = new HashMap<>();
        HashMap<String, String> emails = new HashMap<>();

        for (KeycloakUser user: fileUsers) {
            KeycloakUserIssue issue = new KeycloakUserIssue(user);

            //Check for username duplications in file.
            if (!usernames.containsKey(user.getUsername())) {
                usernames.put(user.getUsername(), "");
            } else {
                issue.addIssue(KeycloakUserIssue.FILE_USERNAME_DUPLICATION);
            }

            //Check for email duplications in file.
            if (!emails.containsKey(user.getEmail())) {
                emails.put(user.getEmail(), "");
            } else {
                issue.addIssue(KeycloakUserIssue.FILE_EMAIL_DUPLICATION);
            }

            //If there exists a user with the same username that user should probably be updated rather than creating a new.
            if (keycloak.realm(realm).users().search(user.getUsername(), true).size() >  0) {
                issue.addIssue(KeycloakUserIssue.USERNAME_CONFLICT);
            }

            //Check if users with email exist. Max 2 results as one may be username and one may be email since both are unique.
            List<UserRepresentation> usersByEmail = keycloak.realm(realm).users().search(user.getEmail(), 0, 2);
            if (usersByEmail.size() > 0) {
                for (UserRepresentation ube : usersByEmail) {
                    //We are trying to assign an existing email to another user if there is another user with the same
                    // email but another username.
                    if (ube.getEmail().equals(user.getEmail()) && !ube.getUsername().equals(user.getUsername())) {
                        issue.addIssue(KeycloakUserIssue.EMAIL_CONFLICT);
                    }
                }
            }

            //Check if any roles we are trying to assign does not exist on server.
            for (KeycloakRole role : user.getRoles()) {
                boolean roleMissing = false;

                try {
                    String roleName = keycloak.realm(realm).roles().get(role.getName()).toRepresentation().getName();
                } catch (NotFoundException e) {
                    roleMissing = true;
                    if (issue.getMessage().equals("")) {
                        issue.setMessage(role.getName());
                    } else {
                        issue.setMessage(issue.getMessage() + ", " + role.getName());
                    }
                }

                if (roleMissing) {
                    issue.addIssue(KeycloakUserIssue.ROLES_MISSING);
                }
            }

            if (issue.getIssues().size() > 0) {
                issues.add(issue);
            }
        }

        return issues;
    }

    private ArrayList<KeycloakCreateUserResponse> createUsers(ArrayList<KeycloakUser> fileUsers, Keycloak keycloak, String realm) {
        ArrayList<KeycloakCreateUserResponse> responses = new ArrayList<>();
        ArrayList<KeycloakUserIssue> conflicts = validateUsers(fileUsers, keycloak, realm);

        for (KeycloakUser user : fileUsers) {
            boolean update = false;
            for (KeycloakUserIssue con : conflicts) {
                if (con.getUser().getUsername().equals(user.getUsername()) &&
                        con.hasIssue(KeycloakUserIssue.USERNAME_CONFLICT)) {
                    update = true;
                    break;
                }
            }

            UserRepresentation userRepresentation = new UserRepresentation();
            userRepresentation.setEmail(user.getEmail());
            userRepresentation.setFirstName(user.getFirstname());
            userRepresentation.setLastName(user.getLastname());
            userRepresentation.setEnabled(user.isEnabled());
            CredentialRepresentation passwordCred = new CredentialRepresentation();
            passwordCred.setTemporary(true);
            passwordCred.setType(CredentialRepresentation.PASSWORD);
            passwordCred.setValue(user.getTempPassword());
            String userId;

            CreateUsersReportResponses.Action action;
            CreateUsersReportResponses.Status status;
            String message = "";
            try {
                if (!update) {
                    userRepresentation.setUsername(user.getUsername());
                    Response response;
                    try {
                        response = keycloak.realm(realm).users().create(userRepresentation);
                    } catch (Exception e) {
                        throw new KeycloakCreateUserException(e.getMessage(), CreateUsersReportResponses.Action.CREATE);
                    }
                    userId = this.factory.getCreatedUserId(response);
                    if (!passwordCred.getValue().isEmpty()) {
                        try {
                            UserResource resource = keycloak.realm(realm).users().get(userId);
                            resource.resetPassword(passwordCred);
                        } catch (Exception e) {
                            throw new KeycloakResetPasswordException(e.getMessage(), CreateUsersReportResponses.Action.CREATE);
                        }
                    }
                    action = CreateUsersReportResponses.Action.CREATE;
                    status = CreateUsersReportResponses.Status.SUCCESS;
                    message = "";
                } else {
                    UserResource resource;
                    try {
                        userId = keycloak.realm(realm).users().search(user.getUsername(), true).get(0).getId();
                        resource = keycloak.realm(realm).users().get(userId);
                        resource.update(userRepresentation);
                    } catch (Exception e) {
                        throw new KeycloakUpdateUserException(e.getMessage(), CreateUsersReportResponses.Action.UPDATE);
                    }
                    if (!passwordCred.getValue().isEmpty()) {
                        try {
                            resource.resetPassword(passwordCred);
                        } catch (Exception e) {
                            throw new KeycloakResetPasswordException(e.getMessage(), CreateUsersReportResponses.Action.UPDATE);
                        }
                    }
                    action = CreateUsersReportResponses.Action.UPDATE;
                    status = CreateUsersReportResponses.Status.SUCCESS;
                    message = "";
                }

                //Add roles. Might want to remove roles not in input file in future?
                for (KeycloakRole role : user.getRoles()) {
                    try {
                        keycloak.realm(realm).users().get(userId).roles().realmLevel()
                                .add(Arrays.asList(keycloak.realm(realm).roles().get(role.getName()).toRepresentation()));
                    } catch (Exception e) {
                        throw new KeycloakAddRoleException(e.getMessage(), update ? CreateUsersReportResponses.Action.UPDATE : CreateUsersReportResponses.Action.CREATE);
                    }
                }

                responses.add(new KeycloakCreateUserResponse(user, action, status, message, false));
            } catch (KeycloakCreateUserException e) {
                this.logger.error("Error creating Keycloak user.", e);
                responses.add(new KeycloakCreateUserResponse(user, e.getAction(), CreateUsersReportResponses.Status.FAIL, CreateUsersReportResponses.Message.USER_CREATE_ERROR, true));
            } catch (KeycloakAddRoleException e) {
                this.logger.error("Error adding roles to Keycloak user.", e);
                responses.add(new KeycloakCreateUserResponse(user, e.getAction(), CreateUsersReportResponses.Status.WARNING, CreateUsersReportResponses.Message.USER_ADD_ROLES_ERROR, true));
            } catch (KeycloakUpdateUserException e) {
                this.logger.error("Error updating Keycloak user.", e);
                responses.add(new KeycloakCreateUserResponse(user, e.getAction(), CreateUsersReportResponses.Status.FAIL, CreateUsersReportResponses.Message.USER_UPDATE_ERROR, true));
            } catch (KeycloakResetPasswordException e) {
                this.logger.error("Error, resetting password for Keycloak user.", e);
                responses.add(new KeycloakCreateUserResponse(user, e.getAction(), CreateUsersReportResponses.Status.WARNING, CreateUsersReportResponses.Message.USER_RESET_PASSWORD_ERROR, true));
            } catch (Exception e) {
                this.logger.error("Unknown Keycloak error.", e);
                responses.add(new KeycloakCreateUserResponse(user, CreateUsersReportResponses.Action.UNKNOWN, CreateUsersReportResponses.Status.UNKNOWN, CreateUsersReportResponses.Message.USER_UNKNOWN_ERROR, true));
            }
        }

        return responses;
    }

    private ArrayList<String> listRoles(Keycloak keycloak, String realm) {
        ArrayList<String> roles = new ArrayList<>();

        List<RoleRepresentation> roleRepresentations = keycloak.realm(realm).roles().list();
        for (RoleRepresentation role : roleRepresentations) {
            roles.add(role.getName());
        }

        return roles;
    }
}
