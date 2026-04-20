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

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "supplier_contract_documents",
        uniqueConstraints = @UniqueConstraint(columnNames = {"supplier_id", "document_type_id"})
)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierContractDocument extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_type_id", nullable = false)
    private ContractDocumentType documentType;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "document_number", length = 100)
    private String documentNumber;

    @Column(name = "signed_date")
    private LocalDate signedDate;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "doc_management_system_no", length = 50)
    private String docManagementSystemNo;

    @Column(name = "pattern_required", length = 20)
    private String patternRequired;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Column(name = "submitted_by", length = 50)
    private String submittedBy;

    @Column(name = "last_checked_at")
    private LocalDateTime lastCheckedAt;
}
