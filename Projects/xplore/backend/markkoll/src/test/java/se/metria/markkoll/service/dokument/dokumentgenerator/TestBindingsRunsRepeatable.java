package se.metria.markkoll.service.dokument.dokumentgenerator;

import lombok.Data;
import se.metria.markkoll.util.dokument.DocProperty;

import java.util.List;

@Data
public class TestBindingsRunsRepeatable {
    @DocProperty("MMK-Kartor-multi")
    List<String> kartor;
}
