package se.metria.xplore.admin.model;

public class KeycloakRealm {
    private String name;
    private String displayName;

    public KeycloakRealm(String name, String displayName) {
        this.name = name;
        this.displayName = displayName;
    }

    public String getName() {
        return this.name;
    }

    public String getDisplayName() {
        return this.displayName;
    }
}
