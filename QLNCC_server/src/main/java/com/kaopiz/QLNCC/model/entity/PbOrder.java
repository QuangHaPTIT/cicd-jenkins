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
@Table(name = "pb_orders")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PbOrder extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "factory_id")
    private FactoryMaster factory;

    @Column(name = "pb_category", nullable = false, length = 20)
    private String pbCategory;

    @Column(name = "department_name", length = 100)
    private String departmentName;

    @Column(name = "team_name", length = 100)
    private String teamName;

    @Column(name = "applicant_name", length = 100)
    private String applicantName;

    @Column(name = "large_category_code", length = 10)
    private String largeCategoryCode;

    @Column(name = "large_category_name", length = 100)
    private String largeCategoryName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductMaster product;

    @Column(name = "order_number", length = 50)
    private String orderNumber;

    @Column(name = "order_date")
    private LocalDate orderDate;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "unit", length = 20)
    private String unit;

    @Column(name = "unit_price", precision = 15, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "total_amount", precision = 15, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "aw_decision_no", length = 50)
    private String awDecisionNo;

    @Column(name = "aw_approval_status", length = 30)
    private String awApprovalStatus;

    @Column(name = "aw_approved_at")
    private LocalDateTime awApprovedAt;

    @Column(name = "has_next_order", length = 30)
    private String hasNextOrder;

    @Column(name = "next_order_planned_at")
    private LocalDate nextOrderPlannedAt;

    @Column(name = "order_doc_sent_at")
    private LocalDate orderDocSentAt;

    @Column(name = "order_doc_deadline")
    private LocalDate orderDocDeadline;

    @Column(name = "recipient_name", length = 200)
    private String recipientName;

    @Column(name = "recipient_email", length = 200)
    private String recipientEmail;

    @Column(name = "exclusion_type", length = 50)
    private String exclusionType;

    @Column(name = "status", nullable = false, length = 30)
    private String status;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_by", length = 50)
    private String createdBy;

    @Column(name = "updated_by", length = 50)
    private String updatedBy;
}
