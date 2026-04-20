package com.kaopiz.QLNCC.model.entity;

import com.kaopiz.QLNCC.model.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoices")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Invoice extends BaseEntity {

    @Column(name = "invoice_no", nullable = false, unique = true, length = 50)
    private String invoiceNo;

    @Column(name = "invoice_source", nullable = false, length = 20)
    private String invoiceSource;

    @Column(name = "contract_type", length = 20)
    private String contractType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rebate_contract_id")
    private RebateContract rebateContract;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promo_contract_id")
    private PromoContract promoContract;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @Column(name = "supplier_code_aw", length = 30)
    private String supplierCodeAw;

    @Column(name = "supplier_name_aw", length = 200)
    private String supplierNameAw;

    @Column(name = "issuer_department", length = 100)
    private String issuerDepartment;

    @Column(name = "issuer_name", length = 100)
    private String issuerName;

    @Column(name = "issuer_code", length = 30)
    private String issuerCode;

    @Column(name = "issuer_org_code", length = 30)
    private String issuerOrgCode;

    @Column(name = "charge_dept_code", length = 30)
    private String chargeDeptCode;

    @Column(name = "charge_dept_name", length = 100)
    private String chargeDeptName;

    @Column(name = "amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(name = "tax_rate", precision = 5, scale = 3)
    private BigDecimal taxRate;

    @Column(name = "applied_at")
    private LocalDateTime appliedAt;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "recognition_month", length = 7)
    private String recognitionMonth;

    @Column(name = "transfer_due_date")
    private LocalDate transferDueDate;

    @Column(name = "offset_date")
    private LocalDate offsetDate;

    @Column(name = "account_code", length = 20)
    private String accountCode;

    @Column(name = "account_detail_code", length = 20)
    private String accountDetailCode;

    @Column(name = "account_name", length = 200)
    private String accountName;

    @Column(name = "debit_account_code", length = 20)
    private String debitAccountCode;

    @Column(name = "debit_account_name", length = 200)
    private String debitAccountName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "payment_method", length = 30)
    private String paymentMethod;

    @Column(name = "aw_doc_status", length = 30)
    private String awDocStatus;

    @Column(name = "line_item_no", length = 10)
    private String lineItemNo;

    @Column(name = "invoice_type", length = 30)
    private String invoiceType;

    @Column(name = "is_cancelled")
    private Boolean isCancelled;

    @Column(name = "management_input_at")
    private LocalDateTime managementInputAt;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_by", length = 50)
    private String createdBy;

    @Column(name = "updated_by", length = 50)
    private String updatedBy;
}
