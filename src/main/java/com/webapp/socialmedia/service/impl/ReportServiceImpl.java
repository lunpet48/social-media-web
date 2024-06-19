package com.webapp.socialmedia.service.impl;

import com.webapp.socialmedia.dto.requests.ReportRequest;
import com.webapp.socialmedia.entity.Notification;
import com.webapp.socialmedia.entity.Post;
import com.webapp.socialmedia.entity.Report;
import com.webapp.socialmedia.entity.User;
import com.webapp.socialmedia.enums.NotificationType;
import com.webapp.socialmedia.enums.ReportStatus;
import com.webapp.socialmedia.enums.ReportType;
import com.webapp.socialmedia.exceptions.BadRequestException;
import com.webapp.socialmedia.mapper.NotificationMapper;
import com.webapp.socialmedia.repository.NotificationRepository;
import com.webapp.socialmedia.repository.PostRepository;
import com.webapp.socialmedia.repository.ReportRepository;
import com.webapp.socialmedia.service.ReportService;
import com.webapp.socialmedia.utils.NotificationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;
    private final PostRepository postRepository;
    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final NotificationMapper notificationMapper;
    @Override
    public Report createReport(ReportRequest reportRequest) {
        return reportRepository.saveAndFlush(Report.builder()
                .reportType(reportRequest.getReportType())
                .reportedId(reportRequest.getReportedId())
                .describeReport(reportRequest.getDescribe())
                .reportStatus(ReportStatus.OPEN)
                .build());
    }

    @Override
    public List<Report> getAllOpenReport(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        return reportRepository.findByReportStatusOrderByCreatedAt(ReportStatus.OPEN, pageable);
    }

    @Override
    public List<Report> getAllCloseReport(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        return reportRepository.findByReportStatusOrderByCreatedAt(ReportStatus.CLOSE, pageable);
    }

    @Override
    public void deletePost(String postId) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Report> reports = reportRepository.findByReportedIdAndReportType(postId, ReportType.POST);
        Post post = postRepository.findByIdAndIsDeleted(postId, Boolean.FALSE).orElseThrow(() -> new BadRequestException("Không tìm thấy bài viết!!"));
        post.setIsDeleted(Boolean.TRUE);
        postRepository.save(post);

        //Thay đổi trạng thái report -> CLOSE
        reports.forEach(this::setReportStatusClose);

        //Notification
        Notification notification = notificationRepository.saveAndFlush(Notification.builder()
                        .receiver(post.getUser())
                        .actor(currentUser)
                        .notificationType(NotificationType.REPORTED_POST)
                        .idType(postId)
                .build());

        simpMessagingTemplate.convertAndSendToUser(post.getUser().getUsername(), NotificationUtils.REPORTED_LINK, notificationMapper.toResponse(notification));
    }

    @Override
    public Report setReportStatusOpen(Report report) {
        report.setReportStatus(ReportStatus.OPEN);
        return reportRepository.saveAndFlush(report);
    }

    @Override
    public Report setReportStatusClose(Report report) {
        report.setReportStatus(ReportStatus.CLOSE);
        return reportRepository.saveAndFlush(report);
    }

    @Override
    public Post recoveryPost(String postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new BadRequestException("Không tìm thấy bài viết"));
        post.setIsDeleted(Boolean.FALSE);
        return postRepository.saveAndFlush(post);
    }

    @Override
    public Report setReportStatusOpen(String reportId) {
        Report report = reportRepository.findById(reportId).orElseThrow(() -> new BadRequestException("Không tìm thấy report"));
        return this.setReportStatusOpen(report);
    }

    @Override
    public Report setReportStatusClose(String reportId) {
        Report report = reportRepository.findById(reportId).orElseThrow(() -> new BadRequestException("Không tìm thấy report"));
        return this.setReportStatusClose(report);
    }

    @Override
    public Report createFeedback(ReportRequest reportRequest) {
        return reportRepository.saveAndFlush(Report.builder()
                        .reportedId(reportRequest.getReportedId())
                        .describeReport(reportRequest.getDescribe())
                        .reportType(ReportType.FEEDBACK)
                        .reportStatus(ReportStatus.OPEN)
                .build());
    }

    @Override
    public Report getReport(String reportId) {
        return reportRepository.findById(reportId).orElseThrow(() -> new BadRequestException("Không tìm thấy report này"));
    }
}
