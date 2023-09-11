package se.metria.markkoll.entity.vardering.fiber;


import lombok.*;
import se.metria.markkoll.entity.vardering.fiber.varderingsprotokoll.FiberVarderingsprotokollEntity;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(schema = "fiber", name = "aker_och_skogsmark")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FiberIntrangAkerOchSkogsmarkEntity {
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
    Integer ersattning;

    public FiberIntrangAkerOchSkogsmarkEntity(String beskrivning, Integer ersattning) {
        this.beskrivning = beskrivning;
        this.ersattning = ersattning;
    }
}
