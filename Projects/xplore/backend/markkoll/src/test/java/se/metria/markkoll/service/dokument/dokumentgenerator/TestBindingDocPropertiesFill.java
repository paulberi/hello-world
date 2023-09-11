package se.metria.markkoll.service.dokument.dokumentgenerator;

import lombok.Data;
import lombok.NoArgsConstructor;
import se.metria.markkoll.util.dokument.DocProperty;

@Data
@NoArgsConstructor
public class TestBindingDocPropertiesFill {
    @DocProperty("tripp")
    String tripp;

    @DocProperty("trapp")
    String trapp;

    @DocProperty("trull")
    String trull;

}
