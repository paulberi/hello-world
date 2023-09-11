package se.metria.markkoll.repository.infobrev;

import se.metria.markkoll.openapi.model.AvtalsjobbStatusDto;

import java.util.UUID;

public interface InfobrevsjobbProgress {
    UUID getId();
    AvtalsjobbStatusDto getStatus();
    Integer getTotal();
    Integer getGenerated();
}
