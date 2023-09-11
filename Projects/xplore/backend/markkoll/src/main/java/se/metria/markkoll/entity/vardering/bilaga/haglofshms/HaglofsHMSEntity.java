package se.metria.markkoll.entity.vardering.bilaga.haglofshms;

import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(schema = "elnat", name = "haglofs_hms")
public class HaglofsHMSEntity {
    @Id
    @GeneratedValue
    UUID id;

    @ManyToOne
    BilagaHaglofsHMSEntity bilagaHaglofsHMS;

    @Column
    Integer ersattning;
}
