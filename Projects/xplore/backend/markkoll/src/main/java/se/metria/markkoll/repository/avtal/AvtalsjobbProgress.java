package se.metria.markkoll.repository.avtal;

import se.metria.markkoll.openapi.model.AvtalsjobbStatusDto;

import java.util.UUID;

public interface AvtalsjobbProgress {
    UUID getId();
    AvtalsjobbStatusDto getStatus();
    Integer getTotal();
    Integer getGenerated();
}