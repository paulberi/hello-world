package se.metria.markkoll.service.dokument.dokumentgenerator;

import lombok.Data;
import se.metria.markkoll.util.dokument.DocProperty;

import java.time.LocalDate;

@Data
public class TestBindingsDate {
    @DocProperty("TEST-namn")
    LocalDate date;
}
