package se.metria.matdatabas.service.matning.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.metria.matdatabas.service.larm.dto.Larm;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatningSaveResult {
    Matning matning;
    List<Larm> larm;
}
