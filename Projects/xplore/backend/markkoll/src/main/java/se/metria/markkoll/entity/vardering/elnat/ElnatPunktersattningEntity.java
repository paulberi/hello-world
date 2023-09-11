package se.metria.markkoll.entity.vardering.elnat;

import lombok.*;
import se.metria.markkoll.entity.vardering.elnat.varderingsprotokoll.ElnatVarderingsprotokollEntity;
import se.metria.markkoll.openapi.model.ElnatPunktersattningTypDto;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(schema = "elnat", name = "punktersattning")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ElnatPunktersattningEntity {
    @Id
    @GeneratedValue
    UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    ElnatVarderingsprotokollEntity varderingsprotokoll;

    @Column
    String beskrivning;

    @Column
    Integer antal;

    @Column
    ElnatPunktersattningTypDto typ;

    public ElnatPunktersattningEntity(String beskrivning, int antal, ElnatPunktersattningTypDto typ) {
        this.beskrivning = beskrivning;
        this.antal = antal;
        this.typ = typ;
    }
}
