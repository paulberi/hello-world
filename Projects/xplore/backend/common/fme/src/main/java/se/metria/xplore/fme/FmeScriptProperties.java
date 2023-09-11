package se.metria.xplore.fme;

import java.util.Map;

public class FmeScriptProperties {
    private String fmeScript;
    private Map<String,String> fmeParams;

    public String getFmeScript() {
        return fmeScript;
    }

    public void setFmeScript(String fmeScript) {
        this.fmeScript = fmeScript;
    }

    public Map<String,String> getFmeParams() {
        return fmeParams;
    }

    public void setFmeParams(Map<String,String> users) {
        this.fmeParams = users;
    }
}
