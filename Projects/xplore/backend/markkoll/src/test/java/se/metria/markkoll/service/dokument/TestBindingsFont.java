package se.metria.markkoll.service.dokument;

import lombok.Data;
import se.metria.markkoll.util.dokument.DocProperty;

import java.util.List;

@Data
public class TestBindingsFont {
    @DocProperty("font")
    List<String> font;

    @DocProperty("bigFont")
    List<String> bigFont;
}
