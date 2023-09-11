package se.metria.matdatabas.service.inrapportering;

import java.util.List;

public class KolibriMeasurementResponse {
    Integer measurementDefinitionId;
    Integer deviceId;
    List<KolibriMeasurement> values;
    Integer unitId;

    public KolibriMeasurementResponse(Integer measurementDefinitionId, Integer deviceId, List<KolibriMeasurement> values, Integer unitId) {
        this.measurementDefinitionId = measurementDefinitionId;
        this.deviceId = deviceId;
        this.values = values;
        this.unitId = unitId;
    }

    public Integer getMeasurementDefinitionId() {
        return measurementDefinitionId;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public List<KolibriMeasurement> getValues() {
        return values;
    }

    public Integer getUnitId() {
        return unitId;
    }
}
