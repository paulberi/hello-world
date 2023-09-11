package se.metria.markkoll.service.dokument.dokumentgenerator;

import se.metria.markkoll.util.dokument.DocProperty;

import java.math.BigDecimal;

public class TestBindingsInvalidDatatype {
    @DocProperty("TEST-namn")
    BigDecimal error;
}
