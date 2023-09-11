package se.metria.markkoll.entity.avtal;

import lombok.*;
import se.metria.markkoll.entity.GeometristatusEntity;
import se.metria.markkoll.entity.fastighet.FastighetEntity;
import se.metria.markkoll.entity.fastighetsforteckning.FastighetsforteckningEntity;
import se.metria.markkoll.entity.logging.avtalslogg.LogGeometristatusEntity;
import se.metria.markkoll.entity.markagare.AvtalspartEntity;
import se.metria.markkoll.entity.markagare.MarkagareEntity;
import se.metria.markkoll.entity.projekt.ProjektEntity;
import se.metria.markkoll.entity.vardering.elnat.varderingsprotokoll.ElnatVarderingsprotokollEntity;
import se.metria.markkoll.entity.vardering.fiber.varderingsprotokoll.FiberVarderingsprotokollEntity;
import se.metria.markkoll.openapi.model.AvtalsstatusDto;
import se.metria.markkoll.openapi.model.TillvaratagandeTypDto;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "avtal")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvtalEntity {
    @Id
    @GeneratedValue
    UUID id;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    FastighetEntity fastighet;

    String anteckning;

    @Builder.Default
    Integer ersattning = 0;

    @OneToMany(mappedBy = "avtal", cascade = CascadeType.REMOVE)
    Set<AvtalspartEntity> avtalsparter = new HashSet<>();

    @OneToOne(mappedBy = "avtal", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    ElnatVarderingsprotokollEntity varderingsprotokoll;

    @OneToOne(mappedBy = "avtal", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    FiberVarderingsprotokollEntity fiberVarderingsprotokoll;

    Boolean skogsfastighet;

    @Column(name = "tillvaratagande_typ")
    @Builder.Default
    TillvaratagandeTypDto tillvaratagandeTyp = TillvaratagandeTypDto.EJ_BESLUTAT;

    @Embedded
    @Builder.Default
    AvtalMetadataEntity metadata = new AvtalMetadataEntity();

    @OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinTable(
            name = "kontaktperson",
            joinColumns = @JoinColumn(name = "avtal_id"),
            inverseJoinColumns = @JoinColumn(name = "avtalspart_id")
    )
    AvtalspartEntity kontaktperson;

    @ManyToOne(fetch = FetchType.LAZY)
    ProjektEntity projekt;

    @OneToMany(mappedBy = "avtal", cascade = CascadeType.REMOVE)
    Set<GeometristatusEntity> geometristatus = new HashSet<>();

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "avtal")
    Set<LogGeometristatusEntity> logGeometristatus;

    @ManyToMany(cascade = CascadeType.REMOVE, mappedBy = "avtal")
    Set<AvtalsjobbEntity> avtalsjobb;

    @OneToOne(cascade = CascadeType.REMOVE, mappedBy = "avtal", fetch = FetchType.LAZY, optional = false)
    FastighetsforteckningEntity fastighetsforteckning;

    int utskicksnummer;

    int egetTillvaratagande;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private AvtalsstatusDto avtalsstatus = AvtalsstatusDto.EJ_BEHANDLAT;

    public void addAvtalspart(AvtalspartEntity avtalspartEntity) {
        if (containsMarkagare(avtalspartEntity.getMarkagare())) {
            return;
        }
        this.avtalsparter.add(avtalspartEntity);
        avtalspartEntity.setAvtal(this);
    }

    public boolean containsMarkagare(MarkagareEntity markagare) {
        return avtalsparter.stream().anyMatch(part -> part.getMarkagare().getId().equals(markagare.getId()));
    }
}
