package se.metria.matdatabas.service.larm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveLarm {

	private Short status;
	private Integer matobjektId;
	private Long matningId;
	private Double varde;
	private Double gransvarde;
	private Integer gransvardeId;
	private Integer larmnivaId;
	private String larmnivaNamn;
	private Integer anvandargruppId;
	private Short typAvKontroll;
	private String enhet;
	private LocalDateTime avlastDatum;
}
