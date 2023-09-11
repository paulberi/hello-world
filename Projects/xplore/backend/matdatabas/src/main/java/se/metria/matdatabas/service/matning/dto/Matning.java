package se.metria.matdatabas.service.matning.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jooq.Record;
import org.jooq.TableField;
import org.jooq.impl.DSL;
import se.metria.matdatabas.db.tables.records.MatningRecord;
import se.metria.matdatabas.service.matning.Matningsfelkod;
import se.metria.matdatabas.service.matning.entity.MatningEntity;

import java.time.LocalDateTime;

import static se.metria.matdatabas.db.tables.Matning.MATNING;
import static se.metria.matdatabas.db.tables.MatrundaMatningar.MATRUNDA_MATNINGAR;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Matning {
	private Long id;
	private LocalDateTime avlastDatum;
	private Double avlastVarde;
	private Short inomDetektionsomrade;
	private Double beraknatVarde;
	private Short status;
	private String kommentar;
	private String felkod;
	private String rapportor;
	private Integer matningstypId;

	public Matning(Record source) {
		id = source.get(MATNING.ID);
		avlastVarde = source.get(MATNING.AVLAST_VARDE);
		inomDetektionsomrade = source.get(MATNING.INOM_DETEKTIONSOMRADE);
		beraknatVarde = source.get(MATNING.BERAKNAT_VARDE);
		status = source.get(MATNING.STATUS);
		kommentar = source.get(MATNING.KOMMENTAR);
		felkod = source.get(MATNING.FELKOD);
		rapportor = source.get(MATNING.RAPPORTOR);
		matningstypId = source.get(MATNING.MATNINGSTYP_ID);
		avlastDatum = source.get("avlast_datum",java.sql.Timestamp.class).toLocalDateTime();
	}

	public static Matning fromEntity(MatningEntity entity) {
		return Matning.builder()
				.id(entity.getId())
				.avlastVarde(entity.getAvlastVarde())
				.avlastDatum(entity.getAvlastDatum())
				.inomDetektionsomrade(entity.getInomDetektionsomrade())
				.beraknatVarde(entity.getBeraknatVarde())
				.status(entity.getStatus())
				.kommentar(entity.getKommentar())
				.felkod(entity.getFelkod())
				.rapportor(entity.getRapportor())
				.matningstypId(entity.getMatningstypId())
				.build();
	}

	public boolean hasFelkod() {
		return (felkod != null) && !felkod.equals(Matningsfelkod.OK);
	}
}
