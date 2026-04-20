package com.kaopiz.QLNCC.model.response;

import java.util.List;

public record SupplierSearchResponse(
        Long id,
        String managementCode,
        String nameOfficial,
        String nameDisplay,
        String status,
        List<PatternSnippet> patterns) {

    public record PatternSnippet(
            Long id, String patternCode, String brandCategory, String departmentName) {}
}
