package se.metria.markkoll.repository.intrang;

import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.UUID;

import static se.metria.markkoll.db.Tables.*;

@Slf4j
public class FastighetsintrangRepositoryCustomImpl implements FastighetsintrangRepositoryCustom {
    @Autowired
    private DSLContext create;

    @Override
    public void saveAllFastighetsintrang(Collection<UUID> fastighetIds, UUID versionId) {
        var intrang =
            create.select(FASTIGHET_OMRADE.FASTIGHET_ID,
                INTRANG.TYPE,
                INTRANG.SUBTYPE,
                INTRANG.STATUS,
                INTRANG.VERSION_ID,
                DSL.field("st_intersection(fastighet_omrade.geom, st_union(intrang.geom))").as("geom"),
                INTRANG.LITTERA,
                INTRANG.AVTALSTYP)
                .from(INTRANG)
                .join(FASTIGHET_OMRADE)
                .on("st_intersects(fastighet_omrade.geom, intrang.geom)")
                .where(FASTIGHET_OMRADE.FASTIGHET_ID.in(fastighetIds))
                .and(INTRANG.VERSION_ID.eq(versionId))
                .groupBy(FASTIGHET_OMRADE.FASTIGHET_ID,
                    INTRANG.ID,
                    INTRANG.TYPE,
                    INTRANG.SUBTYPE,
                    INTRANG.SPANNINGSNIVA,
                    INTRANG.STATUS,
                    INTRANG.VERSION_ID,
                    FASTIGHET_OMRADE.GEOM,
                    INTRANG.LITTERA,
                    INTRANG.AVTALSTYP);

        var numOfRecords = create.insertInto(FASTIGHETSINTRANG,
            FASTIGHETSINTRANG.FASTIGHET_ID,
            FASTIGHETSINTRANG.TYPE,
            FASTIGHETSINTRANG.SUBTYPE,
            FASTIGHETSINTRANG.STATUS,
            FASTIGHETSINTRANG.VERSION_ID,
            FASTIGHETSINTRANG.GEOM,
            FASTIGHETSINTRANG.LITTERA,
            FASTIGHETSINTRANG.AVTALSTYP)
            .select(intrang)
            .execute();

        log.info("Skapade {} fastighetsintrång för {} fastigheter i version {}", numOfRecords, fastighetIds.size(),
            versionId);
    }

    @Override
    public void saveAllFastighetsintrang(UUID fastighetId, UUID versionId) {
        var intrang =
                create.select(FASTIGHET_OMRADE.FASTIGHET_ID,
                              INTRANG.TYPE,
                              INTRANG.SUBTYPE,
                              INTRANG.STATUS,
                              INTRANG.VERSION_ID,
                              DSL.field("st_intersection(fastighet_omrade.geom, st_union(intrang.geom))").as("geom"),
                              INTRANG.LITTERA,
                              INTRANG.AVTALSTYP)
                        .from(INTRANG)
                        .join(FASTIGHET_OMRADE)
                        .on("st_intersects(fastighet_omrade.geom, intrang.geom)")
                       .where(FASTIGHET_OMRADE.FASTIGHET_ID.eq(fastighetId))
                         .and(INTRANG.VERSION_ID.eq(versionId))
                     .groupBy(FASTIGHET_OMRADE.FASTIGHET_ID,
                              INTRANG.ID,
                              INTRANG.TYPE,
                              INTRANG.SUBTYPE,
                              INTRANG.SPANNINGSNIVA,
                              INTRANG.STATUS,
                              INTRANG.VERSION_ID,
                              FASTIGHET_OMRADE.GEOM,
                              INTRANG.LITTERA,
                              INTRANG.AVTALSTYP);

        var numOfRecords = create.insertInto(FASTIGHETSINTRANG,
                FASTIGHETSINTRANG.FASTIGHET_ID,
                FASTIGHETSINTRANG.TYPE,
                FASTIGHETSINTRANG.SUBTYPE,
                FASTIGHETSINTRANG.STATUS,
                FASTIGHETSINTRANG.VERSION_ID,
                FASTIGHETSINTRANG.GEOM,
                FASTIGHETSINTRANG.LITTERA,
                FASTIGHETSINTRANG.AVTALSTYP)
                .select(intrang)
                .execute();

        log.info("Skapade {} fastighetsintrång för fastighet {} i version {}", numOfRecords, fastighetId, versionId);
    }
}
