package se.metria.markkoll.entity.vardering.bilaga.ovrigtintrang;

import lombok.Data;
import se.metria.markkoll.entity.vardering.elnat.varderingsprotokoll.ElnatVarderingsprotokollEntity;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(schema = "elnat", name = "bilaga_ovrigt_intrang")
public class BilagaOvrigtIntrangEntity {
    @Id
    @GeneratedValue
    UUID id;

    @OneToOne
    ElnatVarderingsprotokollEntity varderingsprotokoll;
}
