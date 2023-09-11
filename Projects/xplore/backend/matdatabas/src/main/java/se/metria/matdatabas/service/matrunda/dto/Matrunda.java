package se.metria.matdatabas.service.matrunda.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import se.metria.matdatabas.service.matrunda.entity.MatrundaEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Matrunda {
	private Integer id;
	private String namn;
	private Boolean aktiv;
	private String beskrivning;
	private LocalDateTime skapadDatum;
	private LocalDateTime andradDatum;
	private Integer andradAvId;
	private List<MatrundaMatningstyp> matningstyper;

	public static Matrunda fromEntity(MatrundaEntity matrundaEntity) {
		var matrunda = new Matrunda();
		BeanUtils.copyProperties(matrundaEntity, matrunda, "matningstyper");

		if (matrundaEntity.getMatrundaMatningstyper() != null) {
			matrunda.setMatningstyper(new ArrayList<>(matrundaEntity.getMatrundaMatningstyper().stream()
					.map(MatrundaMatningstyp::fromEntity)
					.collect(Collectors.toList())));
		}

		return matrunda;
	}
}
