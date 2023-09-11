package se.metria.markkoll.entity;

import lombok.*;
import se.metria.markkoll.entity.fastighet.FastighetEntity;
import se.metria.markkoll.entity.logging.projektlogg.InfobrevhandelseEntity;
import se.metria.markkoll.openapi.model.AvtalsjobbStatusDto;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.UUID;

@Entity
@Table(name = "infobrevsjobb")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InfobrevsjobbEntity {
    @Id
    @GeneratedValue
    UUID id;

    @NotNull
    AvtalsjobbStatusDto status;

    String path;

    @NotNull
    Integer total;

    @NotNull
    Integer generated;

    @Column(name = "projekt_id")
    UUID projektId;

    String filnamn;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "infobrevsjobb_fastighet",
            joinColumns = @JoinColumn(name = "infobrevsjobb_id"),
            inverseJoinColumns = @JoinColumn(name = "fastighet_id")
    )
    Collection<FastighetEntity> fastigheter;

    @EqualsAndHashCode.Exclude
    @OneToOne(mappedBy = "infobrevsjobb", cascade = CascadeType.REMOVE)
    InfobrevhandelseEntity infobrevhandelseEntity;
}
