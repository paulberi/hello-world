package se.metria.finfo.entity.agare;

import lombok.*;
import se.metria.finfo.entity.samfallighetsforening.SamfallighetsforeningEntity;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@Entity
@ToString
@Table(name = "forvaltningsobjekt", schema= "finfo")
public class ForvaltningsobjektEntity {
    @Id
    @Column(insertable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private SamfallighetsforeningEntity samfallighetsforening;

    private String objektsinformation;
    private String anmarkning;
    private String andamalstyp;
}
