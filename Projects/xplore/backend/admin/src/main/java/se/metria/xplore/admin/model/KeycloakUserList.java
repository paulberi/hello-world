package se.metria.xplore.admin.model;

import java.util.ArrayList;

public class KeycloakUserList {
    private ArrayList<KeycloakUser> users;
    private Integer totalElements;

    public KeycloakUserList(ArrayList<KeycloakUser> users, Integer totalElements) {
        this.users = users;
        this.totalElements = totalElements;
    }

    public ArrayList<KeycloakUser> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<KeycloakUser> users) {
        this.users = users;
    }

    public Integer getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Integer totalElements) {
        this.totalElements = totalElements;
    }
}
