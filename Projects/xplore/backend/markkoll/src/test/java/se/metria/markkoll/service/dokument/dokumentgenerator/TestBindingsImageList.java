package se.metria.markkoll.service.dokument.dokumentgenerator;

import lombok.Data;
import org.springframework.core.io.ByteArrayResource;
import se.metria.markkoll.util.dokument.DocProperty;

import java.util.Set;

@Data
public class TestBindingsImageList {
    @DocProperty("TEST-namn")
    Set<ByteArrayResource> images;
}
