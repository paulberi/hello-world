package se.metria.markkoll.entity.vardering.elnat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import se.metria.markkoll.entity.vardering.elnat.varderingsprotokoll.ElnatVarderingsprotokollEntity;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(schema = "elnat", name = "ovrigt_intrang")
@NoArgsConstructor
public class ElnatOvrigtIntrangEntity {
    @Id
    @GeneratedValue
    UUID id;

    String beskrivning;

    int ersattning;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    ElnatVarderingsprotokollEntity varderingsprotokoll;

    public ElnatOvrigtIntrangEntity(String beskrivning, int ersattning) {
        this.beskrivning = beskrivning;
        this.ersattning = ersattning;
    }
}
