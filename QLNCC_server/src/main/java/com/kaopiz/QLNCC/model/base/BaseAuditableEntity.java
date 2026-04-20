package com.kaopiz.QLNCC.model.base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BaseAuditableEntity extends BaseEntity {
    @Column(name = "created_by", updatable = false)
    private Long createdBy;

    @Column(name = "modified_by")
    private Long modifiedBy;

    @PrePersist
    protected void onCreate() {
        super.onCreate();

        //TODO: Code after
//        Long personnelId = SecurityUtils.getCurrentPersonnelId();
//        if (personnelId != null) {
//            this.createdBy = personnelId;
//            this.modifiedBy = personnelId;
//        }
    }

    @PreUpdate
    protected void onUpdate() {
        super.onUpdate();

        //TODO: Code after
//        Long personnelId = SecurityUtils.getCurrentPersonnelId();
//        if (personnelId != null) {
//            this.modifiedBy = personnelId;
//        }
    }
}
