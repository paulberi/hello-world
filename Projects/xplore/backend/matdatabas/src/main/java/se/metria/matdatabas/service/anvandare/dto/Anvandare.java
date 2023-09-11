package se.metria.matdatabas.service.anvandare.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import se.metria.matdatabas.service.anvandare.model.AnvandareEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Anvandare {
	private Integer id;
	private String namn;
	private String foretag;
	private Boolean aktiv;
	private String inloggningsnamn;
	private Short behorighet;
	private Integer defaultKartlagerId;
	private Boolean skickaEpost;
	private String epost;
	private LocalDateTime inloggadSenast;
	private LocalDateTime skapadDatum;
	private LocalDateTime andradDatum;
	private Integer andradAvId;
	private List<Integer> anvandargrupper;

	public static Anvandare fromEntity(AnvandareEntity anvandareEntity) {
		var anvandare = new Anvandare();
		BeanUtils.copyProperties(anvandareEntity, anvandare, "anvandargrupper");
		if (anvandareEntity.getAnvandargrupper() != null) {
			anvandare.setAnvandargrupper(anvandareEntity.getAnvandargrupper().stream()
					.map(e -> e.getId())
					.collect(Collectors.toList()));
		}
		return anvandare;
	}
}
