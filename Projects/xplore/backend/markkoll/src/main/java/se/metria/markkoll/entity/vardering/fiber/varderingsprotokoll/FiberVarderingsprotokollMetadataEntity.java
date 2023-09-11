package se.metria.markkoll.entity.vardering.fiber.varderingsprotokoll;

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
public class FiberVarderingsprotokollMetadataEntity {
    @Column
    LocalDateTime varderingstidpunkt;

    @Column
    String varderingsmanOchForetag = "";
}
