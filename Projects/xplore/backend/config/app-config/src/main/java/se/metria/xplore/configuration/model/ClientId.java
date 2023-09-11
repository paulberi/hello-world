package se.metria.xplore.configuration.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class ClientId implements Serializable {
    @Column
    private String realm;

    @Column
    private String clientId;

    public ClientId(String realm, String clientId) {
        this.realm = realm;
        this.clientId = clientId;
    }

    public ClientId() {
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
