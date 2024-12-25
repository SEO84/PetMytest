package com.example.demo.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserReportDTO {

    private Integer reportId;
    private Integer reporterId; // 신고자
    private Integer reportedId; // 신고 대상
    private String reason;
    private String status;      // "Pending", "Reviewed", "Resolved"
    private String reportedAt;  // yyyy-MM-dd HH:mm:ss
}
