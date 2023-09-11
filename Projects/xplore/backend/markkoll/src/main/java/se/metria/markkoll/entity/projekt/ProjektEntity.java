package se.metria.markkoll.entity.projekt;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import se.metria.markkoll.entity.BeredareEntity;
import se.metria.markkoll.entity.ImportVersionEntity;
import se.metria.markkoll.entity.InfobrevsjobbEntity;
import se.metria.markkoll.entity.avtal.AvtalEntity;
import se.metria.markkoll.entity.indata.IndataEntity;
import se.metria.markkoll.entity.logging.projektlogg.ProjektLoggEntity;
import se.metria.markkoll.exception.MarkkollException;
import se.metria.markkoll.openapi.model.ProjektTypDto;
import se.metria.markkoll.openapi.model.UtskicksstrategiDto;
import se.metria.markkoll.repository.common.Auditable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(schema = "markkoll", name = "projekt")
@Inheritance(strategy = InheritanceType.JOINED)
@SequenceGenerator(name = "projekt_id_seq", sequenceName = "projekt_id_seq", allocationSize = 1)
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@With
public class ProjektEntity extends Auditable<UUID> {
    @Id
    @GeneratedValue
    UUID id;

    String namn;

    String ort;

    ProjektTypDto projekttyp;

    String beskrivning;

    @Column(name = "start_datum")
    LocalDateTime startDatum;

    @OneToMany(mappedBy = "projekt", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @Builder.Default
    Set<ImportVersionEntity> versioner = new HashSet<>();

    @OneToMany(mappedBy = "projekt", cascade = CascadeType.ALL)
    @ToString.Exclude
    @Builder.Default
    Set<AvtalEntity> avtal = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinTable(
            name = "current_version",
            joinColumns = @JoinColumn(name = "projekt_id"),
            inverseJoinColumns = @JoinColumn(name = "version_id")
    )
    @ToString.Exclude
    ImportVersionEntity currentVersion;

    @Column(updatable = false)
    String kundId;

    @OneToMany(mappedBy = "projekt", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    Set<ProjektLoggEntity> projektlogg;

    @OneToMany(mappedBy = "projektId", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    Set<InfobrevsjobbEntity> infobrevsjobb;

    @OneToOne(mappedBy = "projekt", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    BeredareEntity beredare = new BeredareEntity(this);

    @OneToMany(mappedBy = "projekt", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    List<IndataEntity> indata;

    @Builder.Default
    UtskicksstrategiDto utskicksstrategi = UtskicksstrategiDto.ADRESS;

    String uppdragsnummer;

    String utbetalningskonto;

    String projektnummer;

    String ansvarigProjektledare;

    String ansvarigKonstruktor;

    @OneToOne(mappedBy = "projekt", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private AvtalsinstallningarEntity avtalsinstallningar = new AvtalsinstallningarEntity(this);

    public void addVersion(ImportVersionEntity importVersionEntity) {
        importVersionEntity.setProjekt(this);
        this.versioner.add(importVersionEntity);
    }

    public void removeVersion(ImportVersionEntity importVersionEntity) {
        if (!this.versioner.remove(importVersionEntity)) {
            throw new MarkkollException();
        }
    }

    public void addAvtal(AvtalEntity avtalEntity) {
        this.avtal.add(avtalEntity);
        avtalEntity.setProjekt(this);
    }

    public void addAvtal(Set<AvtalEntity> avtalSet) {
        for(var avtal: avtalSet) {
            addAvtal(avtal);
        }
    }

}
