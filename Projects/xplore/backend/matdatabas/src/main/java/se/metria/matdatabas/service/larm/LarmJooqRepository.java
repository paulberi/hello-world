package se.metria.matdatabas.service.larm;

import org.jooq.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;
import se.metria.matdatabas.service.larm.query.LarmSearchFilter;
import se.metria.matdatabas.service.matningstyp.query.MatningstypSearchFilter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.jooq.impl.DSL.*;
import static se.metria.matdatabas.common.JooqQueryUtils.paged;
import static se.metria.matdatabas.db.Tables.*;
import static se.metria.matdatabas.db.tables.DefinitionMatningstyp.DEFINITION_MATNINGSTYP;
import static se.metria.matdatabas.db.tables.Gransvarde.GRANSVARDE;
import static se.metria.matdatabas.db.tables.Larm.LARM;
import static se.metria.matdatabas.db.tables.Matning.MATNING;
import static se.metria.matdatabas.db.tables.Matningstyp.MATNINGSTYP;
import static se.metria.matdatabas.db.tables.Matobjekt.MATOBJEKT;
import static se.metria.matdatabas.db.tables.MatobjektTyp.MATOBJEKT_TYP;
import static se.metria.matdatabas.service.matningstyp.MatningstypJooqRepository.matningstypSearchFilter;

@Component
public class LarmJooqRepository {
	private DSLContext create;
	private Map<String, Field<?>> sortColumns = new HashMap<>();

	public LarmJooqRepository(DSLContext create) {
		this.create = create;

		// We allow sorting on these keys.
		// Not very attractive but I couldn't figure out anything good.
		sortColumns.put("matobjektId", MATOBJEKT.ID);
		sortColumns.put("matningstypId", MATNINGSTYP.ID);
		sortColumns.put("matningstypNamn", DEFINITION_MATNINGSTYP.NAMN);
		sortColumns.put("matobjektNamn", MATOBJEKT.NAMN);
		sortColumns.put("matobjektTyp", MATOBJEKT_TYP.NAMN);
		sortColumns.put("matobjektFastighet", MATOBJEKT.FASTIGHET);
		sortColumns.put("avlastDatum", MATNING.AVLAST_DATUM);
		sortColumns.put("larmnivaNamn", LARMNIVA.NAMN);
		sortColumns.put("typAvKontroll", LARM.TYP_AV_KONTROLL);
		sortColumns.put("gransvarde", GRANSVARDE.GRANSVARDE_);
		sortColumns.put("anvandargruppNamn", ANVANDARGRUPP.NAMN);
		sortColumns.put("status", LARM.STATUS);
		sortColumns.put("kvitteradDatum", LARM.KVITTERAD_DATUM);
		sortColumns.put("kvitteradAv", ANVANDARE.NAMN);
	}

	private SelectConditionStep<Record> larmList(LarmSearchFilter larmSearchFilter,
												 MatningstypSearchFilter matningstypSearchFilter) {
		var searchFilter = larmSearchFilter(larmSearchFilter)
				.and(matningstypSearchFilter(matningstypSearchFilter));

		SelectConditionStep<Record> records = select(
				LARM.asterisk(),
				GRANSVARDE.MATNINGSTYP_ID,
				decode().when(DEFINITION_MATNINGSTYP.BERAKNAD_ENHET.isNull(), MATNINGSTYP.ENHET).otherwise(DEFINITION_MATNINGSTYP.BERAKNAD_ENHET).as("enhet"),
				MATNING.AVLAST_DATUM,
				MATNING.STATUS,
				MATNINGSTYP.ID,
				DEFINITION_MATNINGSTYP.NAMN,
				MATNINGSTYP.DECIMALER,
				MATOBJEKT.ID,
				MATOBJEKT.NAMN,
				MATOBJEKT.FASTIGHET,
				MATOBJEKT_TYP.NAMN,
				LARMNIVA.NAMN,
				ANVANDARGRUPP.NAMN,
				ANVANDARE.NAMN
		)
				.from(LARM)
				.join(GRANSVARDE).on(LARM.GRANSVARDE_ID.eq(GRANSVARDE.ID))
				.join(MATNING).on(LARM.MATNING_ID.eq(MATNING.ID))
				.join(MATOBJEKT).on(LARM.MATOBJEKT_ID.eq(MATOBJEKT.ID))
				.join(MATNINGSTYP).on(MATNING.MATNINGSTYP_ID.eq(MATNINGSTYP.ID))
				.join(DEFINITION_MATNINGSTYP).on(MATNINGSTYP.DEFINITION_MATNINGSTYP_ID.eq(DEFINITION_MATNINGSTYP.ID))
				.join(MATOBJEKT_TYP).on(MATOBJEKT_TYP.ID.eq(MATOBJEKT.TYP))
				.join(LARMNIVA).on(LARM.LARMNIVA_ID.eq(LARMNIVA.ID))
				.leftJoin(ANVANDARGRUPP).on(GRANSVARDE.LARM_TILL_ANVANDARGRUPP_ID.eq(ANVANDARGRUPP.ID))
				.leftJoin(ANVANDARE).on(LARM.KVITTERAD_AV_ID.eq(ANVANDARE.ID))
				.where(searchFilter);

		return records;
	}

	public List<Record> getLarmList(LarmSearchFilter larmSearchFilter, MatningstypSearchFilter matningstypSearchFilter) {
		var query = larmList(larmSearchFilter, matningstypSearchFilter).orderBy(MATOBJEKT.NAMN);
		return  create.fetch(query);
	}

	public Stream<Record> larmlistStream(LarmSearchFilter larmSearchFilter, MatningstypSearchFilter matningstypSearchFilter) {
		var query = larmList(larmSearchFilter, matningstypSearchFilter);
		query.attach(create.configuration());
		return create.fetchStream(query);
	}

	public Page<Record> larmListPaged(LarmSearchFilter larmSearchFilter,
									  MatningstypSearchFilter matningstypSearchFilter,
									  Pageable pageable) {
		var query = larmList(larmSearchFilter, matningstypSearchFilter);
		return paged(create, query, pageable, sortColumns);
	}


	public static Condition larmSearchFilter(LarmSearchFilter filter) {
		Condition cond = noCondition();
		if (filter.getLarmStatus() != null) {
			cond = cond.and(LARM.STATUS.eq(filter.getLarmStatus()));
		}
		if (filter.getLarmTillAnvandargruppIds() != null) {
			cond = cond.and(LARM.ANVANDARGRUPP_ID.in(filter.getLarmTillAnvandargruppIds()));
		}
		if (filter.getLarmniva() != null) {
			cond = cond.and(LARMNIVA.ID.eq(filter.getLarmniva()));
		}
		return cond;
	}
}
