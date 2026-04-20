package com.kaopiz.QLNCC.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class SupplierCodeAliasId implements Serializable {

    @Column(name = "supplier_id")
    private Long supplierId;

    @Column(name = "system_name", length = 50)
    private String systemName;

    @Column(name = "alias_code", length = 50)
    private String aliasCode;
}
