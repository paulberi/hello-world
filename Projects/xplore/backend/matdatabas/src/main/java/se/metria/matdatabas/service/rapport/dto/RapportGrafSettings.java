package se.metria.matdatabas.service.rapport.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.metria.matdatabas.service.rapport.entity.RapportGrafSettingsEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RapportGrafSettings {
    Integer id;
    String rubrik;
    String info;
    List<Integer> matningstyper = new ArrayList<>();
    List<Integer> gransvarden = new ArrayList<>();

    public RapportGrafSettings(RapportGrafSettingsEntity entity) {
        setId(entity.getId());
        setRubrik(entity.getRubrik());
        setInfo(entity.getInfo());
        setMatningstyper(
                entity.getMatningstyper()
                      .stream()
                      .map(mtyp -> mtyp.getMatningstypId())
                      .collect(Collectors.toList())
        );
        setGransvarden(
                entity.getGransvarden()
                      .stream()
                      .map(gvarde -> gvarde.getGransvardeId())
                      .collect(Collectors.toList())
        );
    }
}
