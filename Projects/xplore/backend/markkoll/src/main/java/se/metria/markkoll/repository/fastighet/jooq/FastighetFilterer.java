package se.metria.markkoll.repository.fastighet.jooq;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.SelectConditionStep;
import org.jooq.Table;
import se.metria.markkoll.openapi.model.*;
import se.metria.markkoll.util.CollectionUtil;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.jooq.impl.DSL.*;
import static se.metria.markkoll.db.Tables.*;
import static se.metria.markkoll.db.tables.AvtalGeometristatus.AVTAL_GEOMETRISTATUS;
import static se.metria.markkoll.db.tables.Fastighet.FASTIGHET;
import static se.metria.markkoll.db.tables.IntrangFastighetInfo.INTRANG_FASTIGHET_INFO;

@RequiredArgsConstructor
public class FastighetFilterer {
    @NonNull
    private final DSLContext create;

    public SelectConditionStep<?>
    fastigheterRecordsFiltered(UUID projektId, FastighetsfilterDto fastighetsfilter)
    {
        var lengthSum = getLengthSum();

        var versionId = create
            .select(CURRENT_VERSION.VERSION_ID)
            .from(CURRENT_VERSION)
            .where(CURRENT_VERSION.PROJEKT_ID.eq(projektId))
            .fetchAny(CURRENT_VERSION.VERSION_ID, UUID.class);

        var fastigheterIds = create
            .select(FASTIGHETSFORTECKNING.FASTIGHET_ID)
            .from(FASTIGHETSFORTECKNING)
            .leftJoin(AVTAL_GEOMETRISTATUS)
            .on(FASTIGHETSFORTECKNING.AVTAL_ID.eq(AVTAL_GEOMETRISTATUS.AVTAL_ID))
            .and(AVTAL_GEOMETRISTATUS.VERSION_ID.eq(versionId));

        var records = create
            .select(FASTIGHET.ID,
                FASTIGHET.FASTIGHETSBETECKNING,
                FASTIGHET.DETALJTYP,
                AVTAL.ANTECKNING,
                AVTAL.AVTALSSTATUS,
                AVTAL_GEOMETRISTATUS.GEOMETRISTATUS,
                AVTAL.SKOGSFASTIGHET,
                FASTIGHETSFORTECKNING.ANLEDNING)
            .from(FASTIGHET)
            .join(AVTAL)
            .on(AVTAL.FASTIGHET_ID.eq(FASTIGHET.ID))
            .and(AVTAL.PROJEKT_ID.eq(projektId))
            .leftJoin(AVTAL_GEOMETRISTATUS)
            .on(AVTAL_GEOMETRISTATUS.VERSION_ID.eq(versionId))
            .and(AVTAL_GEOMETRISTATUS.AVTAL_ID.eq(AVTAL.ID))
            .join(FASTIGHETSFORTECKNING)
            .on(FASTIGHETSFORTECKNING.FASTIGHET_ID.eq(FASTIGHET.ID))
            .and(FASTIGHETSFORTECKNING.AVTAL_ID.eq(AVTAL.ID))
            .and(FASTIGHETSFORTECKNING.EXCLUDED.eq(false))
            .leftJoin(lengthSum)
            .on(lengthSum.field("fastighet_id", UUID.class).eq(FASTIGHET.ID))
            .and(lengthSum.field("projekt_id", UUID.class).eq(projektId))
            .where(filterQueries(fastighetsfilter, projektId))
            .and(FASTIGHET.ID.in(fastigheterIds));

        return records;
    }

    private Table<?> getLengthSum() {
        return create
            .select(PROJEKT.ID.as("projekt_id"),
                FASTIGHET.ID.as("fastighet_id"),
                coalesce(sum(INTRANG_FASTIGHET_INFO.LENGTH), 0.).as("length_sum"))
            .from(PROJEKT)
            .join(AVTAL)
            .on(AVTAL.PROJEKT_ID.eq(PROJEKT.ID))
            .join(FASTIGHET)
            .on(FASTIGHET.ID.eq(AVTAL.FASTIGHET_ID))
            .join(FASTIGHETSINTRANG)
            .on(FASTIGHETSINTRANG.FASTIGHET_ID.eq(FASTIGHET.ID))
            .leftJoin(INTRANG_FASTIGHET_INFO)
            .on(INTRANG_FASTIGHET_INFO.FASTIGHET_ID.eq(FASTIGHET.ID))
            .and(INTRANG_FASTIGHET_INFO.PROJEKT_ID.eq(PROJEKT.ID))
            .groupBy(PROJEKT.ID, FASTIGHET.ID)
            .asTable();
    }

    private Condition filterQueries(FastighetsfilterDto fastighetsfilter, UUID projektId) {
        var lengthSum = getLengthSum();

        var cond = noCondition();

        if (!CollectionUtil.isNullOrEmpty(fastighetsfilter.getFastighetsbeteckning())) {
            cond = cond.and(lower(FASTIGHET.FASTIGHETSBETECKNING).contains(lower(fastighetsfilter.getFastighetsbeteckning())));
        }

        if(fastighetsfilter.getIntrangLength() != null) {
            cond = cond.and(lengthSum.field("length_sum", Double.class).lessOrEqual(fastighetsfilter.getIntrangLength()));
        }

        if(fastighetsfilter.getExcludeWithMarkagare()) {
            var fastighetIdsWithMarkagare = create
                .select(FASTIGHET.ID)
                .from(FASTIGHET)
                .innerJoin(MARKAGARE)
                .on(FASTIGHET.ID.eq(MARKAGARE.FASTIGHET_ID))
                .and(MARKAGARE.AGARTYP.notEqual(AgartypDto.OMBUD.getValue()))
                .innerJoin(AVTALSPART)
                .on(AVTALSPART.MARKAGARE_ID.eq(MARKAGARE.ID))
                .innerJoin(AVTAL)
                .on(AVTAL.PROJEKT_ID.eq(projektId).and(AVTAL.ID.eq(AVTALSPART.AVTAL_ID)));

            cond = cond.and(FASTIGHET.ID.notIn(fastighetIdsWithMarkagare));
        }

        if (fastighetsfilter.getShowSenastUppdaterade() != null && fastighetsfilter.getShowSenastUppdaterade()) {
            cond = cond
                .and(AVTAL_GEOMETRISTATUS.GEOMETRISTATUS.in(
                    GeometristatusDto.BORTTAGEN.getValue(),
                    GeometristatusDto.NY.getValue(),
                    GeometristatusDto.UPPDATERAD.getValue()));
        }

        if (fastighetsfilter.getAvtalsstatus() == AvtalsstatusDto.AVTAL_SKICKAT) {
            var avtalsstatusIds = create
                .select(AVTAL.FASTIGHET_ID)
                .from(AVTAL)
                .join(AVTALSPART)
                .on(AVTAL.ID.eq(AVTALSPART.AVTAL_ID))
                .where(AVTALSPART.AVTALSSTATUS.eq(fastighetsfilter.getAvtalsstatus().toString()))
                .or(AVTALSPART.AVTALSSTATUS.eq(AvtalsstatusDto.PAMINNELSE_SKICKAD.toString()))
                .and(AVTAL.PROJEKT_ID.eq(projektId));

            cond = cond.and(AVTAL.FASTIGHET_ID.in(avtalsstatusIds));
        } else if (fastighetsfilter.getAvtalsstatus() != null) {
            var avtalsstatusIds = create
                .select(AVTAL.FASTIGHET_ID)
                .from(AVTAL)
                .join(AVTALSPART)
                .on(AVTAL.ID.eq(AVTALSPART.AVTAL_ID))
                .where(AVTALSPART.AVTALSSTATUS.eq(fastighetsfilter.getAvtalsstatus().toString()))
                .and(AVTAL.PROJEKT_ID.eq(projektId));

            cond = cond.and(AVTAL.FASTIGHET_ID.in(avtalsstatusIds));
        }

        if (fastighetsfilter.getExcludeOutreddaFastigheter() != null && fastighetsfilter.getExcludeOutreddaFastigheter()) {
            cond = cond.and(FASTIGHET.DETALJTYP.notIn(DetaljtypDto.FASTO.getValue(), DetaljtypDto.SAMFO.getValue()));
        }

        // Filtrera försenade avtal, dvs. om senaste logghändelsen har avstalsstatus satt till
        // EJ_BEHANDLAT, AVTAL_SKICKAT eller AVTAL_JUSTERAS och skapat datum är längre tillbaka än en vecka
        if (fastighetsfilter.getShowForsenade() != null && fastighetsfilter.getShowForsenade()) {

            var latestLogAvtalsstatus =
                create.select(
                    max(LOG_AVTALSSTATUS.SKAPAD_DATUM).as("maxDatum"),
                    AVTALSPART.ID.as("avtalspartId"))
                    .from(LOG_AVTALSSTATUS)
                    .join(AVTALSPART).on(LOG_AVTALSSTATUS.AVTALSPART_ID.eq(AVTALSPART.ID))
                    .groupBy(AVTALSPART.ID);

            var forsenadeAvtal = create.select(AVTAL.FASTIGHET_ID)
                .from(AVTAL)
                .join(AVTALSPART).on(AVTAL.ID.eq(AVTALSPART.AVTAL_ID))
                .join(latestLogAvtalsstatus).on(latestLogAvtalsstatus.field("avtalspartId", AVTALSPART.ID.getDataType()).eq(AVTALSPART.ID))
                .join(LOG_AVTALSSTATUS).on(LOG_AVTALSSTATUS.SKAPAD_DATUM.eq(latestLogAvtalsstatus.field("maxDatum", LOG_AVTALSSTATUS.SKAPAD_DATUM.getDataType())).
                    and(LOG_AVTALSSTATUS.AVTALSPART_ID.eq(latestLogAvtalsstatus.field("avtalspartId", AVTALSPART.ID.getDataType()))))
                .where(LOG_AVTALSSTATUS.SKAPAD_DATUM.lessOrEqual(LocalDateTime.now().minusWeeks(1)))
                .and(LOG_AVTALSSTATUS.AVTALSSTATUS.in(
                    AvtalsstatusDto.EJ_BEHANDLAT.getValue(),
                    AvtalsstatusDto.AVTAL_JUSTERAS.getValue(),
                    AvtalsstatusDto.AVTAL_SKICKAT.getValue()));
            cond = cond.and(FASTIGHET.ID.in(forsenadeAvtal));
        }

        if (fastighetsfilter.getSkogsfastighet()) {
            cond = cond.and(AVTAL.SKOGSFASTIGHET.eq(true));
        }

        if (fastighetsfilter.getLuftledningar()) {
            var intrangWithLuftledningar = create
                .select(FASTIGHETSINTRANG.FASTIGHET_ID)
                .from(FASTIGHETSINTRANG)
                .where(FASTIGHETSINTRANG.SUBTYPE.eq(IntrangsSubtypDto.LUFTLEDNING.getValue()));
            cond = cond.and(FASTIGHET.ID.in(intrangWithLuftledningar));
        }

        return cond;
    }
}
