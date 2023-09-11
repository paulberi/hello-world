package se.metria.markkoll.repository.fastighet.jooq;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import se.metria.markkoll.entity.fastighet.FastighetEntity;
import se.metria.markkoll.entity.fastighetsforteckning.FastighetsforteckningAnledning;
import se.metria.markkoll.openapi.model.*;
import se.metria.markkoll.util.CollectionUtil;
import se.metria.markkoll.util.JooqUtil;

import java.util.*;
import java.util.stream.Collectors;

import static se.metria.markkoll.db.Tables.*;
import static se.metria.markkoll.db.tables.AvtalGeometristatus.AVTAL_GEOMETRISTATUS;
import static se.metria.markkoll.db.tables.Fastighet.FASTIGHET;
import static se.metria.markkoll.db.tables.Fastighetsstatus.FASTIGHETSSTATUS;
import static se.metria.markkoll.db.tables.IntrangFastighetInfo.INTRANG_FASTIGHET_INFO;

@RequiredArgsConstructor
public class FastighetRepositoryJooqImpl implements FastighetRepositoryJooq {
    private static Map<String, Field<?>> sortFields;
    static {
        sortFields = new HashMap<>();
        sortFields.put("fastighetsbeteckning", FASTIGHET.FASTIGHETSBETECKNING);
    }

    private DSLContext create;
    private FastighetFilterer fastighetFilterer;

    @Autowired
    public FastighetRepositoryJooqImpl(DSLContext create) {
        this.create = create;
        this.fastighetFilterer = new FastighetFilterer(create);
    }

    @Override
    public List<FastighetEntity>
    registerenheterFiltered(UUID projektId, FastighetsfilterDto fastighetsfilter) {
        var records = fastighetFilterer.fastigheterRecordsFiltered(projektId, fastighetsfilter);

        return records.stream()
            .map(this::entityFromRecord)
            .collect(Collectors.toList());
    }

    @Override
    public Integer
    registerenheterCountFiltered(UUID projektId, FastighetsfilterDto fastighetsfilter) {
        return Long.valueOf(registerenheterFiltered(projektId, fastighetsfilter).stream().count()).intValue();
    }

    @Override
    public Page<FastighetDto>
    fastighetPageFiltered(UUID projektId, Pageable pageable, FastighetsfilterDto fastighetsfilter) {
        var records = fastighetFilterer.fastigheterRecordsFiltered(projektId, fastighetsfilter);

        records = records.and(FASTIGHET.DETALJTYP.in(DetaljtypDto.FASTIGHET.toString(), DetaljtypDto.FASTO.toString()));

        return JooqUtil.paged(create, records, pageable, sortFields).map(this::fromRecord);
    }

    @Override
    public Page<FastighetDto>
    samfallighetPageFiltered(UUID projektId, Pageable pageable, FastighetsfilterDto fastighetsfilter) {
        var records = fastighetFilterer.fastigheterRecordsFiltered(projektId, fastighetsfilter);

        records = records.and(FASTIGHET.DETALJTYP.in(DetaljtypDto.SAMF.toString(), DetaljtypDto.SAMFO.toString()));

        return JooqUtil.paged(create, records, pageable, sortFields).map(this::fromRecord);
    }

    @Override
    public List<FastighetEntity>
    fastigheterFiltered(UUID projektId, FastighetsfilterDto fastighetsfilter) {
        var records = fastighetFilterer.fastigheterRecordsFiltered(projektId, fastighetsfilter);

        records = records.and(FASTIGHET.DETALJTYP.in(DetaljtypDto.FASTIGHET.toString(), DetaljtypDto.FASTO.toString()));

        return records.stream().map(this::entityFromRecord).collect(Collectors.toList());
    }

    @Override
    public List<FastighetEntity> fastigheterAndSamfalligheterFiltered(UUID projektId, FastighetsfilterDto fastighetsfilter) {
        var records = fastighetFilterer.fastigheterRecordsFiltered(projektId, fastighetsfilter);

        records = records.and(FASTIGHET.DETALJTYP.in(DetaljtypDto.FASTIGHET.toString(), DetaljtypDto.FASTO.toString(), DetaljtypDto.SAMF.toString(), DetaljtypDto.SAMFO.toString()));

        return records.stream().map(this::entityFromRecord).collect(Collectors.toList());
    }

    @Override
    public List<FastighetEntity>
    samfalligheterFiltered(UUID projektId, FastighetsfilterDto fastighetsfilter) {
        var records = fastighetFilterer.fastigheterRecordsFiltered(projektId, fastighetsfilter);

        records = records.and(FASTIGHET.DETALJTYP.in(DetaljtypDto.SAMF.toString(), DetaljtypDto.SAMFO.toString()));

        return records.stream().map(this::entityFromRecord).collect(Collectors.toList());
    }

    @Override
    public List<IntrangDto> findIntrangInfoForFastighet(UUID fastighetId, UUID projektId){
        List<IntrangDto> result = new ArrayList<>();
        var intrang = create
                .select()
                .from(INTRANG_FASTIGHET_INFO)
                .where(INTRANG_FASTIGHET_INFO.FASTIGHET_ID.eq(fastighetId))
                .and(INTRANG_FASTIGHET_INFO.PROJEKT_ID.eq(projektId));

        for(var i: intrang){
            result.add(new IntrangDto().typ(i.get("type").toString())
                            .subtyp(i.get("subtype").toString())
                            .langd((double)i.get("length")));
        }
        return result;
    }

    @Override
    public Integer getErsattningForFastighet(UUID fastighetId, UUID projektId){
        var ersattningRecord = create
                .select(AVTAL.ERSATTNING)
                .from(AVTAL)
                .where(AVTAL.FASTIGHET_ID.eq(fastighetId).and(AVTAL.PROJEKT_ID.eq(projektId)))
                .fetchOne();
        Integer ersattning = 0;
        if(ersattningRecord != null && ersattningRecord.size()>0){
            ersattning = ersattningRecord.get(AVTAL.ERSATTNING);
        }
        return ersattning;
    }

    @Override
    public Integer setFastighetsInfoForProjekt(UUID fastighetId, UUID projektId, FastighetsProjektInfoDto info){
        setErsattningForFastighet(fastighetId, projektId, info.getErsattning());
        setAnteckningForFastighet(fastighetId, projektId, info.getAnteckning());
        return 0;
    }

    public Integer setErsattningForFastighet(UUID fastighetId, UUID projektId, Integer ersattning){
        var existing = create.select().from(AVTAL)
                .where(AVTAL.FASTIGHET_ID.eq(fastighetId)
                        .and(AVTAL.PROJEKT_ID.eq(projektId))).fetchOne();
        if(existing!=null){
            var updated = create.update(AVTAL)
                    .set(AVTAL.ERSATTNING, ersattning)
                    .where(AVTAL.FASTIGHET_ID.eq(fastighetId)
                            .and(AVTAL.PROJEKT_ID.eq(projektId)))
                    .execute();
        } else {
            var saved = create.insertInto(AVTAL)
                    .columns(AVTAL.ERSATTNING, AVTAL.ANTECKNING, AVTAL.FASTIGHET_ID, AVTAL.PROJEKT_ID)
                    .values(ersattning, "", fastighetId, projektId)
                    .execute();
        }
        return ersattning;
    }

    @Override
    public String getAnteckningForFastighet(UUID fastighetId, UUID projektId){
        var anteckningRecord = create.select(AVTAL.ANTECKNING).from(AVTAL)
                .where(AVTAL.FASTIGHET_ID.eq(fastighetId).and(AVTAL.PROJEKT_ID.eq(projektId)))
                .fetchOne();
        String antecking = "";
        if(anteckningRecord != null && anteckningRecord.size()>0){
            antecking = anteckningRecord.get(AVTAL.ANTECKNING);
        }
        return antecking;
    }

    public String setAnteckningForFastighet(UUID fastighetId, UUID projektId, String anteckning){
        var existing = create.select().from(AVTAL)
                .where(AVTAL.FASTIGHET_ID.eq(fastighetId)
                        .and(AVTAL.PROJEKT_ID.eq(projektId))).fetchOne();
        if(existing!=null){
            var updated = create.update(AVTAL)
                    .set(AVTAL.ANTECKNING, anteckning)
                    .where(AVTAL.FASTIGHET_ID.eq(fastighetId)
                            .and(AVTAL.PROJEKT_ID.eq(projektId)))
                    .execute();
        } else {
            var saved = create.insertInto(AVTAL)
                    .columns(AVTAL.ANTECKNING, AVTAL.ERSATTNING, AVTAL.FASTIGHET_ID, AVTAL.PROJEKT_ID)
                    .values(anteckning, 0, fastighetId, projektId)
                    .execute();
        }
        return anteckning;
    }

    @Override
    public AvtalsstatusDto fastighetAvtalsstatus(UUID fastighetId, UUID projektId) {
        var status = create
                .select(FASTIGHETSSTATUS.STATUS)
                .from(FASTIGHETSSTATUS)
                .where(FASTIGHETSSTATUS.FASTIGHET_ID.eq(fastighetId))
                .and(FASTIGHETSSTATUS.PROJEKT_ID.eq(projektId))
                .fetchAny(FASTIGHETSSTATUS.STATUS, String.class);

        return AvtalsstatusDto.valueOf(status);
    }

    private FastighetEntity entityFromRecord(org.jooq.Record record) {
        return FastighetEntity.builder()
                .fastighetsbeteckning(record.get(FASTIGHET.FASTIGHETSBETECKNING))
                .id(record.get(FASTIGHET.ID))
                .detaljtyp(DetaljtypDto.valueOf(record.get(FASTIGHET.DETALJTYP)))
                .build();
    }

    private FastighetDto fromRecord(org.jooq.Record record) {
        var manuelltTillagd = record.get(FASTIGHETSFORTECKNING.ANLEDNING).equals(
            FastighetsforteckningAnledning.MANUELLT_TILLAGD.toString());

        var geometristatus = record.get(AVTAL_GEOMETRISTATUS.GEOMETRISTATUS) == null ?
            GeometristatusDto.OFORANDRAD :
            GeometristatusDto.valueOf(record.get(AVTAL_GEOMETRISTATUS.GEOMETRISTATUS));

        var hasAnteckningar = !CollectionUtil.isNullOrEmpty(record.get(AVTAL.ANTECKNING));

        return new FastighetDto()
                .fastighetsbeteckning(record.get(FASTIGHET.FASTIGHETSBETECKNING))
                .id(record.get(FASTIGHET.ID))
                .detaljtyp(record.get(FASTIGHET.DETALJTYP))
                .geometristatus(geometristatus)
                .avtalsstatus(AvtalsstatusDto.fromValue(record.get(AVTAL.AVTALSSTATUS)))
                .manuelltTillagd(manuelltTillagd)
                .skogsfastighet(record.get(AVTAL.SKOGSFASTIGHET))
                .hasAnteckningar(hasAnteckningar);
    }

    public List<String> getIngaendeFastigheterBeteckningar(UUID samfallighetsId) {
        List<String> result = new ArrayList<>();

        var list = create.select(SAMFALLIGHET_FASTIGHET.FASTIGHETSBETECKNING)
                .from(SAMFALLIGHET_FASTIGHET)
                .where(SAMFALLIGHET_FASTIGHET.SAMFALLIGHET_ID.eq(samfallighetsId))
                .fetch();
        for(var item: list){
            result.add(item.value1());
        }
        return result;
    }
}
