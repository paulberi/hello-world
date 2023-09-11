package se.metria.markkoll.testdata;

import se.metria.markkoll.entity.avtal.AvtalsjobbEntity;
import se.metria.markkoll.entity.InfobrevsjobbEntity;
import se.metria.markkoll.entity.logging.projektlogg.AvtalhandelseEntity;
import se.metria.markkoll.entity.logging.projektlogg.InfobrevhandelseEntity;
import se.metria.markkoll.entity.logging.projektlogg.ProjektLoggEntity;
import se.metria.markkoll.entity.logging.projektlogg.ProjekthandelseEntity;
import se.metria.markkoll.openapi.model.*;

import java.time.LocalDateTime;
import java.util.UUID;

public class LoggTestData {
    public static ProjektLoggItemDto infobrevhandelseDto(UUID infobrevsjobbId) {
        return new InfobrevhandelseDto()
            .infobrevsjobbId(infobrevsjobbId)
            .antalFastigheter(2)
            .skapadAv("Big C")
            .skapadDatum(LocalDateTime.of(1234, 5, 6, 7, 8))
            .projektLoggType(ProjektLoggTypeDto.INFOBREVHANDELSE);
    }

    public static ProjektLoggEntity infobrevhandelseEntity(UUID infobrevsjobbId) {
        return InfobrevhandelseEntity.builder()
            .infobrevsjobb(InfobrevsjobbEntity.builder().id(infobrevsjobbId).build())
            .antalFastigheter(2)
            .skapadAv("Big C")
            .skapadDatum(LocalDateTime.of(1234, 5, 6, 7, 8))
            .projektLoggType(ProjektLoggTypeDto.INFOBREVHANDELSE)
            .build();
    }

    public static ProjektLoggItemDto avtalhandelseDto(UUID avtalsjobbId) {
        return new AvtalhandelseDto()
            .antalFastigheter(2)
            .avtalsjobbId(avtalsjobbId)
            .skapadAv("Big C")
            .skapadDatum(LocalDateTime.of(1234, 5, 6, 7, 8))
            .projektLoggType(ProjektLoggTypeDto.AVTALHANDELSE);
    }

    public static ProjektLoggEntity avtalhandelseEntity(UUID avtalsjobbId) {
        return AvtalhandelseEntity.builder()
            .antalFastigheter(2)
            .avtalsjobb(AvtalsjobbEntity.builder().id(avtalsjobbId).build())
            .skapadAv("Big C")
            .skapadDatum(LocalDateTime.of(1234, 5, 6, 7, 8))
            .projektLoggType(ProjektLoggTypeDto.AVTALHANDELSE)
            .build();
    }

    public static ProjektLoggItemDto projekthandelseDto() {
        return new ProjekthandelseDto()
            .projekthandelseTyp(ProjekthandelseTypDto.HAMTA_MARKAGARE)
            .skapadAv("Big C")
            .skapadDatum(LocalDateTime.of(1234, 5, 6, 7, 8))
            .projektLoggType(ProjektLoggTypeDto.PROJEKTHANDELSE);
    }

    public static ProjektLoggEntity projekthandelseEntity() {
        return ProjekthandelseEntity.builder()
            .projekthandelsetyp(ProjekthandelseTypDto.HAMTA_MARKAGARE)
            .skapadAv("Big C")
            .skapadDatum(LocalDateTime.of(1234, 5, 6, 7, 8))
            .projektLoggType(ProjektLoggTypeDto.PROJEKTHANDELSE)
            .build();
    }
}
