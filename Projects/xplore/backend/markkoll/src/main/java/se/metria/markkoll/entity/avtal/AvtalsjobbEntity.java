package se.metria.markkoll.entity.avtal;

import lombok.*;
import se.metria.markkoll.entity.logging.projektlogg.AvtalhandelseEntity;
import se.metria.markkoll.openapi.model.AvtalMappstrategiDto;
import se.metria.markkoll.openapi.model.AvtalsjobbStatusDto;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.UUID;

@Entity
@Table(name = "avtalsjobb")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "avtalhandelseEntity")
@EqualsAndHashCode(exclude = "avtalhandelseEntity")
public class AvtalsjobbEntity {
    @Id
    @GeneratedValue
    UUID id;

    @NotNull
    AvtalsjobbStatusDto status;

    String path;

    String filnamn;

    @NotNull
    Integer total;

    @NotNull
    Integer generated;

    @Enumerated(value = EnumType.STRING)
    AvtalMappstrategiDto mappstrategi;

    @ManyToMany
    @JoinTable(
            name = "avtalsjobb_avtal",
            joinColumns = @JoinColumn(name = "avtalsjobb_id"),
            inverseJoinColumns = @JoinColumn(name = "avtal_id")
    )
    Collection<AvtalEntity> avtal;

    @OneToOne(mappedBy = "avtalsjobb", cascade = CascadeType.REMOVE)
    AvtalhandelseEntity avtalhandelseEntity;
}
