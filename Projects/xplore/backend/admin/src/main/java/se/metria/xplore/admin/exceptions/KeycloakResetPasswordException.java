package se.metria.xplore.admin.exceptions;

import se.metria.xplore.admin.model.CreateUsersReportResponses;

public class KeycloakResetPasswordException extends Exception {
    private CreateUsersReportResponses.Action action;

    public KeycloakResetPasswordException(String errorMessage, CreateUsersReportResponses.Action action) {
        super(errorMessage);
        this.action = action;
    }

    public CreateUsersReportResponses.Action getAction() {
        return this.action;
    }
}
