package se.metria.xplore.configuration.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonNodeBinaryType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "app_config", schema="config")
@TypeDef(name = "jsonb-node", typeClass = JsonNodeBinaryType.class)
@SequenceGenerator(name = "app_config_seq", sequenceName = "app_config_seq", allocationSize = 1)
public class AppConfig {

    @Id
    @GeneratedValue(generator = "app_config_seq")
    private Long id;

    private String name;

    private String description;

    @Type(type = "jsonb-node")
    @Column(name = "json", columnDefinition = "jsonb")
    private JsonNode json;

    @OneToMany(mappedBy = "appConfig", cascade = CascadeType.ALL)
    private Set<AppConfigRestriction> appConfigRestrictions = new HashSet<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public JsonNode getJson() {
        return json;
    }

    public void setJson(JsonNode json) {
        this.json = json;
    }

    public Set<AppConfigRestriction> getAppConfigRestrictions() {
        return appConfigRestrictions;
    }

    public void setAppConfigRestrictions(Set<AppConfigRestriction> appConfigRestrictions) {
        this.appConfigRestrictions = appConfigRestrictions;
    }

    public void addAccessRestriction(AppConfigRestriction appConfigRestriction) {
        appConfigRestriction.setAppConfig(this);
        this.appConfigRestrictions.add(appConfigRestriction);
    }
}
