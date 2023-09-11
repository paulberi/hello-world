package se.metria.matdatabas.service.rapport.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import se.metria.matdatabas.service.common.Auditable;
import se.metria.matdatabas.service.rapport.dto.RapportGrafSettings;
import se.metria.matdatabas.service.rapport.dto.RapportSettings;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "rapport")
@SequenceGenerator(name = "rapport_id_seq", sequenceName = "rapport_id_seq", allocationSize = 1)
public class RapportSettingsEntity extends Auditable {

    @Id
    @GeneratedValue(generator = "rapport_id_seq")
    private Integer id;

    private String namn;

    private Boolean aktiv;

    private String mejlmeddelande;

    private String beskrivning;

    @Column(name = "inledning_rubrik")
    private String inledningRubrik;

    @Column(name = "inledning_information")
    private String inledningInformation;

    @Column(name = "tidsintervall")
    private String tidsintervall;

    private LocalDateTime startdatum;

    private LocalDateTime rorelsereferensdatum;

    private UUID lagesbild;

    @Column(name = "senast_skickad")
    private LocalDateTime senastSkickad;

    @Column(name = "dataperiod_from")
    private String dataperiodFrom;

    @OneToMany(
        mappedBy = "rapport",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private Set<RapportMottagareEntity> rapportMottagare = new HashSet<>();

    @OneToMany(
        mappedBy = "rapport",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<RapportGrafSettingsEntity> rapportGraf = new ArrayList<>();

    public RapportSettingsEntity(RapportSettings rapportSettings) {
        update(rapportSettings);
    }

    public void update(RapportSettings rapportSettings) {
        setNamn(rapportSettings.getNamn());
        setAktiv(rapportSettings.getAktiv());
        setMejlmeddelande(rapportSettings.getMejlmeddelande());
        setBeskrivning(rapportSettings.getBeskrivning());
        setInledningRubrik(rapportSettings.getInledningRubrik());
        setInledningInformation(rapportSettings.getInledningInformation());
        setTidsintervall(rapportSettings.getTidsintervall());
        setStartdatum(rapportSettings.getStartDatum());
        setRorelsereferensdatum(rapportSettings.getRorelsereferensdatum());
        if (rapportSettings.getLagesbild() == null) {
            setLagesbild(null);
        } else {
            setLagesbild(UUID.fromString(rapportSettings.getLagesbild()));
        }
        setSenastSkickad(rapportSettings.getSenastSkickad());
        setDataperiodFrom(rapportSettings.getDataperiodFrom());

        UpdateGrafer(
            rapportSettings.getRapportGraf()
                           .stream()
                           .map(RapportGrafSettingsEntity::new)
                           .collect(Collectors.toList())
        );

        UpdateMottagare(
            rapportSettings.getRapportMottagare()
                           .stream()
                           .map(RapportMottagareEntity::new)
                           .collect(Collectors.toSet())
        );
    }

    public void AddRapportGraf(RapportGrafSettingsEntity entity) {
        getRapportGraf().add(entity);
        entity.setRapport(this);
    }

    public void RemoveRapportGraf(RapportGrafSettingsEntity entity) {
        getRapportGraf().remove(entity);
        entity.setRapport(null);
    }

    public void AddMottagare(RapportMottagareEntity entity) {
        getRapportMottagare().add(entity);
        entity.setRapport(this);
    }

    public void RemoveMottagare(RapportMottagareEntity entity) {
        getRapportMottagare().remove(entity);
        entity.setRapport(null);
    }

    public void UpdateMottagare(Set<RapportMottagareEntity> mottagareNew) {
        final Set<RapportMottagareEntity> mottagareOld = getRapportMottagare();

        Set<RapportMottagareEntity> mottagareAdd = new HashSet<>(mottagareNew);
        mottagareAdd.removeAll(mottagareOld);

        Set<RapportMottagareEntity> mottagareRemove = new HashSet<>(mottagareOld);
        mottagareRemove.removeAll(mottagareNew);

        mottagareRemove.forEach(this::RemoveMottagare);
        mottagareAdd.forEach(this::AddMottagare);
    }

    public void UpdateGrafer(List<RapportGrafSettingsEntity> graferNew) {
        final List<RapportGrafSettingsEntity> graferOld = new ArrayList<>(getRapportGraf());

        List<RapportGrafSettingsEntity> graferAdd = new ArrayList<>(graferNew);
        graferAdd.removeAll(graferOld);

        List<RapportGrafSettingsEntity> graferRemove = new ArrayList<>(graferOld);
        graferRemove.removeAll(graferNew);

        graferRemove.forEach(this::RemoveRapportGraf);
        graferAdd.forEach(this::AddRapportGraf);

        List<RapportGrafSettingsEntity> graferUpdate = new ArrayList<>(graferOld);
        graferUpdate.retainAll(graferNew);
        graferUpdate.forEach(g -> g.update(new RapportGrafSettings(graferNew.get(graferNew.indexOf(g)))));
    }
}
