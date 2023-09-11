package se.metria.xplore.kundconfig.service.geoserver.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FeatureTypeAttribute {
    String name;
    String nativeName;
}
