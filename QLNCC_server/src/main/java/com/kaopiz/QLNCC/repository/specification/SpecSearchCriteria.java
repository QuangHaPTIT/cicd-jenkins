package com.kaopiz.QLNCC.repository.specification;

import lombok.Getter;
import lombok.Setter;

import static com.kaopiz.QLNCC.repository.specification.SearchOperation.OR_PREDICATE_FLAG;

@Getter
@Setter
public class SpecSearchCriteria {
    private String          key;
    private SearchOperation operation;
    private Object          value;
    private Boolean         orPredicate; // true = OR, false/null = AND

    public SpecSearchCriteria(String key, SearchOperation operation, Object value) {
        this.key       = key;
        this.operation = operation;
        this.value     = value;
    }

    public SpecSearchCriteria(String orPredicate, String key,
                              SearchOperation operation, Object value) {
        this.orPredicate = orPredicate != null
                && orPredicate.equals(OR_PREDICATE_FLAG);
        this.key         = key;
        this.operation   = operation;
        this.value       = value;
    }
}