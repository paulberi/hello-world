package se.metria.markkoll.entity.vardering.fiber;

import lombok.Data;
import se.metria.markkoll.entity.admin.KundEntity;
import se.metria.markkoll.entity.markagare.PersonEntity;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(schema = "fiber", name = "vardering_kund_agare")
public class VarderingKundAgareEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    private FiberVarderingConfigEntity varderingConfig;

    @ManyToOne(fetch = FetchType.LAZY)
    private KundEntity kund;

    @ManyToOne(fetch = FetchType.LAZY)
    private PersonEntity person;

    @Column
    private String namn;
}
