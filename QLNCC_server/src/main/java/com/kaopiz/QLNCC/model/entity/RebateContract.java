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
@Table(name = "rebate_contracts")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class RebateContract extends BaseEntity {

    @Column(name = "management_no", unique = true, length = 50)
    private String managementNo;

    @Column(name = "contract_no", nullable = false, unique = true, length = 50)
    private String contractNo;

    @Column(name = "prev_year_contract_no", length = 50)
    private String prevYearContractNo;

    @Column(name = "next_year_contract_no", length = 50)
    private String nextYearContractNo;

    @Column(name = "rebate_calc_contract_no", length = 50)
    private String rebateCalcContractNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @Column(name = "aw_decision_no", length = 50)
    private String awDecisionNo;

    @Column(name = "cvs_ff_hd_type", length = 20)
    private String cvsFfHdType;

    @Column(name = "draft_department", length = 100)
    private String draftDepartment;

    @Column(name = "draft_team", length = 100)
    private String draftTeam;

    @Column(name = "drafter_name", length = 100)
    private String drafterName;

    @Column(name = "input_person", length = 100)
    private String inputPerson;

    @Column(name = "input_date")
    private LocalDate inputDate;

    @Column(name = "draft_date")
    private LocalDate draftDate;

    @Column(name = "contract_signed_date")
    private LocalDate contractSignedDate;

    @Column(name = "contract_start_date", nullable = false)
    private LocalDate contractStartDate;

    @Column(name = "contract_end_date", nullable = false)
    private LocalDate contractEndDate;

    @Column(name = "calculation_start_date")
    private LocalDate calculationStartDate;

    @Column(name = "calculation_end_date")
    private LocalDate calculationEndDate;

    @Column(name = "payment_due_date")
    private LocalDate paymentDueDate;

    @Column(name = "rebate_recognition_date")
    private LocalDate rebateRecognitionDate;

    @Column(name = "rebate_type", length = 50)
    private String rebateType;

    @Column(name = "large_category_code", length = 10)
    private String largeCategoryCode;

    @Column(name = "target_products", columnDefinition = "TEXT")
    private String targetProducts;

    @Column(name = "target_region", length = 100)
    private String targetRegion;

    @Column(name = "calculation_basis", columnDefinition = "TEXT")
    private String calculationBasis;

    @Column(name = "direct_rebate_amount", precision = 15, scale = 2)
    private BigDecimal directRebateAmount;

    @Column(name = "all_store_rebate_amount", precision = 15, scale = 2)
    private BigDecimal allStoreRebateAmount;

    @Column(name = "payment_method", length = 30)
    private String paymentMethod;

    @Column(name = "bank_name", length = 100)
    private String bankName;

    @Column(name = "bank_branch", length = 100)
    private String bankBranch;

    @Column(name = "account_number", length = 50)
    private String accountNumber;

    @Column(name = "seal_request_date")
    private LocalDate sealRequestDate;

    @Column(name = "seal_request_no", length = 50)
    private String sealRequestNo;

    @Column(name = "seal_return_status", length = 30)
    private String sealReturnStatus;

    @Column(name = "invoice_no", length = 50)
    private String invoiceNo;

    @Column(name = "invoice_input_at")
    private LocalDateTime invoiceInputAt;

    @Column(name = "is_subcontractor_target")
    private Boolean isSubcontractorTarget;

    @Column(name = "status", nullable = false, length = 30)
    private String status;

    @Column(name = "contract_result", length = 30)
    private String contractResult;

    @Column(name = "has_agreement_letter", length = 10)
    private String hasAgreementLetter;

    @Column(name = "reason_letter_required", length = 10)
    private String reasonLetterRequired;

    @Column(name = "reason_letter_content", columnDefinition = "TEXT")
    private String reasonLetterContent;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "other_notes", columnDefinition = "TEXT")
    private String otherNotes;

    @Column(name = "supplier_contact_dept", columnDefinition = "TEXT")
    private String supplierContactDept;

    @Column(name = "supplier_contact_person", length = 200)
    private String supplierContactPerson;

    @Column(name = "recognition_payment_diff_note", length = 500)
    private String recognitionPaymentDiffNote;

    @Column(name = "created_by", length = 50)
    private String createdBy;

    @Column(name = "updated_by", length = 50)
    private String updatedBy;
}
