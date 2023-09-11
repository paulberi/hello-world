package se.metria.markkoll.repository.intrang;

import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.UUID;

import static se.metria.markkoll.db.Tables.*;

@Slf4j
public class OmradesintrangRepositoryCustomImpl implements OmradesintrangRepositoryCustom {
    @Autowired
    private DSLContext create;

    @Override
    public void saveAllOmradesintrang(Collection<UUID> fastighetIds, UUID versionId) {
        var intrang =
            create.select(FASTIGHET_OMRADE.FASTIGHET_ID,
                FASTIGHET_OMRADE.OMRADE_NR,
                INTRANG.TYPE,
                INTRANG.SUBTYPE,
                INTRANG.SPANNINGSNIVA,
                INTRANG.STATUS,
                INTRANG.VERSION_ID,
                DSL.field("st_intersection(fastighet_omrade.geom, intrang.geom)").as("geom"),
                INTRANG.LITTERA,
                INTRANG.AVTALSTYP
            )
                .from(INTRANG)
                .join(FASTIGHET_OMRADE)
                .on("st_intersects(fastighet_omrade.geom, intrang.geom)")
                .where(FASTIGHET_OMRADE.FASTIGHET_ID.in(fastighetIds))
                .and(INTRANG.VERSION_ID.eq(versionId));

        var numOfRecords = create.insertInto(OMRADESINTRANG,
            OMRADESINTRANG.FASTIGHET_ID,
            OMRADESINTRANG.OMRADE_NR,
            OMRADESINTRANG.TYPE,
            OMRADESINTRANG.SUBTYPE,
            OMRADESINTRANG.SPANNINGSNIVA,
            OMRADESINTRANG.STATUS,
            OMRADESINTRANG.VERSION_ID,
            OMRADESINTRANG.GEOM,
            OMRADESINTRANG.LITTERA,
            OMRADESINTRANG.AVTALSTYP
        )
            .select(intrang)
            .execute();

        log.info("Skapade {} områdesintrång för {} fastigheter i version {}", numOfRecords, fastighetIds.size(),
            versionId);
    }

    @Override
    public void saveAllOmradesintrang(UUID fastighetId, UUID versionId) {
        var intrang =
                create.select(FASTIGHET_OMRADE.FASTIGHET_ID,
                              FASTIGHET_OMRADE.OMRADE_NR,
                              INTRANG.TYPE,
                              INTRANG.SUBTYPE,
                              INTRANG.SPANNINGSNIVA,
                              INTRANG.STATUS,
                              INTRANG.VERSION_ID,
                              DSL.field("st_intersection(fastighet_omrade.geom, intrang.geom)").as("geom"),
                              INTRANG.LITTERA,
                              INTRANG.AVTALSTYP
                        )
                        .from(INTRANG)
                        .join(FASTIGHET_OMRADE)
                        .on("st_intersects(fastighet_omrade.geom, intrang.geom)")
                        .where(FASTIGHET_OMRADE.FASTIGHET_ID.eq(fastighetId))
                        .and(INTRANG.VERSION_ID.eq(versionId));

        var numOfRecords = create.insertInto(OMRADESINTRANG,
                          OMRADESINTRANG.FASTIGHET_ID,
                          OMRADESINTRANG.OMRADE_NR,
                          OMRADESINTRANG.TYPE,
                          OMRADESINTRANG.SUBTYPE,
                          OMRADESINTRANG.SPANNINGSNIVA,
                          OMRADESINTRANG.STATUS,
                          OMRADESINTRANG.VERSION_ID,
                          OMRADESINTRANG.GEOM,
                          OMRADESINTRANG.LITTERA,
                          OMRADESINTRANG.AVTALSTYP
                )
                .select(intrang)
                .execute();

        log.info("Skapade {} områdesintrång för fastighet {} i version {}", numOfRecords, fastighetId, versionId);
    }
}
