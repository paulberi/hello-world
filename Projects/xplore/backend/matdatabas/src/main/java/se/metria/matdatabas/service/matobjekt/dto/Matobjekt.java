package se.metria.matdatabas.service.matobjekt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jooq.Record;
import org.springframework.beans.BeanUtils;
import se.metria.matdatabas.service.matobjekt.entity.MatobjektEntity;
import se.metria.matdatabas.service.matobjektgrupp.entity.MatobjektgruppEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static se.metria.matdatabas.db.tables.Matobjekt.MATOBJEKT;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Matobjekt {
	private Integer id;
	private Short typ;
	private String namn;
	private Boolean aktiv;
	private Boolean kontrollprogram;
	private BigDecimal posN;
	private BigDecimal posE;
	private String fastighet;
	private String lage;
	private UUID bifogadBildId;
	private LocalDateTime skapadDatum;
	private LocalDateTime andradDatum;
	private Integer andradAvId;
	private List<Integer> matobjektgrupper;
	private List<MatningstypMatrunda> matrundor;
	private List<UUID> dokument;

	public Matobjekt(Record source) {
		id = source.get(MATOBJEKT.ID);
		typ = source.get(MATOBJEKT.TYP);
		namn = source.get(MATOBJEKT.NAMN);
		aktiv = source.get(MATOBJEKT.AKTIV);
		fastighet = source.get(MATOBJEKT.FASTIGHET);
		lage = source.get(MATOBJEKT.LAGE);
		posN = source.get(MATOBJEKT.POS_N);
		posE = source.get(MATOBJEKT.POS_E);
	}

	public static Matobjekt fromEntity(MatobjektEntity entity) {
		var matobjekt = new Matobjekt();
		BeanUtils.copyProperties(entity, matobjekt, "matobjektgrupper", "dokument");

		if (entity.getMatobjektgrupper() != null) {
			matobjekt.setMatobjektgrupper(entity.getMatobjektgrupper().stream()
					.map(MatobjektgruppEntity::getId)
					.collect(Collectors.toList()));
		}

		if (entity.getDokumentIds() != null) {
			matobjekt.setDokument(new ArrayList<>(entity.getDokumentIds()));
		}

		return matobjekt;
	}
}
