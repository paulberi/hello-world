package se.metria.xplore.admin.geoserver;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("geoserver")
public class GeoServerProperties {
    private String url; // e.g. localhost:8080/geoserver
    private String username;
    private String password;
}
