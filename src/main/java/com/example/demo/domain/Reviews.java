package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reviews {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Integer reviewId;

    // 리뷰 작성자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id")
    private Users reviewer;

    // 리뷰 대상(사용자)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_id")
    private Users reviewed;

    @Column(nullable = false)
    private Integer rating; // 1 ~ 5점

    @Lob
    private String comment; // 리뷰 내용

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now(); // 리뷰 작성 시간
}
