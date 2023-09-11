package se.metria.markkoll.repository.logging.projektlogg.jooq;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import se.metria.markkoll.db.tables.records.ProjektloggRecord;
import se.metria.markkoll.entity.logging.projektlogg.*;
import se.metria.markkoll.openapi.model.ProjektLoggFilterDto;
import se.metria.markkoll.openapi.model.ProjektLoggTypeDto;
import se.metria.markkoll.repository.logging.projektlogg.ProjektLoggRepository;
import se.metria.markkoll.util.JooqUtil;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.jooq.impl.DSL.noCondition;
import static se.metria.markkoll.db.Tables.*;

@RequiredArgsConstructor
public class ProjektLoggRepositoryJooqImpl implements ProjektLoggRepositoryJooq {
    private static final Map<String, Field<?>> fields;
    static {
        fields = new HashMap<>();
        fields.put("datum", PROJEKTLOGG.SKAPAD_DATUM);
    }

    @NonNull
    DSLContext create;

    @Autowired
    @Lazy
    ProjektLoggRepository projektLoggRepository;

    @NonNull
    AuditorAware<String> auditorAware;

    @Override
    public Page<ProjektLoggEntity>
    getProjektLoggPage(UUID projektId, Pageable pageable, List<ProjektLoggFilterDto> filter) {

        var condition = PROJEKTLOGG.PROJEKT_ID.eq(projektId).and(allFilterConditions(filter));

        var ids = create
              .select(PROJEKTLOGG.ID)
                .from(PROJEKTLOGG)
            .leftJoin(PROJEKTLOGG_PROJEKTHANDELSE)
                  .on(PROJEKTLOGG.ID.eq(PROJEKTLOGG_PROJEKTHANDELSE.ID))
            .leftJoin(PROJEKTLOGG_AVTALHANDELSE)
                  .on(PROJEKTLOGG.ID.eq(PROJEKTLOGG_AVTALHANDELSE.ID))
            .leftJoin(PROJEKTLOGG_INFOBREVHANDELSE)
                  .on(PROJEKTLOGG.ID.eq(PROJEKTLOGG_INFOBREVHANDELSE.ID))
            .leftJoin(PROJEKTLOGG_MANUELL_FASTIGHETHANDELSE)
                  .on(PROJEKTLOGG.ID.eq(PROJEKTLOGG_MANUELL_FASTIGHETHANDELSE.ID))
               .where(condition);

        var records = create.selectFrom(PROJEKTLOGG).where(PROJEKTLOGG.ID.in(ids));

        return JooqUtil.paged(create, records, pageable, fields).map(this::fromRecord);
    }

    private Condition allFilterConditions(List<ProjektLoggFilterDto> filter) {
        if (filter == null) {
            return noCondition();
        }

        var condition = noCondition();
        var filterSet = filter.stream().collect(Collectors.toSet());

        if (filterSet.contains(ProjektLoggFilterDto.SKAPAT_AV_MIG)) {
            condition = condition.and(filterCondition(ProjektLoggFilterDto.SKAPAT_AV_MIG));
        }

        var filterSetHandelserConditions = filterSet.stream()
            .filter(f -> f != ProjektLoggFilterDto.SKAPAT_AV_MIG)
            .map(this::filterCondition)
            .reduce(noCondition(), (acc, cond) -> acc.or(cond));

        if (filterSetHandelserConditions != noCondition()) {
            condition = condition.and(filterSetHandelserConditions);
        }

        return condition;
    }

    private Condition filterCondition(ProjektLoggFilterDto filter) {
        switch (filter) {
            case SKAPAT_AV_MIG:
                return PROJEKTLOGG.SKAPAD_AV.eq(auditorAware.getCurrentAuditor().orElse(""));
            case OVRIGA_DOKUMENT:
                return PROJEKTLOGG_INFOBREVHANDELSE.ID.eq(PROJEKTLOGG.ID);
            case MARKUPPLATELSEAVTAL:
                return PROJEKTLOGG_AVTALHANDELSE.ID.eq(PROJEKTLOGG.ID);
            case PROJEKTHANDELSER:
                return PROJEKTLOGG_PROJEKTHANDELSE.ID.eq(PROJEKTLOGG.ID)
                    .or(PROJEKTLOGG_MANUELL_FASTIGHETHANDELSE.ID.eq(PROJEKTLOGG.ID));
            default:
                throw new IllegalArgumentException("Okänt projektloggsfilter: " + filter);
        }
    }

    private ProjektLoggEntity fromRecord(ProjektloggRecord record) {
        var id = record.getId();

        var entity = projektLoggRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        entity.setProjektLoggType(getProjektLoggtyp(entity));
        return entity;
    }

    private ProjektLoggTypeDto getProjektLoggtyp(ProjektLoggEntity entity) {
        if (entity instanceof ProjekthandelseEntity) {
            return ProjektLoggTypeDto.PROJEKTHANDELSE;
        }
        else if (entity instanceof AvtalhandelseEntity) {
            return ProjektLoggTypeDto.AVTALHANDELSE;
        }
        else if (entity instanceof InfobrevhandelseEntity) {
            return ProjektLoggTypeDto.INFOBREVHANDELSE;
        }
        else if (entity instanceof ManuellFastighethandelseEntity) {
            return ProjektLoggTypeDto.MANUELL_FASTIGHETHANDELSE;
        }
        else {
            throw new IllegalArgumentException("Hittar ingen giltig projektloggstyp för entitet med id " + entity.getId());
        }
    }
}
