package se.metria.markkoll.service.map;

public enum LayerStyle {
    FASTIGHETER_URVAL("fastigheter_urval"),
    FASTIGHETSYTOR_TONAD("fastighetsytor_tonad"),
    INTRANG_PROJEKT("intrang_projekt"),
    INTRANG_PROJEKT_AVTALSKARTA("intrang_projekt_avtalskarta"),
    INTRANG_PROJEKT_PROJEKTOMRADE("intrang_projekt_projektomrade");

    private String value;

    LayerStyle(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
