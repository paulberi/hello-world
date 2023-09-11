package se.metria.finfo.entity.registerenhet;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import se.metria.finfo.openapi.model.AtgardstypDto;
import se.metria.finfo.util.BaseEntity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
@Entity
@Table(name = "berord_av_atgard", schema= "finfo")
public class BerordAvAtgardEntity extends BaseEntity<UUID> {

    private LocalDate registreringsdatum;
    private String aktbeteckning;

    @Enumerated(EnumType.STRING)
    private AtgardstypDto atgardstyp;

    @ElementCollection
    @CollectionTable(name = "atgard", joinColumns = @JoinColumn(name = "berord_av_atgard_id"))
    @Column(name = "typ")
    private List<String> atgarder = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private RegisterenhetEntity registerenhet;
}
