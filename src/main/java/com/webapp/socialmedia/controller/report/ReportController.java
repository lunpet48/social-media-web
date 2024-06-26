package com.webapp.socialmedia.controller.report;

import com.webapp.socialmedia.dto.requests.ReportRequest;
import com.webapp.socialmedia.dto.responses.ResponseDTO;
import com.webapp.socialmedia.entity.Post;
import com.webapp.socialmedia.mapper.PostMapper;
import com.webapp.socialmedia.service.PostMediaService;
import com.webapp.socialmedia.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/report")
public class ReportController {
    private final ReportService reportService;
    private final PostMediaService postMediaService;
    private final PostMapper postMapper;
    @PostMapping
    public ResponseEntity<?> reportPost(@RequestBody ReportRequest reportRequest) {
        return ResponseEntity.ok(new ResponseDTO().success(reportService.createReport(reportRequest)));
    }

    @GetMapping("/{reportId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getReport(@PathVariable String reportId) {
        return ResponseEntity.ok(new ResponseDTO().success(reportService.getReport(reportId)));
    }

    @GetMapping("/open")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getReport(@RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "0") int pageNo) {
        return ResponseEntity.ok(new ResponseDTO().success(reportService.getAllOpenReport(pageNo, pageSize)));
    }

    @GetMapping("/close")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getCloseReport(@RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "0") int pageNo) {
        return ResponseEntity.ok(new ResponseDTO().success(reportService.getAllCloseReport(pageNo, pageSize)));
    }

    @PostMapping("/{reportId}/open")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> setOpen(@PathVariable String reportId) {
        return ResponseEntity.ok(new ResponseDTO().success(reportService.setReportStatusOpen(reportId)));
    }

    @PostMapping("/{reportId}/close")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> setClose(@PathVariable String reportId) {
        return ResponseEntity.ok(new ResponseDTO().success(reportService.setReportStatusClose(reportId)));
    }

    @DeleteMapping("/delete-post")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deletePost(@RequestBody AdminRequest request) {
        reportService.deletePost(request.id, request.message);
    }

    @PostMapping("/recovery-post")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> recoveryPost(@RequestBody AdminRequest request) {
        Post post = reportService.recoveryPost(request.id, request.message);
        return ResponseEntity.ok(new ResponseDTO().success(postMapper.toResponse(post, postMediaService.getFilesByPostId(post.getId()))));
    }

    @PostMapping("/lock-user")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> lockUser(@RequestBody AdminRequest request) {
        return ResponseEntity.ok(new ResponseDTO().success(reportService.lockUser(request.id, request.message)));
    }

    @PostMapping("/unlock-user")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> unlockUser(@RequestBody AdminRequest request) {
        return ResponseEntity.ok(new ResponseDTO().success(reportService.unlockUser(request.id, request.message)));
    }


    //ở phần feedback reportedId sẽ là logId (hành động của admin)
    //LƯU Ý: Khi người dùng bị khóa thì nên để cái này authHeader null
    @PostMapping("/feedback")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> feedback(@RequestBody ReportRequest reportRequest) {
        return ResponseEntity.ok(new ResponseDTO().success(reportService.createFeedback(reportRequest)));
    }

    @GetMapping("/log/{logId}")
    public ResponseEntity<?> getLog(@PathVariable String logId) {
        return ResponseEntity.ok(new ResponseDTO().success(reportService.getLog(logId)));
    }

    public record AdminRequest(String id, String message) {}
}
