package com.busanit501.bootproject.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class MatchingRoomDTO {
    private String title;
    private String description;
    private String place;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate meetingDate;

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime meetingTime;

    private Integer maxParticipants;
}
