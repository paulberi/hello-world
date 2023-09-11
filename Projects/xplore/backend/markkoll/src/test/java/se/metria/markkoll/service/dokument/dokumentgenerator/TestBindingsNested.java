package se.metria.markkoll.service.dokument.dokumentgenerator;

import lombok.Data;

@Data
public class TestBindingsNested {
    TestBindingsInner intePrimitiv = new TestBindingsInner();
}
