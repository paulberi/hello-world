package se.metria.matdatabas.service.matningstyp;

import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;
import se.metria.matdatabas.service.matning.MatningJooqRepository;
import se.metria.matdatabas.service.matning.Matningstatus;
import se.metria.matdatabas.service.matning.query.MatningSearchFilter;
import se.metria.matdatabas.service.matningstyp.query.MatningstypSearchFilter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.jooq.impl.DSL.*;
import static se.metria.matdatabas.common.JooqQueryUtils.paged;
import static se.metria.matdatabas.db.tables.DefinitionMatningstyp.DEFINITION_MATNINGSTYP;
import static se.metria.matdatabas.db.tables.GruppMatobjekt.GRUPP_MATOBJEKT;
import static se.metria.matdatabas.db.tables.Matning.MATNING;
import static se.metria.matdatabas.db.tables.Matningstyp.MATNINGSTYP;
import static se.metria.matdatabas.db.tables.Matobjekt.MATOBJEKT;
import static se.metria.matdatabas.db.tables.MatobjektTyp.MATOBJEKT_TYP;
import static se.metria.matdatabas.db.tables.MatrundaMatningstyp.MATRUNDA_MATNINGSTYP;

@Component
public class MatningstypJooqRepository {
	private DSLContext create;
	private Map<String, Field<?>> sortColumns = new HashMap<>();

	public MatningstypJooqRepository(DSLContext create) {
		this.create = create;

		// We allow sorting on these keys.
		// Not very attractive but I couldn't figure out anything good.
		sortColumns.put("matningstypId", MATNINGSTYP.ID);
		sortColumns.put("matningstypAktiv", MATNINGSTYP.AKTIV);
		sortColumns.put("matningstypNamn", DEFINITION_MATNINGSTYP.NAMN);
		sortColumns.put("matobjektId", MATOBJEKT.ID);
		sortColumns.put("matobjektNamn", MATOBJEKT.NAMN);
		sortColumns.put("matobjektTyp", MATOBJEKT_TYP.NAMN);
		sortColumns.put("matobjektFastighet", MATOBJEKT.FASTIGHET);
		sortColumns.put("matobjektLage", MATOBJEKT.LAGE);
		sortColumns.put("matobjektAktiv", MATOBJEKT.AKTIV);
		sortColumns.put("ejGranskade", field(name("ej_granskade")));
		sortColumns.put("aktiv", field(name("bothAktiv")));
		sortColumns.put("antalMatningar", field(name("antal_matningar")));
	}

	public Page<Record> matningstyperAntalMatningar(MatningstypSearchFilter filter, LocalDateTime fromDatum, LocalDateTime tomDatum, Short matningStatus, Pageable pageable) {
		MatningSearchFilter matningFilter = MatningSearchFilter.builder()
				.from(fromDatum)
				.to(tomDatum)
				.status(matningStatus)
				.build();

		SelectHavingStep<Record> select = select(MATOBJEKT.asterisk(), MATNINGSTYP.ID, DEFINITION_MATNINGSTYP.NAMN,  DEFINITION_MATNINGSTYP.STORHET, count(MATNING.ID).as("antal_matningar"))
				.from(MATOBJEKT)
				.join(MATNINGSTYP).on(MATNINGSTYP.MATOBJEKT_ID.eq(MATOBJEKT.ID))
				.join(DEFINITION_MATNINGSTYP).on(DEFINITION_MATNINGSTYP.ID.eq(MATNINGSTYP.DEFINITION_MATNINGSTYP_ID))
				.leftJoin(MATNING).on(MATNING.MATNINGSTYP_ID.eq(MATNINGSTYP.ID))
				.where(matningstypSearchFilter(filter).and(MatningJooqRepository.matningSearchFilter(matningFilter)))
				.groupBy(MATOBJEKT.ID, MATNINGSTYP.ID, DEFINITION_MATNINGSTYP.ID, MATNING.MATNINGSTYP_ID);

		return paged(create, select, pageable, sortColumns);
	}

	public SelectHavingStep<Record> matningstyperAntalEjGranskade(MatningstypSearchFilter filter) {
		Condition aktiv = MATNINGSTYP.AKTIV.isTrue().and(MATOBJEKT.AKTIV.isTrue());
		SelectHavingStep<Record> select = select(MATOBJEKT.asterisk(), MATNINGSTYP.asterisk(), MATOBJEKT_TYP.NAMN, DEFINITION_MATNINGSTYP.NAMN, DEFINITION_MATNINGSTYP.STORHET,  field(aktiv).as("bothAktiv"), count(MATNING.ID).as("ej_granskade"))
				.from(MATOBJEKT)
				.join(MATOBJEKT_TYP).on(MATOBJEKT_TYP.ID.eq(MATOBJEKT.TYP))
				.join(MATNINGSTYP).on(MATNINGSTYP.MATOBJEKT_ID.eq(MATOBJEKT.ID))
				.join(DEFINITION_MATNINGSTYP).on(DEFINITION_MATNINGSTYP.ID.eq(MATNINGSTYP.DEFINITION_MATNINGSTYP_ID))
				.leftJoin(MATNING).on(MATNING.MATNINGSTYP_ID.eq(MATNINGSTYP.ID).and(MATNING.STATUS.eq(Matningstatus.EJGRANSKAT)))
				.where(matningstypSearchFilter(filter))
				.groupBy(MATOBJEKT.ID, MATNINGSTYP.ID, MATOBJEKT_TYP.NAMN, DEFINITION_MATNINGSTYP.ID, MATNING.MATNINGSTYP_ID);

		return select;
	}

	public List<Record> matningstyperAntalEjGranskadeList(MatningstypSearchFilter filter) {
		var query = matningstyperAntalEjGranskade(filter).orderBy(MATOBJEKT.NAMN);
		return  create.fetch(query);
	}

	public Page<Record> matningstyperAntalEjGranskadePage(MatningstypSearchFilter filter, Pageable pageable) {
		var query = matningstyperAntalEjGranskade(filter);
		return paged(create, query, pageable, sortColumns);
	}

	public List<Record> matningstyperList(MatningstypSearchFilter filter) {
		var query = matningstyper(filter);

		return create.fetch(query);
	}

	public Stream<Record> matningstyperStream(MatningstypSearchFilter filter) {
		var query = matningstyper(filter).orderBy(MATOBJEKT.NAMN);
		query.attach(create.configuration());
		return create.fetchStream(query);
	}

	public Page<Record> matningstyperPaged(MatningstypSearchFilter filter, Pageable pageable) {
		var query = matningstyper(filter);
		return paged(create, query, pageable, sortColumns);
	}

	private SelectConditionStep<Record> matningstyper(MatningstypSearchFilter filter) {
		return select(MATNINGSTYP.asterisk(), MATOBJEKT.asterisk(), MATOBJEKT_TYP.NAMN, DEFINITION_MATNINGSTYP.NAMN, DEFINITION_MATNINGSTYP.STORHET)
					.from(MATNINGSTYP)
					.join(MATOBJEKT).on(MATOBJEKT.ID.eq(MATNINGSTYP.MATOBJEKT_ID))
					.join(MATOBJEKT_TYP).on(MATOBJEKT_TYP.ID.eq(MATOBJEKT.TYP))
					.join(DEFINITION_MATNINGSTYP).on(DEFINITION_MATNINGSTYP.ID.eq(MATNINGSTYP.DEFINITION_MATNINGSTYP_ID))
					.where(matningstypSearchFilter(filter));
	}

	public static Condition matningstypSearchFilter(MatningstypSearchFilter filter) {
		Condition cond = noCondition();
		if (filter.getIncludeIds() != null) {
			cond = cond.and(MATNINGSTYP.ID.in(filter.getIncludeIds()));
		}
		if (filter.getExcludeIds() != null) {
			cond = cond.and(MATNINGSTYP.ID.in(filter.getExcludeIds()).not());
		}
		if (filter.getMatobjektIds() != null) {
			if (filter.getMatobjektIds().isEmpty()) {
				cond = cond.and(DSL.falseCondition());
			} else {
				cond = cond.and(MATOBJEKT.ID.in(filter.getMatobjektIds()));
			}
		}
		if (filter.getMatobjektNamn() != null) {
			cond = cond.and(lower(MATOBJEKT.NAMN).startsWith(lower(filter.getMatobjektNamn())));
		}
		if (filter.getMatobjektTyp() != null) {
			cond = cond.and(MATOBJEKT.TYP.eq(filter.getMatobjektTyp()));
		}
		if (filter.getMatobjektgrupper() != null) {
			for (Integer id : filter.getMatobjektgrupper()) {
				cond = cond.andExists(selectFrom(GRUPP_MATOBJEKT)
						.where(GRUPP_MATOBJEKT.GRUPP_ID.eq(id))
						.and(GRUPP_MATOBJEKT.MATOBJEKT_ID.eq(MATOBJEKT.ID)));
			}
		}
		if (filter.getFastighet() != null) {
			cond = cond.and(lower(MATOBJEKT.FASTIGHET).startsWith(lower(filter.getFastighet())));
		}
		if (filter.getMatansvarigAnvandargruppIds() != null) {
			cond = cond.and(MATNINGSTYP.MATANSVARIG_ANVANDARGRUPP_ID.in(filter.getMatansvarigAnvandargruppIds()));
		}
		if (filter.getOnlyAktiva() != null && filter.getOnlyAktiva()) {
			cond = cond.and(MATOBJEKT.AKTIV.isTrue()).and(MATNINGSTYP.AKTIV.isTrue());
		}
		if (filter.getOnlyAktivaMatobjekt() != null && filter.getOnlyAktivaMatobjekt()) {
			cond = cond.and(MATOBJEKT.AKTIV.isTrue());
		}
		if (filter.getMatningStatus() != null) {
			cond = cond.andExists(selectFrom(MATNING)
					.where(MATNING.STATUS.eq(filter.getMatningStatus()))
					.and(MATNING.MATNINGSTYP_ID.eq(MATNINGSTYP.ID)));
		}
		if (filter.getMatrunda() != null) {
			cond = cond.andExists(selectFrom(MATRUNDA_MATNINGSTYP)
					.where(MATRUNDA_MATNINGSTYP.MATRUNDA_ID.eq(filter.getMatrunda()))
					.and(MATRUNDA_MATNINGSTYP.MATNINGSTYP_ID.eq(MATNINGSTYP.ID)));
		}
		if (filter.getMatningFromDatum() != null) {
			cond = cond.and(MATNING.AVLAST_DATUM.greaterOrEqual(filter.getMatningFromDatum()));
		}
		if (filter.getMatningToDatum() != null) {
			cond = cond.and(MATNING.AVLAST_DATUM.lessOrEqual(filter.getMatningToDatum()));
		}
		if (filter.getExcludeAutomatiska() != null && filter.getExcludeAutomatiska()) {
			Condition automatisk = DEFINITION_MATNINGSTYP.AUTOMATISK_INRAPPORTERING.isTrue();
			Condition giltigtInstrument = MATNINGSTYP.INSTRUMENT.length().greaterThan(0);
			cond = cond.andNot(automatisk.and(giltigtInstrument));
		}
		return cond;
	}
}
