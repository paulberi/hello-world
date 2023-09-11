package se.metria.matdatabas.service.rapport.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import se.metria.matdatabas.service.rapport.dto.RapportGrafSettings;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "rapport_graf")
@SequenceGenerator(name = "rapport_graf_seq", sequenceName = "rapport_graf_seq", allocationSize = 1)
public class RapportGrafSettingsEntity {
    @Id
    @GeneratedValue(generator = "rapport_graf_seq")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    private RapportSettingsEntity rapport;

    private String rubrik;

    private String info;

    @OneToMany(
            mappedBy = "rapportGraf",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<RapportGrafMatningstypEntity> matningstyper = new HashSet<>();

    @OneToMany(
            mappedBy = "rapportGraf",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<RapportGrafGransvardeEntity> gransvarden = new HashSet<>();

    public RapportGrafSettingsEntity(RapportGrafSettings graf) {
        update(graf);
    }

    public void update(RapportGrafSettings graf) {
        this.id = graf.getId();
        this.rubrik = graf.getRubrik();
        this.info = graf.getInfo();

        UpdateMatningstyper(
            graf.getMatningstyper()
                .stream()
                .map(RapportGrafMatningstypEntity::new)
                .collect(Collectors.toSet())
        );

        UpdateGransvarden(
                graf.getGransvarden()
                    .stream()
                    .map(RapportGrafGransvardeEntity::new)
                    .collect(Collectors.toSet())
        );
    }

    public void AddMatningstyp(RapportGrafMatningstypEntity entity) {
        getMatningstyper().add(entity);
        entity.setRapportGraf(this);
    }

    public void AddGransvarde(RapportGrafGransvardeEntity entity) {
        getGransvarden().add(entity);
        entity.setRapportGraf(this);
    }

    public void RemoveMatningstyp(RapportGrafMatningstypEntity entity) {
        getMatningstyper().remove(entity);
        entity.setRapportGraf(null);
    }

    public void RemoveGransvarde(RapportGrafGransvardeEntity entity) {
        getGransvarden().remove(entity);
        entity.setRapportGraf(null);
    }

    public void UpdateMatningstyper(Set<RapportGrafMatningstypEntity> matningstyperNew) {
        final Set<RapportGrafMatningstypEntity> matningstyperOld = getMatningstyper();

        Set<RapportGrafMatningstypEntity> matningstyperAdd = new HashSet<>(matningstyperNew);
        matningstyperAdd.removeAll(matningstyperOld);

        Set<RapportGrafMatningstypEntity> matningstyperRemove = new HashSet<>(matningstyperOld);
        matningstyperRemove.removeAll(matningstyperNew);

        matningstyperRemove.forEach(this::RemoveMatningstyp);
        matningstyperAdd.forEach(this::AddMatningstyp);
    }

    public void UpdateGransvarden(Set<RapportGrafGransvardeEntity> gransvardenNew) {
        final Set<RapportGrafGransvardeEntity> gransvardenOld = getGransvarden();

        Set<RapportGrafGransvardeEntity> gransvardenAdd = new HashSet<>(gransvardenNew);
        gransvardenAdd.removeAll(gransvardenOld);

        Set<RapportGrafGransvardeEntity> gransvardenRemove = new HashSet<>(gransvardenOld);
        gransvardenRemove.removeAll(gransvardenNew);

        gransvardenRemove.forEach(this::RemoveGransvarde);
        gransvardenAdd.forEach(this::AddGransvarde);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RapportGrafSettingsEntity)) {
            return false;
        }
        RapportGrafSettingsEntity that = (RapportGrafSettingsEntity) o;

        return this.id == that.id;
    }

    @Override
    public int hashCode() {
        return this.id != null ? this.id.hashCode() : 0;
    }
}
