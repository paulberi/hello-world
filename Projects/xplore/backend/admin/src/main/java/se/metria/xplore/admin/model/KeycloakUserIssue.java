package se.metria.xplore.admin.model;

import java.util.ArrayList;

public class KeycloakUserIssue {
    public static final String USERNAME_CONFLICT = "USERNAME_CONFLICT";
    public static final String EMAIL_CONFLICT = "EMAIL_CONFLICT";
    public static final String ROLES_MISSING = "ROLES_MISSING";
    public static final String FILE_USERNAME_DUPLICATION = "FILE_USERNAME_DUPLICATION";
    public static final String FILE_EMAIL_DUPLICATION = "FILE_EMAIL_DUPLICATION";

    private KeycloakUser user;
    private ArrayList<String> issues;
    private String message;

    public KeycloakUserIssue(KeycloakUser user) {
        this.user = user;
        this.issues = new ArrayList<>();
        this.message = "";
    }

    public KeycloakUser getUser() {
        return this.user;
    }

    public ArrayList<String> getIssues() {
        return this.issues;
    }

    public void addIssue(String issue) {
        this.issues.add(issue);
    }

    public boolean hasIssue(String issue) {
        boolean res = false;
        for (String i : this.issues) {
            if (i.equals(issue)) {
                res = true;
            }
        }

        return res;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
