package se.metria.matdatabas.service.rapport.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.metria.matdatabas.service.rapport.entity.RapportMottagareEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RapportMottagare {
    String namn;
    String epost;

    public RapportMottagare(RapportMottagareEntity entity) {
        setNamn(entity.getNamn());
        setEpost(entity.getEpost());
    }
}
