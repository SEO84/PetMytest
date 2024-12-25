package com.example.demo.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalkScheduleDTO {

    private Integer scheduleId;
    private Integer userId;
    private String walkDate;   // yyyy-MM-dd
    private String walkTime;   // HH:mm:ss
    private String status;     // "Scheduled", "Completed", "Cancelled"
}
