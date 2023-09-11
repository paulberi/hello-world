package se.metria.xplore.admin.model;

public class KeycloakRole {
    private String name;
    private String description;

    public KeycloakRole(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }
}
