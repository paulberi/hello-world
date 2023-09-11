package se.metria.xplore.kundconfig.service.geoserver.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

@Data
@JsonRootName(value = "featureType")
@JsonIgnoreProperties(ignoreUnknown = true)
public class FeatureTypeDetails {
    String name;
    String nativeName;
    String title;
    FeatureTypeAttribute[] attributes;
}
