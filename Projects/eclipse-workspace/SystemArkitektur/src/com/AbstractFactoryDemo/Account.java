package com.AbstractFactoryDemo;

public class Account {
    private String username;
    private String platform;

    public Account(String username, String platform) {
        this.username = username;
        this.platform = platform;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
}
