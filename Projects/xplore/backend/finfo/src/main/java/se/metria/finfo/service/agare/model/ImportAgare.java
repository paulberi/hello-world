package se.metria.finfo.service.agare.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImportAgare {
    private String adress;

    private String andel;

    private String co;

    private String namn;

    private String personnummer;

    private String postnummer;

    private String postort;

    private String description;

    private String typ;
}
