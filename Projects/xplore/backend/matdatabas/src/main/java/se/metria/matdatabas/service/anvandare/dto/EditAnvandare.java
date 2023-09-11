package se.metria.matdatabas.service.anvandare.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import se.metria.matdatabas.service.anvandare.model.AnvandareEntity;
import se.metria.matdatabas.service.anvandargrupp.entity.AnvandargruppEntity;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditAnvandare {
	private String namn;
	private String foretag;
	private Boolean aktiv;
	private String inloggningsnamn;
	private Short behorighet;
	private Integer defaultKartlagerId;
	private Boolean skickaEpost;
	private String epost;
	private List<Integer> anvandargrupper;

	public AnvandareEntity toEntity() {
		return copyToEntity(new AnvandareEntity());
	}

	public AnvandareEntity copyToEntity(AnvandareEntity entity) {
		BeanUtils.copyProperties(this, entity, "anvandargrupper");
		if (this.getAnvandargrupper() != null) {
			entity.setAnvandargrupper(this.getAnvandargrupper().stream()
					.map(id -> new AnvandargruppEntity(id))
					.collect(Collectors.toSet()));
		}
		return entity;
	}
}
