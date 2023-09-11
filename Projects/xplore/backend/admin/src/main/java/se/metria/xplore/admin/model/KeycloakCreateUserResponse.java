package se.metria.xplore.admin.model;

public class KeycloakCreateUserResponse {
    private KeycloakUser user;
    private CreateUsersReportResponses.Action action;
    private CreateUsersReportResponses.Status status;
    private String message;
    private boolean error;

    public KeycloakCreateUserResponse(KeycloakUser user, CreateUsersReportResponses.Action action, CreateUsersReportResponses.Status status, String message, boolean error) {
        this.user = user;
        this.action = action;
        this.status = status;
        this.message = message;
        this.error = error;
    }

    public KeycloakUser getUser() {
        return this.user;
    }

    public CreateUsersReportResponses.Action getAction() {
        return this.action;
    }

    public CreateUsersReportResponses.Status getStatus() {
        return this.status;
    }

    public String getMessage() {
        return this.message;
    }

    public boolean isError() {
        return this.error;
    }
}
