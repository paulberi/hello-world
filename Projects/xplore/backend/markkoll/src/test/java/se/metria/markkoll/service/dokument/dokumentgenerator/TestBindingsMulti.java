package se.metria.markkoll.service.dokument.dokumentgenerator;

import lombok.Data;
import org.springframework.core.io.ByteArrayResource;
import se.metria.markkoll.util.dokument.DocProperty;

import java.util.List;

@Data
public class TestBindingsMulti {
    @DocProperty("Text")
    List<String> text;

    @DocProperty("Map")
    List<ByteArrayResource> map;
}
