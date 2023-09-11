package se.metria.markkoll.service.geofence.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

@Data
@JsonRootName(value = "Rule")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Rule {
    private Integer id;
    private Integer priority;
    private String userName;
    private String roleName;
    private String workspace;
    private String layer;
    private Access access;
    private LayerDetails layerDetails;
}
