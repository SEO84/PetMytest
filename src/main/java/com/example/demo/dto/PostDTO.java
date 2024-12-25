package com.example.demo.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDTO {

    private Integer postId;
    private Integer userId;
    private String category; // "FreeBoard", "EmergencyHospital", "UsedItems", "WalkRequest"
    private String title;
    private String content;
    private String createdAt;
    private String updatedAt;
}
