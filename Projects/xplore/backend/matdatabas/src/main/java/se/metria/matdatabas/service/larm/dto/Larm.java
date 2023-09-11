package se.metria.matdatabas.service.larm.dto;

import lombok.Data;
import org.jooq.Record;
import se.metria.matdatabas.openapi.model.LarmDto;
import se.metria.matdatabas.service.larm.entity.LarmEntity;

import static se.metria.matdatabas.db.Tables.*;
import static se.metria.matdatabas.db.Tables.ANVANDARE;
import static se.metria.matdatabas.db.tables.DefinitionMatningstyp.DEFINITION_MATNINGSTYP;
import static se.metria.matdatabas.db.tables.Larm.LARM;
import static se.metria.matdatabas.db.tables.Matning.MATNING;
import static se.metria.matdatabas.db.tables.Matningstyp.MATNINGSTYP;
import static se.metria.matdatabas.db.tables.Matobjekt.MATOBJEKT;
import static se.metria.matdatabas.db.tables.MatobjektTyp.MATOBJEKT_TYP;

@Data
public class Larm extends LarmDto {

	public Larm() {
		super();
	}

	public static Larm fromEntity(LarmEntity entity) {
		return (Larm) new Larm()
				.id(entity.getId())
				.status(Integer.valueOf(entity.getStatus()))
				.matobjektId(entity.getMatobjektId())
				.matningId(entity.getMatningId())
				.varde(entity.getVarde())
				.gransvarde(entity.getGransvarde())
				.gransvardeId(entity.getGransvardeId())
				.kvitteradDatum(entity.getKvitteradDatum())
				.kvitteradAvId(entity.getKvitteradAvId())
				.larmnivaId(entity.getLarmnivaId())
				.anvandargruppId(entity.getAnvandargruppId())
				.typAvKontroll(Integer.valueOf(entity.getTypAvKontroll()));
	}

	public static Larm fromEntityAndSaveLarm(LarmEntity entity, SaveLarm saveLarm) {
		var larm = fromEntity(entity);
		larm.setLarmnivaNamn(saveLarm.getLarmnivaNamn());
		larm.setEnhet(saveLarm.getEnhet());
		larm.setAvlastDatum(saveLarm.getAvlastDatum());

		return larm;
	}

	public Larm(Record source) {
		super();
		setId(source.get(LARM.ID));
		setMatningstypNamn(source.get(DEFINITION_MATNINGSTYP.NAMN));
		setMatningstypDecimaler(Integer.valueOf(source.get(MATNINGSTYP.DECIMALER)));
		setMatobjektNamn(source.get(MATOBJEKT.NAMN));
		setMatobjektId(source.get(MATOBJEKT.ID));
		setMatningId(source.get(LARM.MATNING_ID));
		setMatobjektTyp(source.get(MATOBJEKT_TYP.NAMN));
		setMatobjektFastighet(source.get(MATOBJEKT.FASTIGHET));
		setAvlastDatum(source.get(MATNING.AVLAST_DATUM));
		setVarde(source.get(LARM.VARDE));
		setEnhet(source.get(MATNINGSTYP.ENHET));
		setLarmnivaId(source.get(LARM.LARMNIVA_ID));
		setLarmnivaNamn(source.get(LARMNIVA.NAMN));
		setTypAvKontroll(Integer.valueOf(source.get(LARM.TYP_AV_KONTROLL)));
		setGransvarde(source.get(GRANSVARDE.GRANSVARDE_));
		setAnvandargruppNamn(source.get(ANVANDARGRUPP.NAMN));
		setAnvandargruppId(source.get(LARM.ANVANDARGRUPP_ID));
		setStatus(Integer.valueOf(source.get(LARM.STATUS)));
		setKvitteradDatum(source.get(LARM.KVITTERAD_DATUM));
		setKvitteradAvId(source.get(LARM.KVITTERAD_AV_ID));
		setKvitteradAv(source.get(ANVANDARE.NAMN));
	}
}
