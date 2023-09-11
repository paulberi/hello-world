package se.metria.xplore.nmd;

import org.springframework.boot.context.properties.ConfigurationProperties;
import se.metria.xplore.fme.FmeScriptProperties;

@ConfigurationProperties("nmd")
public class NmdProperties  extends FmeScriptProperties {
}
