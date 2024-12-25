package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "walk_schedules")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalkSchedules {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Integer scheduleId;

    // 사용자 (외래 키)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    private LocalDate walkDate;  // 산책 날짜
    private LocalTime walkTime;  // 산책 시간

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private WalkStatus status = WalkStatus.Scheduled;

    public enum WalkStatus {
        Scheduled, Completed, Cancelled
    }
}
