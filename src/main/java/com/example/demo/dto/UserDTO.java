package com.example.demo.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private Integer userId;
    private String email;
    // 보안상, 비밀번호는 Response에 노출하지 않는 편이 좋음
    private String password;
    private String name;
    private Integer age;
    private String gender;    // "Male", "Female", "Other"
    private String address;
    private String profilePicture;
    private String phoneNumber;
    private Boolean isVerified;
    private String createdAt; // 문자열(yyyy-MM-dd HH:mm:ss) 형태로 변환해서 보낼 수도 있음

    /**
     * 예시로 ModelMapper를 이용하면
     * Entity(Users) -> UserDTO 변환 가능
     */
}
