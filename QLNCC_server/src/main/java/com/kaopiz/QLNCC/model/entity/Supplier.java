package com.kaopiz.QLNCC.model.entity;

import com.kaopiz.QLNCC.model.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "suppliers")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Supplier extends BaseEntity {

    @OneToMany(mappedBy = "supplier", fetch = FetchType.LAZY)
    @Builder.Default
    @SQLRestriction("is_active = true")
    private List<SupplierPattern> patterns = new ArrayList<>();

    @Column(name = "management_code", nullable = false, unique = true, length = 10)
    private String managementCode;

    @Column(name = "core_system_code", length = 50)
    private String coreSystemCode;

    @Column(name = "representative_code", length = 50)
    private String representativeCode;

    @Column(name = "name_official", nullable = false, length = 200)
    private String nameOfficial;

    @Column(name = "name_display", length = 200)
    private String nameDisplay;

    @Column(name = "name_kana", length = 200)
    private String nameKana;

    @Column(name = "trade_type", length = 100)
    private String tradeType;

    @Column(name = "delivery_type", length = 100)
    private String deliveryType;

    @Column(name = "subcontractor_list_no")
    private Integer subcontractorListNo;

    @Column(name = "capital_amount_million", precision = 15, scale = 2)
    private BigDecimal capitalAmountMillion;

    @Column(name = "employee_count")
    private Integer employeeCount;

    @Column(name = "is_subsidiary")
    private Boolean isSubsidiary;

    @Column(name = "zip_code", length = 10)
    private String zipCode;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @Column(name = "phone", length = 30)
    private String phone;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "deleted_reason", columnDefinition = "TEXT")
    private String deletedReason;

    @Column(name = "added_to_list_at")
    private LocalDate addedToListAt;

    @Column(name = "subcontractor_duty_type", length = 100)
    private String subcontractorDutyType;

    @Column(name = "delegate_work_content", columnDefinition = "TEXT")
    private String delegateWorkContent;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_by", length = 50)
    private String createdBy;

    @Column(name = "updated_by", length = 50)
    private String updatedBy;
}
