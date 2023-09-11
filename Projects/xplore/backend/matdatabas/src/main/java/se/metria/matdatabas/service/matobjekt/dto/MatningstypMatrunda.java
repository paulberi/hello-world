package se.metria.matdatabas.service.matobjekt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.metria.matdatabas.db.tables.records.MatrundaMatningstypRecord;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatningstypMatrunda {
    private Integer matningstypId;
    private Integer matrundaId;

    public static MatningstypMatrunda fromRecord(MatrundaMatningstypRecord source) {
        return MatningstypMatrunda.builder()
                .matningstypId(source.getMatningstypId())
                .matrundaId(source.getMatrundaId())
                .build();
    }
}
