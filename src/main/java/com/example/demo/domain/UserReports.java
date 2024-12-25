package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_reports")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserReports {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Integer reportId;

    // 신고자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id")
    private Users reporter;

    // 신고 대상
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_id")
    private Users reported;

    @Lob
    private String reason; // 신고 사유

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ReportStatus status = ReportStatus.Pending;

    @Builder.Default
    private LocalDateTime reportedAt = LocalDateTime.now(); // 신고 시간

    public enum ReportStatus {
        Pending, Reviewed, Resolved
    }
}
