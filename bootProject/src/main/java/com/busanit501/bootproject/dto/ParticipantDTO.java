package com.busanit501.bootproject.dto;

import lombok.Data;

@Data
public class ParticipantDTO {
    private String userName;      // 사용자 이름
    private String profileImagePath; // 프로필 이미지 URL (필요 시)
}

