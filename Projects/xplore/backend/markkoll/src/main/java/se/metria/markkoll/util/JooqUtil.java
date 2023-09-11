package se.metria.markkoll.util;

import org.jooq.*;
import org.jooq.Record;
import org.springframework.data.domain.*;

import java.util.Map;

public class JooqUtil {
    public static <T extends Record> Page<T> paged(
            DSLContext create,
            SelectOrderByStep<T> query,
            Pageable pageable,
            Map<String, Field<?>> fields)
    {
        var count = create.fetchCount(query);

        SelectLimitStep<T> pagingQuery = query;
        if (!pageable.getSort().isEmpty()) {
            pagingQuery = query.orderBy(sortField(fields, pageable.getSort().iterator().next()));
        }

        var result = create.fetch(pagingQuery
                .limit(pageable.getPageSize())
                .offset(pageable.getPageNumber() * pageable.getPageSize()));

        return new PageImpl<>(result, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()), count);
    }

    private static SortField<?> sortField(Map<String, Field<?>> map, Sort.Order order) {
        var field = map.get(order.getProperty());
        if (field == null) {
            throw new IllegalArgumentException();
        }

        return order.getDirection() == Sort.Direction.ASC ? field.asc() : field.desc();
    }
}
