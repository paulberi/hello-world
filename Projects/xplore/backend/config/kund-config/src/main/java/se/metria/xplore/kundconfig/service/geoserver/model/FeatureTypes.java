package se.metria.xplore.kundconfig.service.geoserver.model;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@JsonRootName(value = "featureTypes")
@Data
public class FeatureTypes {
    private List<FeatureType> featureType;
}