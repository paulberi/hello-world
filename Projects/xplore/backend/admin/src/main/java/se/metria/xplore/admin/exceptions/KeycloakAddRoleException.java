package se.metria.xplore.admin.exceptions;

import se.metria.xplore.admin.model.CreateUsersReportResponses;

public class KeycloakAddRoleException extends Exception {
    private CreateUsersReportResponses.Action action;

    public KeycloakAddRoleException(String errorMessage, CreateUsersReportResponses.Action action) {
        super(errorMessage);
        this.action = action;
    }

    public CreateUsersReportResponses.Action getAction() {
        return this.action;
    }
}
