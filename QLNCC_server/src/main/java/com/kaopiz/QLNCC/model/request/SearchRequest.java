package com.kaopiz.QLNCC.model.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SearchRequest {

    private List<SearchEntry> filters;
    private List<SearchEntry> joinFilters;

    @Getter
    @Setter
    public static class SearchEntry {
        private String key;
        private String value;
    }
}
