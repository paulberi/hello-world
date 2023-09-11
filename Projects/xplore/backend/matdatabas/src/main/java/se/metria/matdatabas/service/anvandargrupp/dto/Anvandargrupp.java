package se.metria.matdatabas.service.anvandargrupp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import se.metria.matdatabas.service.anvandargrupp.entity.AnvandargruppEntity;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Anvandargrupp {
	private Integer id;
	private String namn;
	private String beskrivning;
	private Integer antalAnvandare;
	private LocalDateTime skapadDatum;
	private LocalDateTime andradDatum;
	private Integer andradAvId;

	public static Anvandargrupp fromEntity(AnvandargruppEntity anvandargruppEntity) {
		var anvandargrupp = new Anvandargrupp();
		BeanUtils.copyProperties(anvandargruppEntity, anvandargrupp);
		anvandargrupp.setAntalAnvandare(anvandargruppEntity.getAnvandareCount());
		return anvandargrupp;
	}
}
