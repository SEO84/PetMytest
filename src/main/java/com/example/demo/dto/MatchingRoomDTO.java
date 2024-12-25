package com.example.demo.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchingRoomDTO {

    private Integer roomId;
    private Integer hostId;
    private String title;
    private String description;

    // 최대 참가인원 (1~10)
    private Integer maxParticipants;

    // 현재 참가인원
    private Integer currentParticipants;

    private String status;      // "Open", "Closed"
    private String createdAt;
}

