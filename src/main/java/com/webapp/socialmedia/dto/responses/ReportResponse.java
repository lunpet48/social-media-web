package com.webapp.socialmedia.dto.responses;

import com.webapp.socialmedia.entity.Report;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportResponse {
    private List<Report> reports;
    private int totalPages;
}
