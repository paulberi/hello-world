package se.metria.markkoll.entity.avtal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Data
@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvtalMetadataEntity {
    @Column
    @Builder.Default
    String stationsnamn = "";

    @Column
    @Builder.Default
    String matandeStation = "";

    @Column
    @Builder.Default
    String franStation = "";

    @Column
    @Builder.Default
    String tillStation = "";

    @Column
    @Builder.Default
    String markslag = "";
}
