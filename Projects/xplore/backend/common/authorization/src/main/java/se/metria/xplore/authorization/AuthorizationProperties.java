package se.metria.xplore.authorization;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("authorization")
public class AuthorizationProperties {
    private String certsUrl;
    private boolean disabled;

    public String getCertsUrl() {
        return certsUrl;
    }

    public void setCertsUrl(String certsUrl) {
        this.certsUrl = certsUrl;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
}
