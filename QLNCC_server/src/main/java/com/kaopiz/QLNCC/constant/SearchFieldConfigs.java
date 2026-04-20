package com.kaopiz.QLNCC.constant;

import com.kaopiz.QLNCC.repository.specification.FieldSearchConfig;
import com.kaopiz.QLNCC.repository.specification.SearchOperation;
import jakarta.persistence.criteria.JoinType;

import java.util.Map;

public final class SearchFieldConfigs {

    private SearchFieldConfigs() {}

    public static final Map<String, FieldSearchConfig> SUPPLIER_SEARCH_CONFIG =
            Map.ofEntries(
                    Map.entry("managementCode", FieldSearchConfig.equality()),
                    Map.entry("nameOfficial", FieldSearchConfig.contains()),
                    Map.entry("nameDisplay", FieldSearchConfig.contains()),
                    Map.entry("status", FieldSearchConfig.equality()),
                    Map.entry("patterns.patternCode", FieldSearchConfig.join(SearchOperation.EQUALITY, JoinType.LEFT)),
                    Map.entry("patterns.brandCategory", FieldSearchConfig.join()),
                    Map.entry("patterns.departmentName", FieldSearchConfig.join()));



}
