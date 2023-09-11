package se.metria.markkoll.service.dokument.dokumentgenerator;

import lombok.Data;
import se.metria.markkoll.util.dokument.DocProperty;

@Data
public class TestBindingsMissingDocProperty {
    @DocProperty("Ett fält som inte finns")
    String foo;
}
