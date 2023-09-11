package se.metria.matdatabas.service.anvandargrupp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import se.metria.matdatabas.service.anvandargrupp.entity.AnvandargruppEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditAnvandargrupp {
	private String namn;
	private String beskrivning;

	public AnvandargruppEntity toEntity() {
		return copyToEntity(new AnvandargruppEntity());
	}
	public AnvandargruppEntity copyToEntity(AnvandargruppEntity entity) {
		BeanUtils.copyProperties(this, entity);
		return entity;
	}
}
