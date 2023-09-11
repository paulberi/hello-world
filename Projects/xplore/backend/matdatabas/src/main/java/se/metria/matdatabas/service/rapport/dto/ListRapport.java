package se.metria.matdatabas.service.rapport.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.metria.matdatabas.service.matrunda.dto.ListMatrunda;
import se.metria.matdatabas.service.matrunda.entity.MatrundaEntity;
import se.metria.matdatabas.service.rapport.entity.RapportSettingsEntity;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListRapport {
    private Integer id;
    private String namn;
    private Boolean aktiv;
    private String beskrivning;
    private LocalDateTime senastSkickad;

    public static ListRapport fromEntity(RapportSettingsEntity rapport) {
        return ListRapport.builder()
                .id(rapport.getId())
                .namn(rapport.getNamn())
                .aktiv(rapport.getAktiv())
                .beskrivning(rapport.getBeskrivning())
                .senastSkickad(rapport.getSenastSkickad())
                .build();
    }

}
