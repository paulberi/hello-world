package se.metria.matdatabas.service.matning.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatningSearchFilter {
	private LocalDateTime from;
	private LocalDateTime to;
	private Short status;
	private Boolean filterGranskade;
	private Boolean filterFelkodOk;
}
