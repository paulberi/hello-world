package se.metria.matdatabas.service.matrunda.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import se.metria.matdatabas.service.matrunda.entity.MatrundaEntity;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditMatrunda {
	private String namn;
	private Boolean aktiv;
	private String beskrivning;
	private List<MatrundaMatningstyp> matningstyper;
}
