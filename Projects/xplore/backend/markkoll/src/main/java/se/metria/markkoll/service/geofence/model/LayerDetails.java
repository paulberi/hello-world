package se.metria.markkoll.service.geofence.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LayerDetails {
    private LayerType layerType;
    private String cqlFilterRead;
    private CatalogMode catalogMode;
    private List<LayerAttributes> attributes;
}
