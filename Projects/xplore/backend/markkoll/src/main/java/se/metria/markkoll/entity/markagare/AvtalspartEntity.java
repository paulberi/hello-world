package se.metria.markkoll.entity.markagare;

import lombok.*;
import se.metria.markkoll.entity.avtal.AvtalEntity;
import se.metria.markkoll.entity.logging.avtalslogg.LogAvtalsstatusEntity;
import se.metria.markkoll.openapi.model.AvtalsstatusDto;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "avtalspart")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@With
@EqualsAndHashCode
@ToString
public class AvtalspartEntity {
    @Id
    @GeneratedValue
    UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JoinColumn(name="avtal_id")
    AvtalEntity avtal;

    @OneToOne(mappedBy = "kontaktperson", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    AvtalEntity kontaktpersonAvtal;

    Boolean signatar = false;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    MarkagareEntity markagare = new MarkagareEntity();

    AvtalsstatusDto avtalsstatus;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "avtalspart")
    Set<LogAvtalsstatusEntity> logAvtalsstatus = new HashSet<>();

    @Column(name = "inkludera_i_avtal")
    boolean inkluderaIAvtal;

    LocalDate utbetalningsdatum;
}
