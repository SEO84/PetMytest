package com.example.demo.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeDTO {

    private Integer likeId;
    private Integer postId;
    private Integer userId;
    private String createdAt; // yyyy-MM-dd HH:mm:ss
}
