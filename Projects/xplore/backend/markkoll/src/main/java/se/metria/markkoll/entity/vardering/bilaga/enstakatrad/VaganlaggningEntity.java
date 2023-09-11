package se.metria.markkoll.entity.vardering.bilaga.enstakatrad;

import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(schema = "elnat", name = "vaganlaggning")
public class VaganlaggningEntity {
    @Id
    @GeneratedValue
    UUID id;

    @ManyToOne
    BilagaEnstakaTradEntity bilagaEnstakaTrad;

    @Column
    String beskrivning;

    @Column
    Integer langd;

    @Column
    Integer area;
}
