package se.metria.xplore.admin.model;

import java.util.ArrayList;
import java.util.UUID;

public class KeycloakUser {
    private UUID uuid;
    private String username;
    private String email;
    private String firstname;
    private String lastname;
    private ArrayList<KeycloakRole> roles;
    private boolean enabled;
    private String tempPassword; //Only for setting temporary password when creating user. Should NEVER be read from anywhere else than input file.

    public KeycloakUser(String username, String email, String firstname, String lastname, ArrayList<KeycloakRole> roles, boolean enabled, String tempPassword) {
        this.uuid = UUID.randomUUID();
        this.username = username;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.roles = roles;
        this.enabled = enabled;
        this.tempPassword = tempPassword;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public String getUsername() {
        return this.username;
    }

    public String getEmail() {
        return this.email;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public String getLastname() {
        return this.lastname;
    }

    public ArrayList<KeycloakRole> getRoles() {
        return this.roles;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public String getTempPassword() {
        return this.tempPassword;
    }

    public String toString() {
        String output = "";
        output += "username: " + this.username + "\n";
        output += "email: " + this.email + "\n";
        output += "firstname: " + this.firstname + "\n";
        output += "lastname: " + this.lastname + "\n";
        output += "roles: ";
        int i = 0;
        for (KeycloakRole role: this.roles) {
            output += role.getName();
            if (i < roles.size() - 1) {
                output += ", ";
            }
            i++;
        }
        output += "\n";
        output += "enabled: " + this.enabled + "\n";
        if (!this.tempPassword.isEmpty()) {
            output += "temporary password IS set";
        } else {
            output += "temporary password is NOT set";
        }
        return output;
    }
}
