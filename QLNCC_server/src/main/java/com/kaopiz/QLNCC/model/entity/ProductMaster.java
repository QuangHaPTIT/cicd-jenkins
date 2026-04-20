package com.kaopiz.QLNCC.model.entity;

import com.kaopiz.QLNCC.model.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "product_masters")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ProductMaster extends BaseEntity {

    @Column(name = "ms_code", nullable = false, unique = true, length = 20)
    private String msCode;

    @Column(name = "product_name", nullable = false, length = 300)
    private String productName;

    @Column(name = "brand_type", length = 10)
    private String brandType;

    @Column(name = "specification", length = 200)
    private String specification;

    @Column(name = "large_category_code", length = 10)
    private String largeCategoryCode;

    @Column(name = "large_category_name", length = 100)
    private String largeCategoryName;

    @Column(name = "status", length = 20)
    private String status;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
}
