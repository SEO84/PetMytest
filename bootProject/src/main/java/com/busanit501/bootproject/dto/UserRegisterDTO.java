package com.busanit501.bootproject.dto;

import com.busanit501.bootproject.enums.Gender;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserRegisterDTO {

    @NotBlank(message = "이메일은 필수 입력 항목입니다.")
    @Email(message = "유효한 이메일 형식을 입력해주세요.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    private String password;

    @NotBlank(message = "이름은 필수 입력 항목입니다.")
    private String name;

    @NotNull(message = "나이는 필수 입력 항목입니다.")
    @Min(value = 0, message = "나이는 0 이상이어야 합니다.")
    private Integer age;

    @NotNull(message = "성별은 필수 입력 항목입니다.")
    private Gender gender;

    @NotBlank(message = "주소는 필수 입력 항목입니다.")
    private String address;

    @NotBlank(message = "전화번호는 필수 입력 항목입니다.")
    @Pattern(regexp = "^\\d{8,11}$", message = "전화번호는 10~11자리의 숫자여야 합니다.")
    private String phoneNumber;

    // 반려동물 정보
    @NotBlank(message = "반려동물 이름은 필수 입력 항목입니다.")
    private String petName;

    @NotBlank(message = "반려동물 종류는 필수 입력 항목입니다.")
    private String petType;

    @NotNull(message = "반려동물 나이는 필수 입력 항목입니다.")
    @Min(value = 0, message = "반려동물 나이는 0 이상이어야 합니다.")
    private Integer petAge;

    @NotNull(message = "반려동물 성별은 필수 입력 항목입니다.")
    private Gender petGender;

    @NotNull(message = "반려동물 무게는 필수 입력 항목입니다.")
    @DecimalMin(value = "0.0", inclusive = true, message = "반려동물 무게는 0 이상이어야 합니다.")
    private Double petWeight; // @Min 대신 @DecimalMin 사용

    @NotBlank(message = "반려동물 성격은 필수 입력 항목입니다.")
    private String petPersonality;
}
