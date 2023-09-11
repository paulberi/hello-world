package se.metria.xplore.configuration.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonNodeBinaryType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@TypeDef(name = "jsonb-node", typeClass = JsonNodeBinaryType.class)
@Table(name = "app_login_info", schema="config")
public class LoginInfo {
    @EmbeddedId
    private ClientId clientId;

    @Type(type = "jsonb-node")
    @Column(name = "json", columnDefinition = "jsonb")
    private JsonNode json;

    public LoginInfo() {
    }

    public JsonNode getJson() {
        return json;
    }

    public void setJson(JsonNode json) {
        this.json = json;
    }
}
