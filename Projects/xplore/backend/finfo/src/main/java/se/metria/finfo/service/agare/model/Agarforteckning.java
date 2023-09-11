package se.metria.finfo.service.agare.model;

import lombok.Data;

import java.util.Set;

@Data
public class Agarforteckning {
    private Set<ImportFastighet> fastighet;

    private Set<String> fel;

    private AgarforteckningJob agarforteckningJob;
}
