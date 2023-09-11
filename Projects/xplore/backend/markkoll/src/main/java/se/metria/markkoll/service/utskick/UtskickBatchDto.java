package se.metria.markkoll.service.utskick;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UtskickBatchDto {
    private String title;
    private List<UtskickDto> utskick;
}
