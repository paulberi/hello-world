package se.metria.xplore.kundconfig.service.geoserver.model;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@JsonRootName(value = "layerGroups")
@Data
public class LayerGroups {
    private List<LayerGroup> layerGroup;
}
