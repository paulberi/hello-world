package se.metria.finfo.service.agare.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class FinfoAgare {
    private String adress;

    private String andel;

    private String co;

    private String namn;

    private String personnummer;

    private String postnummer;

    private String postort;

    private String description;

    private UUID fastighetId;
}
