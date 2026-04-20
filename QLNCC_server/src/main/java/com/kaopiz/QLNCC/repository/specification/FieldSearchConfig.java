package com.kaopiz.QLNCC.repository.specification;

import jakarta.persistence.criteria.JoinType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FieldSearchConfig {
    private final SearchOperation operation;
    private final boolean isJoin;
    private final JoinType joinType;

    public static FieldSearchConfig contains() {
        return new FieldSearchConfig(SearchOperation.CONTAINS, false, JoinType.LEFT);
    }

    public static FieldSearchConfig equality() {
        return new FieldSearchConfig(SearchOperation.EQUALITY, false, JoinType.LEFT);
    }

    public static FieldSearchConfig greaterThan() {
        return new FieldSearchConfig(SearchOperation.GREATER_THAN, false, JoinType.LEFT);
    }

    public static FieldSearchConfig lessThan() {
        return new FieldSearchConfig(SearchOperation.LESS_THAN, false, JoinType.LEFT);
    }

    public static FieldSearchConfig join() {
        return new FieldSearchConfig(SearchOperation.CONTAINS, true, JoinType.LEFT);
    }

    public static FieldSearchConfig join(SearchOperation operation, JoinType joinType) {
        return new FieldSearchConfig(operation, true, joinType);
    }
}
