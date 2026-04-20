package com.kaopiz.QLNCC.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "contract_document_types")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractDocumentType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false, unique = true, length = 50)
    private String code;

    @Column(name = "display_number", length = 10)
    private String displayNumber;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "short_name", length = 100)
    private String shortName;

    @Column(name = "category", length = 50)
    private String category;

    @Column(name = "requires_date")
    private Boolean requiresDate;

    @Column(name = "requires_doc_no")
    private Boolean requiresDocNo;

    @Column(name = "sort_order")
    private Short sortOrder;

    @Column(name = "is_active")
    private Boolean isActive;
}
