package se.metria.markkoll.entity.vardering.elnat.varderingsprotokoll;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@Data
@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ElnatVarderingsprotokollMetadataEntity {
    @Column
    String ledning = "";

    @Column
    String koncessionslopnr = "";

    @Column
    String varderingsmanOchForetag = "";

    @Column
    LocalDateTime varderingstidpunkt = LocalDateTime.now();

    String fastighetsnummer;

    String spanningsniva;
}
