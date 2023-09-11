package se.metria.matdatabas.service.matrunda;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Component;
import se.metria.matdatabas.db.tables.Matningstyp;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;

import static se.metria.matdatabas.db.tables.Matning.MATNING;
import static se.metria.matdatabas.db.tables.MatrundaMatningstyp.MATRUNDA_MATNINGSTYP;

@Component
public class MatrundaJooqRepository {
    private DSLContext create;

    public MatrundaJooqRepository(DSLContext create) {
        this.create = create;
    }

    public List<Record> senasteMatningarForMatrunda(Integer matrundaId) {
        var senasteMatningar = create.select(MATNING.MATNINGSTYP_ID, DSL.max(MATNING.AVLAST_DATUM).as("avlast_datum"))
                .from(MATNING)
                .groupBy(MATNING.MATNINGSTYP_ID)
                .asTable();

        var senasteMatningarJoin = create.select(MATNING.asterisk())
                .from(senasteMatningar)
                .innerJoin(MATNING)
                .on(MATNING.MATNINGSTYP_ID.eq((org.jooq.Field<Integer>) senasteMatningar.field("matningstyp_id")))
                .and(MATNING.AVLAST_DATUM.eq((org.jooq.Field<LocalDateTime>) senasteMatningar.field("avlast_datum")))
                .asTable();

        return create.select(senasteMatningarJoin.asterisk())
                .from(senasteMatningarJoin)
                .innerJoin(MATRUNDA_MATNINGSTYP)
                .on(MATRUNDA_MATNINGSTYP.MATNINGSTYP_ID.eq((org.jooq.Field<Integer>) senasteMatningarJoin.field("matningstyp_id")))
                .where(MATRUNDA_MATNINGSTYP.MATRUNDA_ID.eq(matrundaId))
                .fetch();
    }
}

