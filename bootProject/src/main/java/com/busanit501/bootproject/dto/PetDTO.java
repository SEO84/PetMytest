package com.busanit501.bootproject.dto;

import lombok.Data;

@Data
public class PetDTO {
    private Integer petId;
    private Integer userId;       // 소유자 식별자 (필요 시)
    private String name;          // 반려동물 이름
    private String type;          // 예: "Beagle", "Pome" 등
    private Integer age;          // 나이
    private String gender;        // "Male" or "Female"
    private Float weight;         // 몸무게
    private String personality;   // 성격 정보
    private String profilePicture; // 프로필 사진 경로(필요 시)
}
