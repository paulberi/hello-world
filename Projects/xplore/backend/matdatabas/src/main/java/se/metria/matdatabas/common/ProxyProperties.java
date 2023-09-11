package se.metria.matdatabas.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("proxy")
@Data
public class ProxyProperties {
    private String host;
    private int port;
}
