package se.metria.markkoll.entity.vardering.bilaga.enstakatrad;

import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(schema = "elnat", name = "ovrigt_intrang_skogsmark")
public class OvrigtIntrangSkogsmarkEntity {
    @Id
    @GeneratedValue
    UUID id;

    @Column
    String beskrivning;

    @ManyToOne
    BilagaEnstakaTradEntity bilagaEnstakaTrad;

    @Column
    Integer langd;

    @Column
    Integer bredd;

    @Column
    Integer area;
}
