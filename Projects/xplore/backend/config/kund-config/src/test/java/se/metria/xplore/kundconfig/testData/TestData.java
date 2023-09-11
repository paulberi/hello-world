package se.metria.xplore.kundconfig.testData;

import se.metria.xplore.kundconfig.entity.KundEntity;

public class TestData {
    public static KundEntity mockKundEntity() {
        return KundEntity.builder()
                        .id("111")
                        .namn("Joels El AB")
                        .epost("joel@el.se")
                        .telefon("070")
                        .kontaktperson("Joel")
                        .build();
    }
}
