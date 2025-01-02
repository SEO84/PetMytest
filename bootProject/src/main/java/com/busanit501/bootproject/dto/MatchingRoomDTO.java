package com.busanit501.bootproject.dto;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class MatchingRoomDTO {

    @NotBlank(message = "제목은 필수 입력 항목입니다.")
    private String title;

    @NotBlank(message = "설명은 필수 입력 항목입니다.")
    private String description;

    @NotBlank(message = "장소는 필수 입력 항목입니다.")
    private String place;

    @NotNull(message = "날짜는 필수 입력 항목입니다.")
    @FutureOrPresent(message = "날짜는 현재 또는 미래여야 합니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate meetingDate;

    @NotNull(message = "시간은 필수 입력 항목입니다.")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime meetingTime;

    @NotNull(message = "최대 인원은 필수 입력 항목입니다.")
    @Min(value = 1, message = "최소 인원은 1명이어야 합니다.")
    @Max(value = 100, message = "최대 인원은 100명을 초과할 수 없습니다.")
    private Integer maxParticipants;

    @NotNull(message = "방장의 반려동물을 선택해주세요.")
    private Integer hostPetId;

    // 추가 펫 선택을 위한 필드
    private List<Integer> additionalPetIds;
}
