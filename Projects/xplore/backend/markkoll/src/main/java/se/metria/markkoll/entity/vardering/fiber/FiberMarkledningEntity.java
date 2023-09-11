package se.metria.markkoll.entity.vardering.fiber;

import lombok.*;
import se.metria.markkoll.entity.vardering.fiber.varderingsprotokoll.FiberVarderingsprotokollEntity;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(schema = "fiber", name = "markledning")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FiberMarkledningEntity {
    @Id
    @GeneratedValue
    UUID id;

    @Column
    String beskrivning;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    FiberVarderingsprotokollEntity varderingsprotokoll;

    @Column
    Integer langd;

    @Column
    Integer bredd;

    public FiberMarkledningEntity(String beskrivning, Integer langd, Integer bredd) {
        this.beskrivning = beskrivning;
        this.langd = langd;
        this.bredd = bredd;
    }
}
