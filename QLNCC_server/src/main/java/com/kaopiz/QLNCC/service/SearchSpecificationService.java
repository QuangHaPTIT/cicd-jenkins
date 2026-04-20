package com.kaopiz.QLNCC.service;

import com.kaopiz.QLNCC.model.request.SearchRequest;
import com.kaopiz.QLNCC.repository.specification.FieldSearchConfig;
import com.kaopiz.QLNCC.repository.specification.GenericSpecificationBuilder;
import com.kaopiz.QLNCC.repository.specification.JoinSpecification;

import lombok.extern.slf4j.Slf4j;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class SearchSpecificationService {

    public <T> Specification<T> buildSpecification(
            SearchRequest request,
            Map<String, FieldSearchConfig> fieldConfig) {

        if (request == null) {
            return null;
        }

        if (fieldConfig == null || fieldConfig.isEmpty()) {
            throw new IllegalArgumentException("Field search config must not be empty");
        }

        GenericSpecificationBuilder<T> builder = new GenericSpecificationBuilder<>();

        List<SearchRequest.SearchEntry> filters = request.getFilters();
        if (filters != null) {
            for (SearchRequest.SearchEntry entry : filters) {
                String key = entry.getKey();
                String value = entry.getValue();

                if (key == null || key.isBlank()) {
                    log.warn("Field search key is null or blank");
                    continue;
                }

                if (value == null || value.isBlank()) {
                    continue;
                }

                FieldSearchConfig config = fieldConfig.get(key);
                if (config == null || config.isJoin()) {
                    log.warn("Field search config not found for key: {}", key);
                    continue;
                }

                builder.withIfPresent(key, config.getOperation(), value);
            }
        }

        Specification<T> specification = builder.build();

        List<SearchRequest.SearchEntry> joinFilters = request.getJoinFilters();
        if (joinFilters != null) {
            for (SearchRequest.SearchEntry entry : joinFilters) {
                String key = entry.getKey();
                String value = entry.getValue();

                if (key == null || key.isBlank()) {
                    continue;
                }

                if (value == null || value.isBlank()) {
                    continue;
                }

                FieldSearchConfig config = fieldConfig.get(key);
                if (config == null || !config.isJoin()) {
                    continue;
                }

                JoinSpecification<T> joinSpecification =
                    new JoinSpecification<>(key, value, config.getOperation(), config.getJoinType());

                specification = specification == null
                        ? Specification.where(joinSpecification)
                        : Specification.where(specification).and(joinSpecification);
            }
        }

        return specification;
    }
}
