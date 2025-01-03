// UserRegisterDTO.java
package com.busanit501.bootproject.dto;

import com.busanit501.bootproject.enums.Gender;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserRegisterDTO {

    // ====== 기존 유저 정보 ======

    @NotBlank(message = "이메일은 필수 입력 항목입니다.")
    @Email(message = "유효한 이메일 형식을 입력해주세요.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    @Size(min = 4, message = "비밀번호는 최소 4자 이상이어야 합니다.")
    private String password;

    @NotBlank(message = "이름은 필수 입력 항목입니다.")
    private String name;

    @Min(value = 0, message = "나이는 0 이상이어야 합니다.")
    private Integer age;

    @NotNull(message = "성별은 필수 입력 항목입니다.")
    private Gender gender;  // ENUM 'MALE' or 'FEMALE'

    @NotBlank(message = "주소는 필수 입력 항목입니다.")
    private String address;

    @NotBlank(message = "전화번호는 필수 입력 항목입니다.")
    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "유효한 전화번호 형식을 입력해주세요.")
    private String phoneNumber;

    // ====== 새로 추가된 펫 정보 ======

    @NotBlank(message = "펫 이름은 필수 입력 항목입니다.")
    private String petName;

    @NotBlank(message = "펫 종류는 필수 입력 항목입니다.")
    private String petType;      // 예: "Beagle", "Pome" 등

    @Min(value = 0, message = "펫 나이는 0 이상이어야 합니다.")
    private Integer petAge;

    @NotNull(message = "펫 성별은 필수 입력 항목입니다.")
    private Gender petGender;    // ENUM 'MALE' or 'FEMALE'

    @PositiveOrZero(message = "펫 몸무게는 0 이상이어야 합니다.")
    private Float petWeight;

    private String petPersonality;
}
