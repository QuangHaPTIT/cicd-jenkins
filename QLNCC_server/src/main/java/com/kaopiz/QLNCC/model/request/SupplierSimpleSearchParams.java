package com.kaopiz.QLNCC.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SupplierSimpleSearchParams {

    private String name;
    private String managementCode;
    private String status;
}
