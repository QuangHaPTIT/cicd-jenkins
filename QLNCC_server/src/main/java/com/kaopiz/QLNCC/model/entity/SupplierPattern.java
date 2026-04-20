package com.kaopiz.QLNCC.model.entity;

import com.kaopiz.QLNCC.model.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(
        name = "supplier_patterns",
        uniqueConstraints = @UniqueConstraint(columnNames = {"supplier_id", "pattern_code"})
)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierPattern extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @Column(name = "pattern_code", nullable = false, length = 10)
    private String patternCode;

    @Column(name = "brand_category", length = 20)
    private String brandCategory;

    @Column(name = "main_products", columnDefinition = "TEXT")
    private String mainProducts;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "md_user_id")
    private User mdUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mgr_user_id")
    private User mgrUser;

    @Column(name = "department_name", length = 100)
    private String departmentName;

    @Column(name = "team_name", length = 100)
    private String teamName;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
}
