package se.metria.markkoll.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import se.metria.markkoll.entity.projekt.ProjektEntity;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "beredare")
@EqualsAndHashCode
@NoArgsConstructor
public class BeredareEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private ProjektEntity projekt;

    private String namn = "";

    private String telefonnummer;

    private String adress;

    private String postnummer;

    private String ort;

    public BeredareEntity(ProjektEntity projekt) {
        this.projekt = projekt;
    }
}
