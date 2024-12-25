package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "walk_records")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalkRecords {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Integer recordId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    private LocalDateTime startTime; // 산책 시작 시간
    private LocalDateTime endTime;   // 산책 종료 시간
    private Integer duration;        // 산책 지속 시간(분)
}
