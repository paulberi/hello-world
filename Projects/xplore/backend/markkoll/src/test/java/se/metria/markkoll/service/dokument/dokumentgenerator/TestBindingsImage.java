package se.metria.markkoll.service.dokument.dokumentgenerator;

import lombok.Data;
import org.springframework.core.io.ByteArrayResource;
import se.metria.markkoll.util.dokument.DocProperty;

@Data
public class TestBindingsImage {
    @DocProperty("TEST-namn")
    ByteArrayResource image;
}
