package com.webapp.socialmedia.service;

import com.webapp.socialmedia.dto.requests.ReportRequest;
import com.webapp.socialmedia.entity.Log;
import com.webapp.socialmedia.entity.Post;
import com.webapp.socialmedia.entity.Report;

import java.util.List;

public interface ReportService {
    Report createReport(ReportRequest reportRequest);

    List<Report> getAllOpenReport(int pageNo, int pageSize);

    List<Report> getAllCloseReport(int pageNo, int pageSize);

    Log deletePost(String postId, String message);

    Report setReportStatusOpen(Report report);

    Report setReportStatusClose(Report report);

    Post recoveryPost(String postId, String message);

    Report setReportStatusOpen(String reportId);

    Report setReportStatusClose(String reportId);

    Report createFeedback(ReportRequest reportRequest);

    Report getReport(String reportId);

    Log lockUser(String userId, String message);

    Log unlockUser(String userId, String message);

    Log getLog(String logId);
}
