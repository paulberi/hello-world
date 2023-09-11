package se.metria.xplore.configuration.model;

import com.vladmihalcea.hibernate.type.array.StringArrayType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

@Entity
@Table(name = "app_config_restriction", schema="config")
@SequenceGenerator(name = "app_config_restriction_seq", sequenceName = "app_config_restriction_seq", allocationSize = 1)
@TypeDef(name = "string-array", typeClass = StringArrayType.class)
public class AppConfigRestriction {

    @Id
    @GeneratedValue(generator = "app_config_restriction_seq")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_config_id")
    private AppConfig appConfig;

    @Type(type = "string-array")
    @Column(name = "realms", columnDefinition = "text[]")
    public String[] realms;

    @Type(type = "string-array")
    @Column(name = "clients", columnDefinition = "text[]")
    public String[] clients;

    @Type(type = "string-array")
    @Column(name = "users", columnDefinition = "text[]")
    public String[] users;

    @Type(type = "string-array")
    @Column(name = "groups", columnDefinition = "text[]")
    public String[] groups;

    @Type(type = "string-array")
    @Column(name = "roles", columnDefinition = "text[]")
    public String[] roles;

    public AppConfigRestriction() {

    }

    public AppConfigRestriction(String[] realms, String[] clients, String[] users, String[] groups, String[] roles) {
        this.realms = realms;
        this.clients = clients;
        this.users = users;
        this.groups = groups;
        this.roles = roles;
    }

    public AppConfig getAppConfig() {
        return appConfig;
    }

    public void setAppConfig(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    public String[] getRealms() {
        return realms;
    }

    public void setRealms(String[] realms) {
        this.realms = realms;
    }

    public String[] getClients() {
        return clients;
    }

    public void setClients(String[] clients) {
        this.clients = clients;
    }

    public String[] getUsers() {
        return users;
    }

    public void setUsers(String[] users) {
        this.users = users;
    }

    public String[] getGroups() {
        return groups;
    }

    public void setGroups(String[] groups) {
        this.groups = groups;
    }

    public String[] getRoles() {
        return roles;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
    }
}
