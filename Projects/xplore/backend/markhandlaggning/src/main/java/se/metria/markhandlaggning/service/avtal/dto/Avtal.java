package se.metria.markhandlaggning.service.avtal.dto;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.metria.markhandlaggning.service.avtal.entity.AvtalEntity;
import se.metria.markhandlaggning.service.bifogadfil.dto.Bifogadfil;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Avtal {

    private UUID id;
    private String filnamn;
    private String mimeTyp;
    private LocalDateTime skapadDatum;
    private LocalDateTime uppdateradDatum;
    private byte[] fil;

    public static Avtal fromEntity(AvtalEntity avtalEntity) {
        return Avtal.builder()
                .filnamn(avtalEntity.getFilnamn())
                .fil(avtalEntity.getFil())
                .mimeTyp(avtalEntity.getMimeTyp())
                .build();
    }

}
