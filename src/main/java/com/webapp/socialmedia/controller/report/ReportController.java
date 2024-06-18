package com.webapp.socialmedia.controller.report;

import com.webapp.socialmedia.dto.requests.ReportRequest;
import com.webapp.socialmedia.dto.responses.ResponseDTO;
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
    @PostMapping
    public ResponseEntity<?> reportPost(@RequestBody ReportRequest reportRequest) {
        return ResponseEntity.ok(new ResponseDTO().success(reportService.createReport(reportRequest)));
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

    @DeleteMapping("/delete-post/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deletePost(@PathVariable String postId) {
        reportService.deletePost(postId);
    }

    @PostMapping("/recovery-post/{postId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void recoveryPost(@PathVariable String postId) {
        reportService.recoveryPost(postId);
    }

}
