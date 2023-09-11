package se.metria.markkoll.entity.vardering.elnat.varderingsprotokoll;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(schema = "elnat", name = "hinder_akermark")
@NoArgsConstructor
public class ElnatHinderAkermarkEntity {
    @Id
    @GeneratedValue
    private UUID id;

    private String beskrivning;

    private int ersattning;

    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private ElnatVarderingsprotokollEntity varderingsprotokoll;

    public ElnatHinderAkermarkEntity(String beskrivning, int ersattning) {
        this.beskrivning = beskrivning;
        this.ersattning = ersattning;
    }
}
