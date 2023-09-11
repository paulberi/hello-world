package se.metria.matdatabas.service.bifogadfil.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import se.metria.matdatabas.service.bifogadfil.entity.BifogadfilEntity;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveBifogadfil {
	private String filnamn;
	private String mimeTyp;
	private byte[] fil;
	private byte[] thumbnail;

	public BifogadfilEntity toEntity() {
		return copyToEntity(new BifogadfilEntity());
	}
	public BifogadfilEntity copyToEntity(BifogadfilEntity entity) {
		BeanUtils.copyProperties(this, entity);
		entity.setSkapadDatum(LocalDateTime.now());
		return entity;
	}
}
