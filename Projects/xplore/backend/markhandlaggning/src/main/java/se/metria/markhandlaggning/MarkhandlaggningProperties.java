package se.metria.markhandlaggning;

import org.springframework.boot.context.properties.ConfigurationProperties;
import se.metria.xplore.fme.FmeScriptProperties;

@ConfigurationProperties("markhandlaggning")
public class MarkhandlaggningProperties extends FmeScriptProperties{
}
