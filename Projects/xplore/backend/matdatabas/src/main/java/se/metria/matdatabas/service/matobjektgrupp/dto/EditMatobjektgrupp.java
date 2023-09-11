package se.metria.matdatabas.service.matobjektgrupp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import se.metria.matdatabas.service.matobjektgrupp.entity.MatobjektgruppEntity;

import java.util.HashSet;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditMatobjektgrupp {
	private Short kategori;
	private String namn;
	private Short kartsymbol;
	private String beskrivning;
	private List<Integer> matobjekt;

	public MatobjektgruppEntity toEntity() {
		return copyToEntity(new MatobjektgruppEntity());
	}

	public MatobjektgruppEntity copyToEntity(MatobjektgruppEntity entity) {
		BeanUtils.copyProperties(this, entity, "matobjekt");

		if (this.getMatobjekt() != null) {
			entity.setMatobjektIds(new HashSet<>(this.getMatobjekt()));
		}

		return entity;
	}
}
