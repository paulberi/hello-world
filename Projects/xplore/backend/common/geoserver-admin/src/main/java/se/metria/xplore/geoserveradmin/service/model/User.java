package se.metria.xplore.geoserveradmin.service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    String userName;
    boolean enabled;
}
