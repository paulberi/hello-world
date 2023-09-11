package se.metria.markhandlaggning.service.projekt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import se.metria.markhandlaggning.service.projekt.entity.ProjektEntity;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveProjekt {
    private String projektnr;
    private String avtalstyp;
    private String status;
    private UUID shapeId;
    private UUID markagarlistaId;

    public ProjektEntity toEntity() {
        return copyToEntity(new ProjektEntity());
    }
    public ProjektEntity copyToEntity(ProjektEntity entity) {
        BeanUtils.copyProperties(this, entity);
        return entity;
    }

}
