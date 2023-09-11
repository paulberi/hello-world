package se.metria.markkoll.entity.vardering.bilaga.akernorm74;

import lombok.Data;
import se.metria.markkoll.entity.vardering.elnat.varderingsprotokoll.ElnatVarderingsprotokollEntity;
import se.metria.markkoll.openapi.model.AkernormomradeDto;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(schema = "elnat", name = "bilaga_74_ars_akernorm")
public class Bilaga74ArsAkernormEntity {
    @Id
    @GeneratedValue
    UUID id;

    @OneToOne
    ElnatVarderingsprotokollEntity varderingsprotokoll;

    @Column
    Integer kpi;

    @Column
    AkernormomradeDto omrade;

    @Column
    LocalDateTime vardetidpunkt;

    @Column
    Integer fastighetsnr;
}
