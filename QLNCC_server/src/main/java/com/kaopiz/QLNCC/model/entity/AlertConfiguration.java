package com.kaopiz.QLNCC.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "alert_configurations")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertConfiguration {

    @Id
    @Column(name = "alert_type", nullable = false, length = 50)
    private String alertType;

    @Column(name = "is_enabled")
    private Boolean isEnabled;

    @Column(name = "advance_days_1")
    private Short advanceDays1;

    @Column(name = "advance_days_2")
    private Short advanceDays2;

    @Column(name = "advance_days_3")
    private Short advanceDays3;

    @Column(name = "email_enabled")
    private Boolean emailEnabled;

    @Column(name = "push_enabled")
    private Boolean pushEnabled;

    @Column(name = "recipient_roles", columnDefinition = "varchar[]")
    private String[] recipientRoles;
}
