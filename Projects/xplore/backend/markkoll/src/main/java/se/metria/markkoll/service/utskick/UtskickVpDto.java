package se.metria.markkoll.service.utskick;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.metria.markkoll.openapi.model.MarkagareDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UtskickVpDto {
    private String title;
    @Builder.Default
    private Optional<MarkagareDto> kontaktperson = Optional.empty();
    @Builder.Default
    private List<MarkagareDto> signatarer = new ArrayList<>();
}
