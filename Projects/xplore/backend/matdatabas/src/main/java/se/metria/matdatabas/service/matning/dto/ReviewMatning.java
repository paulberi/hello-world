package se.metria.matdatabas.service.matning.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.metria.matdatabas.service.matning.Matningsfelkod;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewMatning {
	private Double avlastVarde;
	private String kommentar;
	private Short status;
	private Short operation;

}
