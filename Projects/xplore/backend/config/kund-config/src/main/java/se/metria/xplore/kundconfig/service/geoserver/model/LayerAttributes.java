package se.metria.xplore.kundconfig.service.geoserver.model;

import lombok.Data;

@Data
public class LayerAttributes {
    private String name;
    private Class<?> dataType;
}
