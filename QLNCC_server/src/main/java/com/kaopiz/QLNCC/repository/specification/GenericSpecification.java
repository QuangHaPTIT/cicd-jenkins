package com.kaopiz.QLNCC.repository.specification;

import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

import static com.kaopiz.QLNCC.utils.RegexPatternUtils.LIKE_ESCAPE_CHAR;
import static com.kaopiz.QLNCC.utils.RegexPatternUtils.escapeLikePattern;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GenericSpecification<T> implements Specification<T> {

    private SpecSearchCriteria criteria;

    @Override
    public Predicate toPredicate(Root<T> root,
                                 CriteriaQuery<?> query,
                                 CriteriaBuilder builder) {
        String key      = criteria.getKey();
        Object rawValue = criteria.getValue();

        if (rawValue == null) return builder.conjunction();

        String strValue = rawValue.toString();
        String normalizedValue = strValue.toLowerCase(Locale.ROOT);
        String escapedLikeValue = escapeLikePattern(normalizedValue);
        Path<?> path    = root.get(key);

        return switch (criteria.getOperation()) {
            case EQUALITY    -> builder.equal(builder.lower(path.as(String.class)), normalizedValue);
            case NEGATION    -> builder.notEqual(builder.lower(path.as(String.class)), normalizedValue);
            case LIKE,
                 CONTAINS    -> builder.like(builder.lower(path.as(String.class)), "%" + escapedLikeValue + "%", LIKE_ESCAPE_CHAR);
            case STARTS_WITH -> builder.like(builder.lower(path.as(String.class)), escapedLikeValue + "%", LIKE_ESCAPE_CHAR);
            case ENDS_WITH   -> builder.like(builder.lower(path.as(String.class)), "%" + escapedLikeValue, LIKE_ESCAPE_CHAR);
            case GREATER_THAN -> buildCompare(path, builder, strValue, true);
            case LESS_THAN    -> buildCompare(path, builder, strValue, false);
        };
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Predicate buildCompare(Path<?> path,
                                   CriteriaBuilder builder,
                                   String strValue,
                                   boolean isGreater) {

        Comparable value = parseValue(path.getJavaType(), strValue);

        Expression<Comparable> expr = (Expression<Comparable>) path;

        return isGreater
                ? builder.greaterThan(expr, value)
                : builder.lessThan(expr, value);
    }

    private static final Map<Class<?>, Function<String, ? extends Comparable<?>>> PARSERS =
            Map.ofEntries(
                    Map.entry(Integer.class,       Integer::parseInt),
                    Map.entry(int.class,           Integer::parseInt),
                    Map.entry(Long.class,          Long::parseLong),
                    Map.entry(long.class,          Long::parseLong),
                    Map.entry(Double.class,        Double::parseDouble),
                    Map.entry(double.class,        Double::parseDouble),
                    Map.entry(LocalDate.class,     LocalDate::parse),
                    Map.entry(LocalDateTime.class, LocalDateTime::parse)
            );

    private Comparable<?> parseValue(Class<?> type, String value) {
        Function<String, ? extends Comparable<?>> parser = PARSERS.get(type);
        if (parser == null) return value;
        try {
            return parser.apply(value);
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot parse [" + value + "] as " + type.getSimpleName() + " for field [" + criteria.getKey() + "]", e);
        }
    }
}