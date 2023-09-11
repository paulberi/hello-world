package se.metria.markkoll.entity.markagare;

import lombok.*;
import se.metria.markkoll.entity.fastighet.FastighetEntity;
import se.metria.markkoll.openapi.model.AgartypDto;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "markagare")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "avtalspart")
public class MarkagareEntity {
    @Id
    @Column(insertable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="fastighet_id")
    FastighetEntity fastighet;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="person_id")
    PersonEntity person = new PersonEntity();

    @OneToOne(mappedBy = "markagare", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private StyrelsemedlemEntity styrelsemedlem;

    String andel;

    AgartypDto agartyp;

    String kundId;

    boolean agareSaknas;

    @OneToMany(mappedBy = "markagare")
    @ToString.Exclude
    Set<AvtalspartEntity> avtalspart;
}