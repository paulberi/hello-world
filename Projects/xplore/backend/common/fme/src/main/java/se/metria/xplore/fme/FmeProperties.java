package se.metria.xplore.fme;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("fme")
public class FmeProperties {
    private String url;
    private String fileUrl;
    private String username;
    private String password;
    private int connectTimeout;
    private int readTimeout;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String url) {
        this.fileUrl = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }
}
