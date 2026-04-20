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
        name = "pl_insurance_management",
        uniqueConstraints = @UniqueConstraint(columnNames = {"supplier_id", "insurance_year"})
)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PlInsuranceManagement extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @Column(name = "insurance_year", nullable = false)
    private Short insuranceYear;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "collection_status", nullable = false, length = 20)
    private String collectionStatus;

    @Column(name = "collected_at")
    private LocalDate collectedAt;

    @Column(name = "collected_by", length = 50)
    private String collectedBy;

    @Column(name = "alert_sent_at")
    private LocalDateTime alertSentAt;

    @Column(name = "reminder_count")
    private Short reminderCount;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
}
