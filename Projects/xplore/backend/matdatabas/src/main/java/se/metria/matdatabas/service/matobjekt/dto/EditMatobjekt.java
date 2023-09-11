package se.metria.matdatabas.service.matobjekt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import se.metria.matdatabas.service.matobjekt.entity.MatobjektEntity;
import se.metria.matdatabas.service.matobjektgrupp.entity.MatobjektgruppEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditMatobjekt {
	private Short typ;
	private String namn;
	private Boolean aktiv;
	private Boolean kontrollprogram;
	private BigDecimal posN;
	private BigDecimal posE;
	private String fastighet;
	private String lage;
	private UUID bifogadBildId;
	private List<Integer> matobjektgrupper;
	private List<UUID> dokument;

	public MatobjektEntity toEntity() {
		return copyToEntity(new MatobjektEntity());
	}

	public MatobjektEntity copyToEntity(MatobjektEntity entity) {
		BeanUtils.copyProperties(this, entity, "matobjektgrupper", "dokument");

		if (this.getMatobjektgrupper() != null) {
			entity.setMatobjektgrupper(this.getMatobjektgrupper().stream()
					.map(MatobjektgruppEntity::new)
					.collect(Collectors.toSet()));
		}

		if (this.getDokument() != null) {
			entity.setDokumentIds(new HashSet<>(this.getDokument()));
		}

		return entity;
	}
}
