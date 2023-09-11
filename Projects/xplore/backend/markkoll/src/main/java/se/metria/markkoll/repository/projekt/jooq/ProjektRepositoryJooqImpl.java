package se.metria.markkoll.repository.projekt.jooq;

import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import se.metria.markkoll.db.tables.records.ProjektRecord;
import se.metria.markkoll.entity.projekt.ProjektEntity;
import se.metria.markkoll.openapi.model.ProjektTypDto;
import se.metria.markkoll.util.JooqUtil;

import java.util.*;

import static org.jooq.impl.DSL.lower;
import static org.jooq.impl.DSL.noCondition;
import static se.metria.markkoll.db.tables.Projekt.PROJEKT;

@RequiredArgsConstructor
public class ProjektRepositoryJooqImpl implements ProjektRepositoryJooq {
    // TODO: Är det något fel i konfigurationen, eller gnäller IntelliJ i onödan? Fältet fungerar ju bra
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    private DSLContext create;

    private static final Map<String, Field<?>> fields;
    static {
        fields = new HashMap<>();
        fields.put("namn", PROJEKT.NAMN);
        fields.put("skapadDatum", PROJEKT.SKAPAD_DATUM);
    }

    @Override
    public Page<ProjektEntity>
    pageFiltered(Pageable pageable, String searchFilter, Collection<UUID> projektFilter) {

        var condition = noCondition();

        // Inga kunder, inga projektbehörigheter. No soup for you.
        if (projektFilter != null && projektFilter.isEmpty()) {
            return new PageImpl<>(new ArrayList<>(), PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()), 0);
        }
        else if (projektFilter != null) {
            condition = condition.and(projektFilterCondition(projektFilter));
        }
        if (searchFilter != null) {
            condition = condition.and(lower(PROJEKT.NAMN).startsWith(lower(searchFilter)));
        }

        var records = create.selectFrom(PROJEKT).where(condition);

        return JooqUtil.paged(create, records, pageable, fields).map(this::fromRecord);
    }

    private Condition projektFilterCondition(Collection<UUID> projektFilter) {
        if (projektFilter.size() == 0) {
            return noCondition();
        }
        else {
            return PROJEKT.ID.in(projektFilter);
        }
    }

    private ProjektEntity fromRecord(ProjektRecord record) {
        var entity =  ProjektEntity.builder()
                .id(record.getId())
                .projekttyp(ProjektTypDto.fromValue(record.getProjekttyp()))
                .ort(record.getOrt())
                .namn(record.getNamn())
                .beskrivning(record.getBeskrivning())
                .kundId(record.getKundId())
                .build();

        entity.setSkapadDatum(record.getSkapadDatum());

        return entity;
    }
}
