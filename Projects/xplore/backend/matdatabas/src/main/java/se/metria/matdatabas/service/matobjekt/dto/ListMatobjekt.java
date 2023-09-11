package se.metria.matdatabas.service.matobjekt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.metria.matdatabas.db.tables.records.MatobjektRecord;
import se.metria.matdatabas.service.matobjekt.entity.MatobjektEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListMatobjekt {
	private Integer id;
	private Short typ;
	private String namn;
	private Boolean aktiv;
	private String fastighet;
	private String lage;

	public static ListMatobjekt fromRecord(MatobjektRecord record) {
		return ListMatobjekt.builder()
				.id(record.getId())
				.typ(record.getTyp())
				.namn(record.getNamn())
				.aktiv(record.getAktiv())
				.fastighet(record.getFastighet())
				.lage(record.getLage())
				.build();
	}
}
