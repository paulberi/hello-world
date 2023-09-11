package se.metria.matdatabas.common;

import org.jooq.*;
import org.springframework.data.domain.*;

import java.util.Map;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;

public class JooqQueryUtils {

	/**
	 * Executes a jooq query with paging, utilizing the Spring page classes.
	 */
	public static <T extends Record> Page<T> paged(DSLContext create, SelectOrderByStep<T> query, Pageable pageable, Map<String, Field<?>> columnMappings) {
		int count = create.fetchCount(query);

		// The query we will execute, possibly amended with an orderBy clause.
		SelectLimitStep<T> pagingQuery = query;

		if (!pageable.getSort().isEmpty()) {
			Sort.Order order = pageable.getSort().iterator().next();
			var field = getSortField(order, columnMappings);
			pagingQuery = query.orderBy(field);
		}

		Result<T> result = create.fetch(pagingQuery
				.limit(pageable.getPageSize())
				.offset(pageable.getPageNumber() * pageable.getPageSize()));

		return new PageImpl<>(result, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()), count);
	}


	private static OrderField<?> getSortField(Sort.Order order, Map<String, Field<?>> columnMappings) {
		Field<?> orderField = columnMappings.getOrDefault(order.getProperty(), field(name(order.getProperty())));
		return order.isDescending() ? orderField.desc() : orderField.asc();
	}
}
