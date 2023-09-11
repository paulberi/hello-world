package se.metria.markkoll.entity.vardering.elnat;

import lombok.*;
import se.metria.markkoll.entity.vardering.elnat.varderingsprotokoll.ElnatVarderingsprotokollEntity;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(schema = "elnat", name = "ledning_skogsmark")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ElnatLedningSkogsmarkEntity {
    @Id
    @GeneratedValue
    UUID id;

    String beskrivning;

    Integer ersattning;

    @ManyToOne(fetch =  FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    ElnatVarderingsprotokollEntity varderingsprotokoll;

    public ElnatLedningSkogsmarkEntity(String beskrivning, int ersattning) {
        this.beskrivning = beskrivning;
        this.ersattning = ersattning;
    }
}
