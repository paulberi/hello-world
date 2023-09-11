package se.metria.markhandlaggning.service.projekt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.metria.markhandlaggning.service.bifogadfil.dto.Bifogadfil;
import se.metria.markhandlaggning.service.bifogadfil.entity.BifogadfilEntity;
import se.metria.markhandlaggning.service.projekt.entity.ProjektEntity;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Projekt {

    private UUID id;
    private String projektnr;
    private String avtalstyp;
    private String status;
    private UUID shapeId;
    private UUID markagarlistaId;
    private UUID avtalsId;
    private LocalDateTime skapadDatum;
    private LocalDateTime uppdateradDatum;
    private String felmeddelande;

    public static Projekt fromEntity(ProjektEntity projekt) {
        return Projekt.builder()
                .id(projekt.getId())
                .projektnr(projekt.getProjektnr())
                .avtalstyp(projekt.getAvtalstyp())
                .status(projekt.getStatus())
                .shapeId(projekt.getShapeId())
                .markagarlistaId(projekt.getMarkagarlistaId())
                .avtalsId(projekt.getAvtalsId())
                .skapadDatum(projekt.getSkapadDatum())
                .felmeddelande(projekt.getFelmeddelande())
                .build();
    }

}
