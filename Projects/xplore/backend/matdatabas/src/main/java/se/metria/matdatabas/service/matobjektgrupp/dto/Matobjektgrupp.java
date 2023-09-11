package se.metria.matdatabas.service.matobjektgrupp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import se.metria.matdatabas.service.matobjektgrupp.entity.MatobjektgruppEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Matobjektgrupp {
	private Integer id;
	private Short kategori;
	private String namn;
	private Short kartsymbol;
	private String beskrivning;
	private LocalDateTime skapadDatum;
	private LocalDateTime andradDatum;
	private Integer andradAvId;
	private List<Integer> matobjekt;

	public static Matobjektgrupp fromEntity(MatobjektgruppEntity entity) {
		var matobjektgrupp = new Matobjektgrupp();
		BeanUtils.copyProperties(entity, matobjektgrupp, "matobjekt");

		if (entity.getMatobjektIds() != null) {
			matobjektgrupp.setMatobjekt(new ArrayList<>(entity.getMatobjektIds()));
		}

		return matobjektgrupp;
	}
}
