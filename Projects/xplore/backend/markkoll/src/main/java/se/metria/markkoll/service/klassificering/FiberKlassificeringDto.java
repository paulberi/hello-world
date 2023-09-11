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
public class FiberKlassificeringDto {
    @Builder.Default
    Double langdMarkledning = 0.;

    @Builder.Default
    Double langdLuftledning = 0.;

    @Builder.Default
    Integer antalMarkskap = 0;

    @Builder.Default
    Integer antalOptobrunn = 0;

    @Builder.Default
    Integer antalTeknikbod = 0;

    @Builder.Default
    String littera = "";
}
