package se.metria.markkoll.service.geofence.model;

public enum Role {
    ADMIN("ADMIN");

    private final String role;

    Role(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return String.valueOf(role);
    }
}
