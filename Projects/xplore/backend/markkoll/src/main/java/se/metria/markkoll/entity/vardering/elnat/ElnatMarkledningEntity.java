package se.metria.markkoll.entity.vardering.elnat;

import lombok.*;
import se.metria.markkoll.entity.vardering.elnat.varderingsprotokoll.ElnatVarderingsprotokollEntity;
import se.metria.markkoll.repository.common.Auditable;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(schema = "elnat", name = "markledning")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ElnatMarkledningEntity {
    @Id
    @GeneratedValue
    UUID id;

    @Column
    String beskrivning;

    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    ElnatVarderingsprotokollEntity varderingsprotokoll;

    @Column
    Integer langd;

    @Column
    Integer bredd;

    public ElnatMarkledningEntity(String beskrivning, Integer langd, Integer bredd) {
        this.beskrivning = beskrivning;
        this.langd = langd;
        this.bredd = bredd;
    }
}
