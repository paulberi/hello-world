package se.metria.matdatabas.service.matning;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.stereotype.Component;
import se.metria.matdatabas.db.tables.Matning;
import se.metria.matdatabas.service.matning.query.MatningSearchFilter;
import se.metria.matdatabas.service.matningstyp.query.MatningstypSearchFilter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.jooq.impl.DSL.noCondition;
import static org.jooq.impl.DSL.trueCondition;
import static se.metria.matdatabas.db.tables.DefinitionMatningstyp.DEFINITION_MATNINGSTYP;
import static se.metria.matdatabas.db.tables.Matning.MATNING;
import static se.metria.matdatabas.db.tables.Matningstyp.MATNINGSTYP;
import static se.metria.matdatabas.db.tables.Matobjekt.MATOBJEKT;
import static se.metria.matdatabas.service.matningstyp.MatningstypJooqRepository.matningstypSearchFilter;

@Component
public class MatningJooqRepository {
	private DSLContext create;

	public MatningJooqRepository(DSLContext create) {
		this.create = create;
	}

	public List<Record> matningar(MatningstypSearchFilter matningstypSearchFilter, MatningSearchFilter matningSearchFilter) {
		return create.select(MATNING.asterisk(), MATOBJEKT.NAMN, MATNINGSTYP.ID, MATNINGSTYP.ENHET, DEFINITION_MATNINGSTYP.NAMN)
				.from(MATNING)
				.join(MATNINGSTYP).on(MATNINGSTYP.ID.eq(MATNING.MATNINGSTYP_ID))
				.join(MATOBJEKT).on(MATOBJEKT.ID.eq(MATNINGSTYP.MATOBJEKT_ID))
				.join(DEFINITION_MATNINGSTYP).on(DEFINITION_MATNINGSTYP.ID.eq(MATNINGSTYP.DEFINITION_MATNINGSTYP_ID))
				.where(matningstypSearchFilter(matningstypSearchFilter))
				.and(matningSearchFilter(matningSearchFilter))
				.orderBy(MATNING.AVLAST_DATUM)
				.fetch();
	}

	public Stream<Record> matningarStream(MatningstypSearchFilter filter, MatningSearchFilter matningSearchFilter) {
		return create.select(MATNING.asterisk(), MATOBJEKT.NAMN, MATNINGSTYP.ENHET, DEFINITION_MATNINGSTYP.STORHET, DEFINITION_MATNINGSTYP.BERAKNAD_ENHET, DEFINITION_MATNINGSTYP.NAMN)
				.from(MATNING)
				.join(MATNINGSTYP).on(MATNINGSTYP.ID.eq(MATNING.MATNINGSTYP_ID))
				.join(MATOBJEKT).on(MATOBJEKT.ID.eq(MATNINGSTYP.MATOBJEKT_ID))
				.join(DEFINITION_MATNINGSTYP).on(DEFINITION_MATNINGSTYP.ID.eq(MATNINGSTYP.DEFINITION_MATNINGSTYP_ID))
				.where(matningstypSearchFilter(filter))
				.and(matningSearchFilter(matningSearchFilter))
				.orderBy(MATOBJEKT.NAMN, DEFINITION_MATNINGSTYP.NAMN, MATNING.AVLAST_DATUM)
				.fetchStream();
	}

	public static Condition matningSearchFilter(MatningSearchFilter filter) {
		Condition cond = noCondition();

		if (filter.getFrom() != null) {
			cond = cond.and(MATNING.AVLAST_DATUM.ge(filter.getFrom()));
		}
		if (filter.getTo() != null) {
			cond = cond.and(MATNING.AVLAST_DATUM.le(filter.getTo()));
		}
		if (filter.getStatus() != null) {
			cond = cond.and(MATNING.STATUS.eq(filter.getStatus()));
		}
		if (filter.getFilterGranskade() != null && filter.getFilterGranskade()) {
			cond = cond.and(MATNING.STATUS.notEqual(Matningstatus.EJGRANSKAT));
		}
		if (filter.getFilterFelkodOk() != null && filter.getFilterFelkodOk()) {
			cond = cond.and(MATNING.FELKOD.equal(Matningsfelkod.OK));
		}
		return cond;
	}
}
