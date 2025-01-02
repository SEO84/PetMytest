// PetDTO.java
package com.busanit501.bootproject.dto;

import com.busanit501.bootproject.enums.Gender;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class PetDTO {

    @NotBlank(message = "펫 이름은 필수 입력 항목입니다.")
    private String name;

    @NotBlank(message = "펫 종류는 필수 입력 항목입니다.")
    private String type;

    @Min(value = 0, message = "펫 나이는 0 이상이어야 합니다.")
    private Integer age;

    @NotNull(message = "펫 성별은 필수 입력 항목입니다.")
    private Gender petGender; // Enum 타입으로 변경

    @PositiveOrZero(message = "펫 몸무게는 0 이상이어야 합니다.")
    private Float weight;

    private String personality;

    private String profilePicture; // 프로필 사진 경로(필요 시)
}
