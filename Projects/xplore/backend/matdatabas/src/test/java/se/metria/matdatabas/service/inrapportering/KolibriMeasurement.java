package se.metria.matdatabas.service.inrapportering;

public class KolibriMeasurement {
    String time;
    Double value;

    public KolibriMeasurement(String time, Double value) {
        this.time = time;
        this.value = value;
    }

    public String getTime() {
        return time;
    }

    public Double getValue() {
        return value;
    }
}
