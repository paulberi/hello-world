package se.metria.matdatabas.service.matobjekt;

import org.jooq.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import se.metria.matdatabas.db.tables.Matningstyp;
import se.metria.matdatabas.db.tables.Matrunda;
import se.metria.matdatabas.db.tables.MatrundaMatningstyp;
import se.metria.matdatabas.db.tables.records.MatobjektRecord;
import se.metria.matdatabas.db.tables.records.MatrundaMatningstypRecord;
import se.metria.matdatabas.db.tables.records.MatrundaRecord;
import se.metria.matdatabas.service.matningstyp.MatningstypJooqRepository;
import se.metria.matdatabas.service.matningstyp.query.MatningstypSearchFilter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.jooq.impl.DSL.max;
import static se.metria.matdatabas.common.JooqQueryUtils.paged;
import static se.metria.matdatabas.db.tables.Grupp.GRUPP;
import static se.metria.matdatabas.db.tables.GruppMatobjekt.GRUPP_MATOBJEKT;
import static se.metria.matdatabas.db.tables.Matningstyp.MATNINGSTYP;
import static se.metria.matdatabas.db.tables.Matobjekt.MATOBJEKT;
import static se.metria.matdatabas.db.tables.MatobjektTyp.MATOBJEKT_TYP;
import static se.metria.matdatabas.db.tables.MatrundaMatningstyp.MATRUNDA_MATNINGSTYP;
import static se.metria.matdatabas.db.tables.DefinitionMatningstyp.DEFINITION_MATNINGSTYP;

@Component
public class MatobjektJooqRepository {
    private DSLContext create;
    private Map<String, Field<?>> sortColumns = new HashMap<>();

    public MatobjektJooqRepository(DSLContext create) {
        this.create = create;
        sortColumns.put("typ", MATOBJEKT.TYP);
        sortColumns.put("namn", MATOBJEKT.NAMN);
        sortColumns.put("fastighet", MATOBJEKT.FASTIGHET);
        sortColumns.put("lage", MATOBJEKT.LAGE);
        sortColumns.put("aktiv", MATOBJEKT.AKTIV);
    }

    public Result<Record> matobjektMapinfo(MatningstypSearchFilter filter) {
        var kartsymbolSubselect = create.select(GRUPP_MATOBJEKT.MATOBJEKT_ID.as("matobjekt_id"), max(GRUPP.KARTSYMBOL).as("kartsymbol"))
                .from(GRUPP)
                .leftJoin(GRUPP_MATOBJEKT).on(GRUPP_MATOBJEKT.GRUPP_ID.eq(GRUPP.ID))
                .groupBy(GRUPP_MATOBJEKT.MATOBJEKT_ID)
                .asTable("kartsymbol");

        SelectHavingStep<Record> select = create.select(MATOBJEKT.asterisk(), MATOBJEKT_TYP.NAMN.as("typNamn"),
                        kartsymbolSubselect.field("kartsymbol", GRUPP.KARTSYMBOL.getDataType()),
                        MATNINGSTYP.ID)
                .from(MATOBJEKT)
                .leftJoin(MATNINGSTYP).on(MATOBJEKT.ID.eq(MATNINGSTYP.MATOBJEKT_ID))
                .leftJoin(kartsymbolSubselect).on(MATOBJEKT.ID.eq(kartsymbolSubselect.field("matobjekt_id", GRUPP_MATOBJEKT.MATOBJEKT_ID.getDataType())))
                .leftJoin(MATOBJEKT_TYP).on(MATOBJEKT_TYP.ID.eq(MATOBJEKT.TYP))
                .leftJoin(DEFINITION_MATNINGSTYP).on(DEFINITION_MATNINGSTYP.ID.eq(MATNINGSTYP.DEFINITION_MATNINGSTYP_ID))
                .where(MatningstypJooqRepository.matningstypSearchFilter(filter));

        return select.fetch();
    }

    public Page<MatobjektRecord> matobjektPage(MatningstypSearchFilter filter, Pageable pageable) {
        var query = create.selectDistinct(MATOBJEKT.asterisk())
                .from(MATOBJEKT)
                .leftJoin(MATNINGSTYP).on(MATOBJEKT.ID.eq(MATNINGSTYP.MATOBJEKT_ID))
                .leftJoin(DEFINITION_MATNINGSTYP).on(DEFINITION_MATNINGSTYP.ID.eq(MATNINGSTYP.DEFINITION_MATNINGSTYP_ID))
                .where(MatningstypJooqRepository.matningstypSearchFilter(filter));

        return paged(create, query, pageable, sortColumns).map(r -> r.into(MATOBJEKT));
    }

    public List<MatrundaMatningstypRecord> matrundorForMatningstyper(List<Integer> matningstyper) {
        var query = create.select()
                .from(MATRUNDA_MATNINGSTYP)
                .where(MATRUNDA_MATNINGSTYP.MATNINGSTYP_ID.in(matningstyper));

        return query.fetch().map(r -> r.into(MATRUNDA_MATNINGSTYP));
    }
}
