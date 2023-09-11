package se.metria.markkoll.service.dokument.dokumentgenerator;

import lombok.Data;
import se.metria.markkoll.util.dokument.DocProperty;

import java.util.List;

@Data
public class TestBindingsList {
    @DocProperty("TEST-namn")
    List<String> list;
}
