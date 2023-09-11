package se.metria.markkoll.entity.fastighet;


import lombok.*;
import se.metria.markkoll.entity.markagare.MarkagareEntity;
import se.metria.markkoll.entity.samfallighet.SamfallighetEntity;
import se.metria.markkoll.openapi.model.DetaljtypDto;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "fastighet")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@EqualsAndHashCode(exclude = {"markagare"})
@ToString(exclude = {"markagare"})
@With
public class FastighetEntity {
    @Id
    @Column(insertable = false, updatable = false)
    UUID id;

    String fastighetsbeteckning;

    String trakt;

    String blockenhet;

    @Column(name = "extern_id")
    String externid;

    String kommunnamn;

    String lan;

    @Column
    DetaljtypDto detaljtyp;

    @OneToMany(
            mappedBy = "fastighet",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    Set<MarkagareEntity> markagare = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "fastighetId", cascade = CascadeType.REMOVE)
    Set<FastighetOmradeEntity> fastighetOmraden = new HashSet<>();

    @OneToOne(mappedBy = "fastighet", fetch = FetchType.LAZY)
    private SamfallighetEntity samfallighet;

    public void addFastighetomrade(FastighetOmradeEntity fastighetOmradeEntity) {
        fastighetOmraden.add(fastighetOmradeEntity);
    }
}
