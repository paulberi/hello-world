package se.metria.matdatabas.service.inrapportering.model.smhi;

import com.fasterxml.jackson.annotation.JsonValue;

public enum SmhiPeriodCode {
    LATEST_DAY("latest-day"),
    LATEST_MONTHS("latest-months"),
    LATEST_HOUR("latest-hour");

    private String value;

    SmhiPeriodCode(String value) {
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
