package se.metria.markkoll.service.utskick;

import lombok.*;
import se.metria.markkoll.openapi.model.MarkagareDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UtskickDto {
    private String titel;
    private String lopnummer;

    @Builder.Default
    private Optional<MarkagareDto> kontaktperson = Optional.empty();

    @Builder.Default
    private List<MarkagareDto> signatarer = new ArrayList<>();

    @Builder.Default
    private List<UtskickVpDto> utskickVp = new ArrayList<>();
}
