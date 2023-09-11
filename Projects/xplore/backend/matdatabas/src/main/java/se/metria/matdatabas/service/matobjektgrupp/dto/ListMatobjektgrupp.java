package se.metria.matdatabas.service.matobjektgrupp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import se.metria.matdatabas.service.matobjektgrupp.entity.MatobjektgruppEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListMatobjektgrupp {
	private Integer id;
	private Short kategori;
	private String namn;
	private String beskrivning;
	private Integer antalMatobjekt;

	public static ListMatobjektgrupp fromEntity(MatobjektgruppEntity entity) {
		var listMatobjektgrupp = new ListMatobjektgrupp();
		BeanUtils.copyProperties(entity, listMatobjektgrupp, "antalMatobjekt");
		listMatobjektgrupp.setAntalMatobjekt(entity.getMatobjektCount());
		return listMatobjektgrupp;
	}
}
