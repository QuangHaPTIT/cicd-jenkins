package com.kaopiz.QLNCC.utils;

import com.kaopiz.QLNCC.model.request.SearchRequest;
import org.springframework.data.jpa.domain.Specification;

public final class SpecificationSearchUtils {

    private SpecificationSearchUtils() {}

    public static boolean hasJoinFilters(SearchRequest request) {
        return request != null
                && request.getJoinFilters() != null
                && !request.getJoinFilters().isEmpty();
    }

    public static <T> Specification<T> distinctRoot() {
        return (root, query, cb) -> {
            query.distinct(true);
            return cb.conjunction();
        };
    }
}
