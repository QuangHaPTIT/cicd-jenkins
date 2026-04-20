package com.kaopiz.QLNCC.service;

import com.kaopiz.QLNCC.constant.SearchFieldConfigs;
import com.kaopiz.QLNCC.model.entity.Supplier;
import com.kaopiz.QLNCC.model.entity.SupplierPattern;
import com.kaopiz.QLNCC.model.request.SearchRequest;
import com.kaopiz.QLNCC.model.request.SupplierSimpleSearchParams;
import com.kaopiz.QLNCC.model.response.SupplierSearchResponse;
import com.kaopiz.QLNCC.repository.SupplierRepository;
import com.kaopiz.QLNCC.utils.SpecificationSearchUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierSearchService {

    private final SupplierRepository supplierRepository;
    private final SearchSpecificationService searchSpecificationService;

    @Transactional(readOnly = true)
    public Page<@NonNull SupplierSearchResponse> searchSimple(
            SupplierSimpleSearchParams params, Pageable pageable) {
        if (params == null) {
            params = new SupplierSimpleSearchParams();
        }
        return search(simpleGetRequest(params), pageable);
    }

    private static SearchRequest simpleGetRequest(SupplierSimpleSearchParams params) {
        SearchRequest request = new SearchRequest();
        List<SearchRequest.SearchEntry> filters = new ArrayList<>();
        addFilter(filters, "nameOfficial", params.getName());
        addFilter(filters, "managementCode", params.getManagementCode());
        addFilter(filters, "status", params.getStatus());
        request.setFilters(filters);
        return request;
    }

    private static void addFilter(
            List<SearchRequest.SearchEntry> filters, String key, String value) {
        if (value == null || value.isBlank()) {
            return;
        }
        SearchRequest.SearchEntry e = new SearchRequest.SearchEntry();
        e.setKey(key);
        e.setValue(value);
        filters.add(e);
    }

    @Transactional(readOnly = true)
    public Page<@NonNull SupplierSearchResponse> search(SearchRequest request, Pageable pageable) {
        Specification<@NonNull Supplier> spec =
                searchSpecificationService.buildSpecification(
                        request, SearchFieldConfigs.SUPPLIER_SEARCH_CONFIG);
        if (SpecificationSearchUtils.hasJoinFilters(request)) {
            spec = Specification.where(SpecificationSearchUtils.<@NonNull Supplier>distinctRoot()).and(spec);
        }
        return supplierRepository.findAll(spec, pageable).map(this::toResponse);
    }

    private SupplierSearchResponse toResponse(Supplier s) {
        List<SupplierPattern> plist = s.getPatterns();
        List<SupplierSearchResponse.PatternSnippet> patterns =
                plist == null || plist.isEmpty()
                        ? Collections.emptyList()
                        : plist.stream()
                                .map(
                                        p ->
                                                new SupplierSearchResponse.PatternSnippet(
                                                        p.getId(),
                                                        p.getPatternCode(),
                                                        p.getBrandCategory(),
                                                        p.getDepartmentName()))
                                .toList();
        return new SupplierSearchResponse(
                s.getId(),
                s.getManagementCode(),
                s.getNameOfficial(),
                s.getNameDisplay(),
                s.getStatus(),
                patterns);
    }
}
