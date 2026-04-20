package com.kaopiz.QLNCC.model.entity;

import com.kaopiz.QLNCC.model.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Table(name = "factory_masters")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class FactoryMaster extends BaseEntity {

    @Column(name = "factory_code", nullable = false, unique = true, length = 20)
    private String factoryCode;

    @Column(name = "factory_name", nullable = false, length = 200)
    private String factoryName;

    @Column(name = "factory_name_kana", length = 200)
    private String factoryNameKana;

    @Column(name = "zip_code", length = 10)
    private String zipCode;

    @Column(name = "prefecture", length = 50)
    private String prefecture;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @Column(name = "latitude", precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 10, scale = 7)
    private BigDecimal longitude;

    @Column(name = "phone", length = 30)
    private String phone;

    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
}
