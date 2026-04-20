package com.kaopiz.QLNCC.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import static com.kaopiz.QLNCC.repository.specification.SearchOperation.*;

public class GenericSpecificationBuilder<T> {
    private final List<SpecSearchCriteria> params = new ArrayList<>();

    public GenericSpecificationBuilder<T> with(String key, SearchOperation operation, Object value) {
        return with(null, key, operation, value);
    }

    public GenericSpecificationBuilder<T> with(String orPredicate, String key, SearchOperation operation, Object value) {
        if (operation == null) {
            return this;
        }

        params.add(new SpecSearchCriteria(orPredicate, key, operation, value));
        return this;
    }

    public GenericSpecificationBuilder<T> with(String key, String operation, Object value, String prefix, String suffix) {
        return with(null, key, operation, value, prefix, suffix);
    }

    public GenericSpecificationBuilder<T> with(String orPredicate, String key, String operation, Object value, String prefix, String suffix) {
        SearchOperation op = SearchOperation.getSimpleOperation(operation.charAt(0));
        if (op == null) return this;

        if (op == EQUALITY) {
            boolean startAsterisk = prefix != null && prefix.contains(ZERO_OR_MORE_REGEX);
            boolean endAsterisk   = suffix != null && suffix.contains(ZERO_OR_MORE_REGEX);
            if (startAsterisk && endAsterisk) op = CONTAINS;
            else if (startAsterisk)           op = ENDS_WITH;
            else if (endAsterisk)             op = STARTS_WITH;
        }

        params.add(new SpecSearchCriteria(orPredicate, key, op, value));
        return this;
    }

    public GenericSpecificationBuilder<T> with(SpecSearchCriteria criteria) {
        params.add(criteria);
        return this;
    }

    public GenericSpecificationBuilder<T> withIfPresent(String key,
                                                        String operation,
                                                        String value,
                                                        String prefix,
                                                        String suffix) {
        if (value != null && !value.isBlank()) {
            with(key, operation, value, prefix, suffix);
        }
        return this;
    }

    public GenericSpecificationBuilder<T> withIfPresent(String key, String operation, Object value) {
        if (value == null) {
            return this;
        }

        if (value instanceof String stringValue && stringValue.isBlank()) {
            return this;
        }

        return with(key, operation, value, null, null);
    }

    public GenericSpecificationBuilder<T> withIfPresent(String key,
                                                        SearchOperation operation,
                                                        String value) {
        if (value != null && !value.isBlank()) {
            with(key, operation, value);
        }
        return this;
    }

    public Specification<T> build() {
        if (CollectionUtils.isEmpty(params)) return null;

        Specification<T> result = new GenericSpecification<>(params.getFirst());
        for (int i = 1; i < params.size(); i++) {
            result = Boolean.TRUE.equals(params.get(i).getOrPredicate())
                    ? Specification.where(result).or(new GenericSpecification<>(params.get(i)))
                    : Specification.where(result).and(new GenericSpecification<>(params.get(i)));
        }
        return result;
    }
}