package com.busanit501.bootproject.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchingRoomCreateRequest {
    private String title;
    private String description;
    private String place;
    private LocalDate meetingDate;
    private LocalTime meetingTime;
    private Integer maxParticipants;
    private Long hostId;  // 방장 유저 ID
    private Long petId;   // 방장이 본인 반려동물도 같이 등록한다면
}
