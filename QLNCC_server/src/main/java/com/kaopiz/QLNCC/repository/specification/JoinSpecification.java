package com.kaopiz.QLNCC.repository.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

import static com.kaopiz.QLNCC.utils.RegexPatternUtils.LIKE_ESCAPE_CHAR;
import static com.kaopiz.QLNCC.utils.RegexPatternUtils.escapeLikePattern;

public class JoinSpecification<T> implements Specification<T> {

    private final String joinField;
    private final String joinValue;
    private final SearchOperation operation;
    private final JoinType joinType;

    public JoinSpecification(String joinField, String joinValue) {
        this(joinField, joinValue, SearchOperation.CONTAINS, JoinType.LEFT);
    }

    public JoinSpecification(String joinField, String joinValue, SearchOperation operation) {
        this(joinField, joinValue, operation, JoinType.LEFT);
    }

    public JoinSpecification(String joinField,
                             String joinValue,
                             SearchOperation operation,
                             JoinType joinType) {
        this.joinField = joinField;
        this.joinValue = joinValue;
        this.operation = operation;
        this.joinType = joinType == null ? JoinType.LEFT : joinType;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (joinValue == null || joinValue.isBlank()) {
            return criteriaBuilder.conjunction();
        }

        Path<?> joinPath = resolveJoinPath(root);
        SearchOperation searchOperation = operation == null ? SearchOperation.CONTAINS : operation;
        String escapedLikeValue = escapeLikePattern(joinValue.toLowerCase(Locale.ROOT));

        return switch (searchOperation) {
            case EQUALITY -> buildEqualPredicate(criteriaBuilder, joinPath, joinValue);
            case NEGATION -> buildNotEqualPredicate(criteriaBuilder, joinPath, joinValue);
            case LIKE,
                 CONTAINS -> buildLikePredicate(criteriaBuilder, joinPath, "%" + escapedLikeValue + "%");
            case STARTS_WITH -> buildLikePredicate(criteriaBuilder, joinPath, escapedLikeValue + "%");
            case ENDS_WITH -> buildLikePredicate(criteriaBuilder, joinPath, "%" + escapedLikeValue);
            case GREATER_THAN -> buildCompare(criteriaBuilder, joinPath, joinValue, true);
            case LESS_THAN -> buildCompare(criteriaBuilder, joinPath, joinValue, false);
        };
    }

    private Predicate buildEqualPredicate(CriteriaBuilder builder, Path<?> path, String value) {
        if (String.class.equals(path.getJavaType())) {
            return builder.equal(builder.lower(path.as(String.class)), value.toLowerCase(Locale.ROOT));
        }
        return builder.equal(path, parseValue(path.getJavaType(), value));
    }

    private Predicate buildNotEqualPredicate(CriteriaBuilder builder, Path<?> path, String value) {
        if (String.class.equals(path.getJavaType())) {
            return builder.notEqual(builder.lower(path.as(String.class)), value.toLowerCase(Locale.ROOT));
        }
        return builder.notEqual(path, parseValue(path.getJavaType(), value));
    }

    private Predicate buildLikePredicate(CriteriaBuilder builder, Path<?> path, String pattern) {
        Expression<String> stringPath = path.as(String.class);
        return builder.like(builder.lower(stringPath), pattern, LIKE_ESCAPE_CHAR);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Predicate buildCompare(CriteriaBuilder builder, Path<?> path, String value, boolean isGreater) {
        Comparable parsedValue = parseValue(path.getJavaType(), value);
        Expression<Comparable> expression = (Expression<Comparable>) path;
        return isGreater
                ? builder.greaterThan(expression, parsedValue)
                : builder.lessThan(expression, parsedValue);
    }

    private Path<?> resolveJoinPath(Root<T> root) {
        String[] paths = joinField.split("\\.");
        if (paths.length == 1) {
            return root.get(paths[0]);
        }

        From<?, ?> from = root;
        for (int i = 0; i < paths.length - 1; i++) {
            from = getOrCreateJoin(from, paths[i]);
        }
        return from.get(paths[paths.length - 1]);
    }

    private Join<?, ?> getOrCreateJoin(From<?, ?> from, String attribute) {
        for (Join<?, ?> existingJoin : from.getJoins()) {
            if (!attribute.equals(existingJoin.getAttribute().getName())) {
                continue;
            }

            if (existingJoin.getJoinType() == joinType) {
                return existingJoin;
            }

            throw new IllegalArgumentException(
                    "Conflicting join type for relation [" + attribute + "]: existing="
                            + existingJoin.getJoinType() + ", requested=" + joinType);
        }

        return from.join(attribute, joinType);
    }

    private static final Map<Class<?>, Function<String, ? extends Comparable<?>>> PARSERS =
            Map.ofEntries(
                    Map.entry(Integer.class, Integer::parseInt),
                    Map.entry(int.class, Integer::parseInt),
                    Map.entry(Long.class, Long::parseLong),
                    Map.entry(long.class, Long::parseLong),
                    Map.entry(Double.class, Double::parseDouble),
                    Map.entry(double.class, Double::parseDouble),
                    Map.entry(LocalDate.class, LocalDate::parse),
                    Map.entry(LocalDateTime.class, LocalDateTime::parse)
            );

    private Comparable<?> parseValue(Class<?> type, String value) {
        Function<String, ? extends Comparable<?>> parser = PARSERS.get(type);
        if (parser == null) {
            return value;
        }

        try {
            return parser.apply(value);
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot parse [" + value + "] as " + type.getSimpleName() + " for field [" + joinField + "]", e);
        }
    }
}