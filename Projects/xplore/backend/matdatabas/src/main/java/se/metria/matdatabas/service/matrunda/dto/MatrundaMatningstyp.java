package se.metria.matdatabas.service.matrunda.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import se.metria.matdatabas.service.matrunda.entity.MatrundaMatningstypEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatrundaMatningstyp {
	private Integer matningstypId;
	private Short ordning;

	public static MatrundaMatningstyp fromEntity(MatrundaMatningstypEntity entity) {
		var matrundaMatningstyp = new MatrundaMatningstyp();
		BeanUtils.copyProperties(entity, matrundaMatningstyp);
		return matrundaMatningstyp;
	}
}
