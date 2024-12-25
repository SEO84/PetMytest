package com.example.demo.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDTO {

    private Integer reviewId;
    private Integer reviewerId;
    private Integer reviewedId;
    private Integer rating;   // 1~5
    private String comment;
    private String createdAt; // yyyy-MM-dd HH:mm:ss
}
