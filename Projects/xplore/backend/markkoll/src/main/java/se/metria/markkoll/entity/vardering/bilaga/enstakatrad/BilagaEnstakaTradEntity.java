package se.metria.markkoll.entity.vardering.bilaga.enstakatrad;

import lombok.Data;
import se.metria.markkoll.entity.vardering.elnat.varderingsprotokoll.ElnatVarderingsprotokollEntity;
import se.metria.markkoll.openapi.model.TillvaxtomradeDto;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(schema = "elnat", name = "bilaga_enstaka_trad")
public class BilagaEnstakaTradEntity {
    @Id
    @GeneratedValue
    UUID id;

    @OneToOne
    ElnatVarderingsprotokollEntity varderingsprotokoll;

    @Column
    String nisIb;

    @Column
    TillvaxtomradeDto tillvaxtomrade;

    @Column
    LocalDateTime vardetidpunkt;

    @Column
    Integer fastighetsnr;
}
