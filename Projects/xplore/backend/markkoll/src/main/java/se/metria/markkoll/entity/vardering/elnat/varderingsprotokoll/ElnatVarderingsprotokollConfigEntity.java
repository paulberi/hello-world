package se.metria.markkoll.entity.vardering.elnat.varderingsprotokoll;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Data
@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ElnatVarderingsprotokollConfigEntity {
    @Column
    Boolean lagspanning = false;

    @Column
    Boolean storskogsbruksavtalet = false;

    @Column
    Boolean ingenGrundersattning = false;

    @Column
    Boolean forhojdMinimumersattning = false;
}
