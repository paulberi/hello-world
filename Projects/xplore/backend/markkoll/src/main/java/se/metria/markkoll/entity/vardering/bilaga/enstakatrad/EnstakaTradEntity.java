package se.metria.markkoll.entity.vardering.bilaga.enstakatrad;

import lombok.Data;
import se.metria.markkoll.openapi.model.TradslagDto;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(schema = "elnat", name = "enstaka_trad")
public class EnstakaTradEntity {
    @Id
    @GeneratedValue
    UUID enstakaTradId;

    @Column
    String beskrivning;

    @ManyToOne
    BilagaEnstakaTradEntity bilagaEnstakaTrad;

    @Column
    Integer diameter;

    @Column
    Integer antal;

    @Column
    TradslagDto tradslag;
}
