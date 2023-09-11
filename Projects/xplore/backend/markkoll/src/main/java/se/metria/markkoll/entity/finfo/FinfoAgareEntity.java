package se.metria.markkoll.entity.finfo;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "agare", schema= "finfo")
public class FinfoAgareEntity {
    @Id
    @Column(insertable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;

    String adress;

    String andel;

    String co;

    String namn;

    String personnummer;

    String postnummer;

    String postort;

    String description;

    @Column(name = "fastighet_id")
    UUID fastighetId;

    String typ;

    @Column(name = "importerad_datum")
    LocalDateTime importeradDatum;
}
