package se.metria.matdatabas.service.paminnelse;

import org.jooq.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;
import se.metria.matdatabas.service.matningstyp.query.MatningstypSearchFilter;
import se.metria.matdatabas.service.paminnelse.query.PaminnelseSearchFilter;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.jooq.impl.DSL.*;
import static se.metria.matdatabas.common.JooqQueryUtils.paged;
import static se.metria.matdatabas.db.tables.DefinitionMatningstyp.DEFINITION_MATNINGSTYP;
import static se.metria.matdatabas.db.tables.Matning.MATNING;
import static se.metria.matdatabas.db.tables.Matningstyp.MATNINGSTYP;
import static se.metria.matdatabas.db.tables.Matobjekt.MATOBJEKT;
import static se.metria.matdatabas.db.tables.MatobjektTyp.MATOBJEKT_TYP;
import static se.metria.matdatabas.service.matningstyp.MatningstypJooqRepository.matningstypSearchFilter;
import static se.metria.matdatabas.service.paminnelse.PaminnelseJooqFields.*;

@Component
public class PaminnelseJooqRepository {
	private DSLContext create;
	private Map<String, Field<?>> sortColumns = new HashMap<>();

	public PaminnelseJooqRepository(DSLContext create) {
		this.create = create;

		// We allow sorting on these keys.
		// Not very attractive but I couldn't figure out anything good.
		sortColumns.put("matningstypId", field(MATANSVARIG_ID));
		sortColumns.put("matningstypNamn", field(MATNINGSTYP_NAMN));
		sortColumns.put("matobjektId", field(MATOBJEKT_ID));
		sortColumns.put("matobjektNamn", field(MATOBJEKT_NAMN));
		sortColumns.put("matobjektTyp", field(MATOBJEKT_TYP_NAMN));
		sortColumns.put("matobjektFastighet", field(MATOBJEKT_FASTIGHET));
		sortColumns.put("matobjektLage",field(MATOBJEKT_LAGE));
		sortColumns.put("forsenad", field(name(FORSENAD_DAGAR)));
		sortColumns.put("avlastDatum", field(AVLAST_DATUM));
	}


	private SelectConditionStep<Record> paminnelser(MatningstypSearchFilter matningstypSearchFilter,
													PaminnelseSearchFilter paminnelseSearchFilter) {
		Condition aktiv = MATNINGSTYP.AKTIV.isTrue().and(MATOBJEKT.AKTIV.isTrue());

		Table<?> nested = select(MATNING.MATNINGSTYP_ID,
				max(MATNING.AVLAST_DATUM).as(AVLAST_DATUM))
				.from(MATNING)
				.groupBy(MATNING.MATNINGSTYP_ID).asTable("senaste_matning");

		Table<?> matningar = select(
				MATNINGSTYP.ID.as(MATNINGSTYP_ID),
				DEFINITION_MATNINGSTYP.NAMN.as(MATNINGSTYP_NAMN),
				MATOBJEKT.ID.as(MATOBJEKT_ID),
				MATOBJEKT.NAMN.as(MATOBJEKT_NAMN),
				MATOBJEKT.FASTIGHET.as(MATOBJEKT_FASTIGHET),
				MATOBJEKT.LAGE.as(MATOBJEKT_LAGE),
				MATOBJEKT_TYP.NAMN.as(MATOBJEKT_TYP_NAMN),
				MATNINGSTYP.MATINTERVALL_ANTAL_GANGER.as(ANTAL_GANGER),
				MATNINGSTYP.MATINTERVALL_TIDSENHET.as(TIDSENHET),
				MATNINGSTYP.MATANSVARIG_ANVANDARGRUPP_ID.as(MATANSVARIG_ID),
				MATNINGSTYP.PAMINNELSE_DAGAR,
				nested.field(AVLAST_DATUM),
				case_().when(nested.field(MATNING.AVLAST_DATUM).isNull(), MATNINGSTYP.ANDRAD_DATUM)
						.otherwise(nested.field(MATNING.AVLAST_DATUM)).as(SENASTE_DATUM))
				.from(nested)
				.fullOuterJoin(MATNINGSTYP)
				.on(MATNINGSTYP.ID.eq(nested.field(MATNING.MATNINGSTYP_ID)))
				.leftOuterJoin(MATOBJEKT).on(MATOBJEKT.ID.eq(MATNINGSTYP.MATOBJEKT_ID))
				.leftOuterJoin(DEFINITION_MATNINGSTYP).on(DEFINITION_MATNINGSTYP.ID.eq(MATNINGSTYP.DEFINITION_MATNINGSTYP_ID))
				.leftOuterJoin(MATOBJEKT_TYP).on(MATOBJEKT_TYP.ID.eq(MATOBJEKT.TYP))
				.where(matningstypSearchFilter(matningstypSearchFilter).and(aktiv)).asTable("matningar");

		Table<?> paminnelser = select(matningar.asterisk(),
				case_(matningar.field(TIDSENHET).cast(Short.class))
						.when((short) 1, timestampAdd(matningar.field(SENASTE_DATUM).cast(Timestamp.class),
								matningar.field(MATNINGSTYP.PAMINNELSE_DAGAR).add(1), DatePart.DAY))
						.when((short) 2, timestampAdd(matningar.field(SENASTE_DATUM).cast(Timestamp.class),
								matningar.field(MATNINGSTYP.PAMINNELSE_DAGAR).add(7), DatePart.DAY))
						.when((short) 3, timestampAdd(timestampAdd(matningar.field(SENASTE_DATUM).cast(Timestamp.class),
								1, DatePart.MONTH), matningar.field(MATNINGSTYP.PAMINNELSE_DAGAR), DatePart.DAY))
						.when((short) 4, timestampAdd(timestampAdd(matningar.field(SENASTE_DATUM).cast(Timestamp.class),
								1, DatePart.YEAR), matningar.field(MATNINGSTYP.PAMINNELSE_DAGAR), DatePart.DAY))
						.otherwise(timestampAdd(matningar.field(SENASTE_DATUM).cast(Timestamp.class),
								matningar.field(MATNINGSTYP.PAMINNELSE_DAGAR), DatePart.DAY))
						.as(MATDATUM)).from(matningar).asTable("paminnelser");

		Table<?> forsenade = select(paminnelser.asterisk(),
				case_().when(paminnelser.field(MATDATUM).cast(Timestamp.class).greaterOrEqual(now()), 0)
						.otherwise(extract(now().sub(paminnelser.field(MATDATUM).cast(Timestamp.class)), DatePart.DAY)).as(FORSENAD_DAGAR))
				.from(paminnelser).asTable("forsenade");

		return select(forsenade.asterisk()).from(forsenade).where(paminnelseSearchFilter(paminnelseSearchFilter));


	}

	public List<Record> getPaminnelserList(MatningstypSearchFilter matningstypSearchFilter,
										   PaminnelseSearchFilter paminnelseSearchFilter) {
		var query = paminnelser(matningstypSearchFilter, paminnelseSearchFilter).orderBy(field(MATOBJEKT_NAMN));
		return  create.fetch(query);
	}

	public Page<Record> getPaminnelserPage(MatningstypSearchFilter matningstypSearchFilter,
										   PaminnelseSearchFilter paminnelseSearchFilter,
										   Pageable pageable) {
		var query = paminnelser(matningstypSearchFilter, paminnelseSearchFilter);
		return paged(create, query, pageable, sortColumns);
	}

	public static Condition paminnelseSearchFilter(PaminnelseSearchFilter filter) {
		Condition cond = noCondition();
		if (filter.getOnlyForsenade() != null && filter.getOnlyForsenade()) {
			cond = cond.and(field(FORSENAD_DAGAR).greaterThan(0));
		}
		return cond;
	}
}
