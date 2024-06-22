package com.webapp.socialmedia.service.impl;

import com.webapp.socialmedia.dto.requests.ReportRequest;
import com.webapp.socialmedia.entity.*;
import com.webapp.socialmedia.enums.*;
import com.webapp.socialmedia.exceptions.BadRequestException;
import com.webapp.socialmedia.mapper.NotificationMapper;
import com.webapp.socialmedia.repository.*;
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
    private final UserRepository userRepository;
    private final LogRepository logRepository;
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
    public Log deletePost(String postId) {
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
                        .notificationType(NotificationType.DELETE_POST)
                        .idType(postId)
                .build());

        simpMessagingTemplate.convertAndSendToUser(post.getUser().getUsername(), NotificationUtils.REPORTED_LINK, notificationMapper.toResponse(notification));

        return logRepository.saveAndFlush(Log.builder()
                        .eventType(EventType.DELETE_POST)
                        .objectId(postId)
                .build());
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
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Post post = postRepository.findById(postId).orElseThrow(() -> new BadRequestException("Không tìm thấy bài viết"));
        post.setIsDeleted(Boolean.FALSE);

        //Notification
        Notification notification = notificationRepository.saveAndFlush(Notification.builder()
                .receiver(post.getUser())
                .actor(currentUser)
                .notificationType(NotificationType.RECOVERY_POST)
                .idType(postId)
                .build());

        simpMessagingTemplate.convertAndSendToUser(post.getUser().getUsername(), NotificationUtils.REPORTED_LINK, notificationMapper.toResponse(notification));

        logRepository.save(Log.builder()
                        .objectId(postId)
                    .eventType(EventType.RECOVERY_POST)
                .build());

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

    @Override
    public Log lockUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BadRequestException("Không tìm thấy người dùng"));
        user.setIsLocked(true);
        user.setLockReason(LockReason.BAD_USER);

        Log log = logRepository.saveAndFlush(Log.builder()
                .eventType(EventType.LOCK_USER)
                .objectId(userId)
                .build());

        user.setLogId(log.getId());
        userRepository.saveAndFlush(user);

        List<Report> reports = reportRepository.findByReportedIdAndReportType(userId, ReportType.USER);
        reports.forEach(this::setReportStatusClose);

        return log;
    }

    @Override
    public Log unlockUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BadRequestException("Không tìm thấy người dùng"));
        user.setIsLocked(false);
        user.setLogId(null);
        userRepository.saveAndFlush(user);

        return logRepository.saveAndFlush(Log.builder()
                .eventType(EventType.UNLOCK_USER)
                .objectId(userId)
                .build());
    }
}
