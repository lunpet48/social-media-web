package com.webapp.socialmedia.dto.requests;

import com.webapp.socialmedia.enums.ReportType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportRequest {
    private ReportType reportType;
    private String reportedId;
    private String describe;
}
