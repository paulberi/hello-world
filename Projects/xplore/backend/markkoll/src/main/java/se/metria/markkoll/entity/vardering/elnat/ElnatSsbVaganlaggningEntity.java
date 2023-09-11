package se.metria.markkoll.entity.vardering.elnat;

import lombok.*;
import se.metria.markkoll.entity.vardering.elnat.varderingsprotokoll.ElnatVarderingsprotokollEntity;
import se.metria.markkoll.openapi.model.ElnatZonDto;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(schema = "elnat", name = "ssb_vaganlaggning")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ElnatSsbVaganlaggningEntity {
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
    ElnatZonDto zon;

    public ElnatSsbVaganlaggningEntity(String beskrivning, int langd, ElnatZonDto zon) {
        this.beskrivning = beskrivning;
        this.langd = langd;
        this.zon = zon;
    }
}
