package com.kaopiz.QLNCC.controller;

import com.kaopiz.QLNCC.model.request.SearchRequest;
import com.kaopiz.QLNCC.model.request.SupplierSimpleSearchParams;
import com.kaopiz.QLNCC.model.response.SupplierSearchResponse;
import com.kaopiz.QLNCC.service.SupplierSearchService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/suppliers")
@RequiredArgsConstructor
public class SupplierSearchController {

    private final SupplierSearchService supplierSearchService;

    @GetMapping("/search")
    public Page<@NonNull SupplierSearchResponse> searchSimple(
            @ModelAttribute SupplierSimpleSearchParams params,
            @PageableDefault(size = 20) Pageable pageable) {
        return supplierSearchService.searchSimple(params, pageable);
    }

    @PostMapping("/search")
    public Page<@NonNull SupplierSearchResponse> searchAdvanced(
            @RequestBody(required = false) SearchRequest request,
            @PageableDefault(size = 20) Pageable pageable) {
        return supplierSearchService.search(request, pageable);
    }
}
