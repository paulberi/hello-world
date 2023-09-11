package se.metria.finfo.entity.agare;

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
    private UUID id;

    private String adress;

    private String andel;

    private String co;

    private String namn;

    private String personnummer;

    private String postnummer;

    private String postort;

    private String description;

    @Column(name = "fastighet_id")
    private UUID fastighetId;

    private String typ;

    @Column(name = "importerad_datum")
    private LocalDateTime importeradDatum;
}
