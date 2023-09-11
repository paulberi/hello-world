package se.metria.markhandlaggning.service.bifogadfil.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.metria.markhandlaggning.service.bifogadfil.entity.BifogadfilEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bifogadfil {

    private UUID id;
    private String filnamn;
    private String mimeTyp;
    private LocalDateTime skapadDatum;
    private byte[] fil;

    public static Bifogadfil fromEntity(BifogadfilEntity bifogadfil) {
        return Bifogadfil.builder()
                .id(bifogadfil.getId())
                .filnamn(bifogadfil.getFilnamn())
                .fil(bifogadfil.getFil())
                .mimeTyp(bifogadfil.getMimeTyp())
                .skapadDatum(bifogadfil.getSkapadDatum())
                .build();
    }

}
