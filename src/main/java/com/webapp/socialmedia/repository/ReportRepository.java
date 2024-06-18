package com.webapp.socialmedia.repository;

import com.webapp.socialmedia.entity.Report;
import com.webapp.socialmedia.enums.ReportStatus;
import com.webapp.socialmedia.enums.ReportType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, String> {
    List<Report> findByReportStatusOrderByCreatedAt(ReportStatus reportStatus, Pageable pageable);

    List<Report> findByReportedIdAndReportType(String id, ReportType reportType);
}
