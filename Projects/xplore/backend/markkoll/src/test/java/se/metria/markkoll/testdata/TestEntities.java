package se.metria.markkoll.testdata;

import se.metria.markkoll.entity.avtal.AvtalEntity;
import se.metria.markkoll.entity.fastighet.FastighetEntity;
import se.metria.markkoll.entity.projekt.ProjektEntity;
import se.metria.markkoll.entity.vardering.elnat.ElnatRotnettoEntity;
import se.metria.markkoll.entity.vardering.elnat.varderingsprotokoll.ElnatVarderingsprotokollEntity;
import se.metria.markkoll.openapi.model.DetaljtypDto;
import se.metria.markkoll.openapi.model.ProjektTypDto;

import java.util.UUID;

public class TestEntities {
    public static ProjektEntity projektEntity() {
        return ProjektEntity.builder()
            .namn("namn")
            .projekttyp(ProjektTypDto.FIBER)
            .build();
    }

    public static FastighetEntity fastighetEntity(UUID id) {
        return FastighetEntity.builder()
            .id(id)
            .fastighetsbeteckning("fastighetsbeteckning")
            .kommunnamn("kommunnamn")
            .detaljtyp(DetaljtypDto.FASTIGHET)
            .externid("extern-id")
            .build();
    }

    public static AvtalEntity avtalEntity(ProjektEntity projektEntity, FastighetEntity fastighetEntity) {
        return AvtalEntity.builder()
            .projekt(projektEntity)
            .fastighet(fastighetEntity)
            .ersattning(0)
            .build();
    }

    public static ElnatVarderingsprotokollEntity varderingsprotokollEntity(AvtalEntity avtalEntity) {
        return ElnatVarderingsprotokollEntity.builder()
            .avtal(avtalEntity)
            .build();
    }

    public static ElnatRotnettoEntity rotnettoEntity(ElnatVarderingsprotokollEntity varderingsprotokollEntity) {
        return ElnatRotnettoEntity.builder()
            .varderingsprotokoll(varderingsprotokollEntity)
            .ersattning(0)
            .build();
    }
}
