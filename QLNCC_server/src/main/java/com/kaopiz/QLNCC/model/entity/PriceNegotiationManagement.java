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
        name = "price_negotiation_management",
        uniqueConstraints = @UniqueConstraint(columnNames = {"supplier_id", "fiscal_year"})
)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PriceNegotiationManagement extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @Column(name = "fiscal_year", nullable = false)
    private Short fiscalYear;

    @Column(name = "is_target", nullable = false)
    private Boolean isTarget;

    @Column(name = "exclusion_reason", columnDefinition = "TEXT")
    private String exclusionReason;

    @Column(name = "status", nullable = false, length = 30)
    private String status;

    @Column(name = "department", length = 100)
    private String department;

    @Column(name = "category", length = 100)
    private String category;

    @Column(name = "negotiation_date")
    private LocalDate negotiationDate;

    @Column(name = "completed_at")
    private LocalDate completedAt;

    @Column(name = "result_notes", columnDefinition = "TEXT")
    private String resultNotes;

    @Column(name = "alert_sent_at")
    private LocalDateTime alertSentAt;
}
