package se.metria.matdatabas.service.rapport.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.metria.matdatabas.service.rapport.entity.RapportSettingsEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RapportSettings {
    Integer id;
    String namn;
    Boolean aktiv;
    String mejlmeddelande;
    String beskrivning;
    String dataperiodFrom;
    LocalDateTime rorelsereferensdatum;
    String tidsintervall;
    LocalDateTime startDatum;
    String inledningRubrik;
    String inledningInformation;
    String lagesbild;
    LocalDateTime senastSkickad;
    LocalDateTime skapadDatum;
    LocalDateTime andradDatum;
    Integer andradAvId;
    List<RapportGrafSettings> rapportGraf;
    List<RapportMottagare> rapportMottagare;

    public RapportSettings(RapportSettingsEntity entity) {
        setId(entity.getId());
        setNamn(entity.getNamn());
        setAktiv(entity.getAktiv());
        setMejlmeddelande(entity.getMejlmeddelande());
        setBeskrivning(entity.getBeskrivning());
        setRorelsereferensdatum(entity.getRorelsereferensdatum());
        setTidsintervall(entity.getTidsintervall());
        setStartDatum(entity.getStartdatum());
        setDataperiodFrom(entity.getDataperiodFrom());
        setInledningRubrik(entity.getInledningRubrik());
        setInledningInformation(entity.getInledningInformation());
        if (entity.getLagesbild() == null) {
            setLagesbild(null);
        } else {
            setLagesbild(entity.getLagesbild().toString());
        }
        setSenastSkickad(entity.getSenastSkickad());
        setSkapadDatum(entity.getSkapadDatum());
        setAndradDatum(entity.getAndradDatum());
        setAndradAvId(entity.getAndradAvId());

        setRapportGraf(
            entity.getRapportGraf()
                  .stream()
                  .map(RapportGrafSettings::new)
                  .collect(Collectors.toList())
        );

        setRapportMottagare(
            entity.getRapportMottagare()
                  .stream()
                  .map(RapportMottagare::new)
                  .collect(Collectors.toList())
        );
    }
}
