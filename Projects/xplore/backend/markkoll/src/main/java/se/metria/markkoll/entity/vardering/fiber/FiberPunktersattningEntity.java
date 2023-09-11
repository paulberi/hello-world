package se.metria.markkoll.entity.vardering.fiber;

import lombok.*;
import se.metria.markkoll.entity.vardering.fiber.varderingsprotokoll.FiberVarderingsprotokollEntity;
import se.metria.markkoll.openapi.model.FiberPunktersattningTypDto;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(schema = "fiber", name = "punktersattning")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FiberPunktersattningEntity {
    @Id
    @GeneratedValue
    UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    FiberVarderingsprotokollEntity varderingsprotokoll;

    @Column
    String beskrivning;

    @Column
    Integer antal;

    @Column
    FiberPunktersattningTypDto typ;

    public FiberPunktersattningEntity(String beskrivning, Integer antal, FiberPunktersattningTypDto typ) {
        this.beskrivning = beskrivning;
        this.antal = antal;
        this.typ = typ;
    }
}
