package se.metria.matdatabas.service.inrapportering.model.smhi;

import com.fasterxml.jackson.annotation.JsonValue;

public enum SmhiParameterCode {
    DYGNSNEDERBÖRD("5"),
    MOMENTAN_TEMPERATUR("1");

    private String value;

    SmhiParameterCode(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
