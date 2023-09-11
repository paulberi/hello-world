package se.metria.finfo.service.agare.model;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ImportFastighet {
    private UUID uuid;

    private List<ImportAgare> agare;

    private String beteckning;

    private String fastighetsnyckel;

}
