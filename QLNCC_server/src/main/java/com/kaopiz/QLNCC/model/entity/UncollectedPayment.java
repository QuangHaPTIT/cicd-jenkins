package com.kaopiz.QLNCC.model.entity;

import com.kaopiz.QLNCC.model.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(
        name = "uncollected_payments",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"contract_type", "contract_id", "period_type", "fiscal_year"}
        )
)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UncollectedPayment extends BaseEntity {

    @Column(name = "contract_type", nullable = false, length = 20)
    private String contractType;

    @Column(name = "contract_id", nullable = false)
    private Long contractId;

    @Column(name = "period_type", nullable = false, length = 30)
    private String periodType;

    @Column(name = "fiscal_year", nullable = false)
    private Short fiscalYear;

    @Column(name = "period_start_date", nullable = false)
    private LocalDate periodStartDate;

    @Column(name = "period_end_date", nullable = false)
    private LocalDate periodEndDate;

    @Column(name = "expected_amount", precision = 15, scale = 2)
    private BigDecimal expectedAmount;

    @Column(name = "actual_amount", precision = 15, scale = 2)
    private BigDecimal actualAmount;

    @Column(name = "difference", precision = 15, scale = 2)
    private BigDecimal difference;

    @Column(name = "status", length = 30)
    private String status;

    @Column(name = "collected_at")
    private LocalDate collectedAt;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
}
