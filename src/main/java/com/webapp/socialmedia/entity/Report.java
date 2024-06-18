package com.webapp.socialmedia.entity;

import com.webapp.socialmedia.enums.ReportStatus;
import com.webapp.socialmedia.enums.ReportType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "db_report")
public class Report {
    @Id
    @UuidGenerator
    private String id;
    @Enumerated(EnumType.STRING)
    private ReportType reportType;
    private String reportedId;
    private String describeReport;
    @Enumerated(EnumType.STRING)
    private ReportStatus reportStatus;
    private Date createdAt;

    @PrePersist
    void createdAt() {
        this.createdAt = new Date();
    }
}
