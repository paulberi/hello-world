package se.metria.markkoll.service.dokument.dokumentgenerator;

import lombok.Data;
import se.metria.markkoll.util.dokument.DocProperty;

@Data
public class TestBindingsBoolean {
    @DocProperty("TEST-cb1")
    Boolean booleanTrue;

    @DocProperty("TEST-cb2")
    Boolean booleanFalse;
}
