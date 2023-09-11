package se.metria.matdatabas.service.matning.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.metria.matdatabas.service.matning.Matningsfelkod;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveMatning {
	private LocalDateTime avlastDatum;
	private Double avlastVarde;
	private Double beraknatVarde;
	private String kommentar;
	private String felkod;
	private String rapportor;
	private Short inomDetektionsomrade;

	public boolean hasFelkod() {
		return felkod != null && !felkod.equals(Matningsfelkod.OK);
	}
}
