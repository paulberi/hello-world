package se.metria.markkoll.entity.vardering.bilaga.haglofshms;

import lombok.Data;
import se.metria.markkoll.entity.vardering.elnat.varderingsprotokoll.ElnatVarderingsprotokollEntity;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(schema = "elnat", name = "bilaga_haglofs_hms")
public class BilagaHaglofsHMSEntity {
    @Id
    @GeneratedValue
    UUID id;

    @OneToOne
    ElnatVarderingsprotokollEntity varderingsprotokoll;
}
