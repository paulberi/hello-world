package se.metria.markkoll.entity.vardering.bilaga.akernorm74;

import lombok.Data;
import se.metria.markkoll.openapi.model.HinderMarkslagDto;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(schema = "elnat", name = "hinder")
public class HinderEntity {
    @Id
    @GeneratedValue
    UUID id;

    @Column
    String beskrivning;

    @ManyToOne
    Bilaga74ArsAkernormEntity bilagaAkernorm74;

    @Column
    Integer langdL1;

    @Column
    Integer langdL2;

    @Column
    Integer langdL3;

    @Column
    HinderMarkslagDto markslag;
}
