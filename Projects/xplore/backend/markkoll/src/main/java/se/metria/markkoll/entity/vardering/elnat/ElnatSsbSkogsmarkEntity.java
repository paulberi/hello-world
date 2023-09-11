package se.metria.markkoll.entity.vardering.elnat;

import lombok.*;
import se.metria.markkoll.entity.vardering.elnat.varderingsprotokoll.ElnatVarderingsprotokollEntity;
import se.metria.markkoll.repository.common.Auditable;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(schema = "elnat", name = "ssb_skogsmark")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ElnatSsbSkogsmarkEntity {
    @Id
    @GeneratedValue
    UUID id;

    @Column
    String beskrivning;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    ElnatVarderingsprotokollEntity varderingsprotokoll;

    @Column
    Integer langd;

    @Column
    Integer bredd;

    public ElnatSsbSkogsmarkEntity(String beskrivning, Integer langd, Integer bredd) {
        this.beskrivning = beskrivning;
        this.langd = langd;
        this.bredd = bredd;
    }
}
