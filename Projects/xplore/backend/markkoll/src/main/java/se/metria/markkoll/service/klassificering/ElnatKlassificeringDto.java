package se.metria.markkoll.service.klassificering;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Klassificering av områdesintrång i ett elnätsprojekt.
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ElnatKlassificeringDto {
    @Builder.Default
    Double langdMarkledning = 0.;

    Double langdLuftledning = 0.;

    Double langdMarkstrak = 0.;

    Integer antalNatstationer = 0;

    Integer antalKabelskap = 0;

    @Builder.Default
    String littera = "";

    Double hogstaSpanningsniva = 0.;
}
