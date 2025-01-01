package com.busanit501.bootproject.dto;

import lombok.Data;

@Data
public class UserRegisterDTO {
    // ====== 기존 유저 정보 ======
    private String email;
    private String password;
    private String name;
    private Integer age;
    private String gender;  // "Male" or "Female"
    private String address;
    private String phoneNumber;

    // ====== 새로 추가된 펫 정보 ======
    private String petName;
    private String petType;      // 예: "Beagle", "Pome" 등
    private Integer petAge;
    private String petGender;    // "Male" or "Female"
    private Float petWeight;
    private String petPersonality;
}
