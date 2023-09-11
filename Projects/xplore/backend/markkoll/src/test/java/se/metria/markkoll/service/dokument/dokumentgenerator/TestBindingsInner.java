package se.metria.markkoll.service.dokument.dokumentgenerator;

import lombok.Data;
import se.metria.markkoll.util.dokument.DocProperty;

@Data
public class TestBindingsInner {
    @DocProperty("TEST-namn")
    String primitiv;

    @DocProperty("Deprecated")
    @Deprecated
    String deprecated;
}
