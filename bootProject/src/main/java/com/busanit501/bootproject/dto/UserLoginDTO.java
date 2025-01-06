package com.busanit501.bootproject.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 사용자 로그인 요청을 위한 DTO 클래스
 */
@Data
public class UserLoginDTO {

    /**
     * 사용자 이메일 (필수 입력, 유효한 이메일 형식)
     */
    @NotBlank(message = "이메일은 필수 입력 항목입니다.")
    @Email(message = "유효한 이메일 형식을 입력해주세요.")
    private String email;

    /**
     * 사용자 비밀번호 (필수 입력)
     */
    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    private String password;
}
