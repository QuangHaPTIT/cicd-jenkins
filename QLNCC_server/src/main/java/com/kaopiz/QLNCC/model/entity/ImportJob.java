package com.kaopiz.QLNCC.model.entity;

import com.kaopiz.QLNCC.utils.Util;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "import_jobs")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "job_type", nullable = false, length = 50)
    private String jobType;

    @Column(name = "file_name", length = 500)
    private String fileName;

    @Column(name = "file_size_bytes")
    private Long fileSizeBytes;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "total_rows")
    private Integer totalRows;

    @Column(name = "success_rows")
    private Integer successRows;

    @Column(name = "error_rows")
    private Integer errorRows;

    @Column(name = "skipped_rows")
    private Integer skippedRows;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "error_log", columnDefinition = "jsonb")
    private String errorLog;

    @Column(name = "summary", columnDefinition = "jsonb")
    private String summary;

    @Column(name = "triggered_by", length = 50)
    private String triggeredBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = Util.getNow();
        }
    }
}
