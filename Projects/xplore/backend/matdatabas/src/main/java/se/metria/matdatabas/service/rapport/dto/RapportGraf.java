package se.metria.matdatabas.service.rapport.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.metria.matdatabas.openapi.model.GransvardeDto;
import se.metria.matdatabas.service.matning.dto.MatningDataSeries;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RapportGraf {
    String rubrik;
    String information;
    List<MatningDataSeries> matningar;
    List<MatningDataSeries> referensdata;
    List<GransvardeDto> gransvarden;
}
