package com.example.demo.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetDTO {

    private Integer petId;
    private Integer userId; // 주인
    private String name;    // 반려동물 이름
    private String type;    // 종류(개, 고양이 등)
    private Integer age;
    private String gender;  // "Male", "Female"
    private String personality;
    private Float weight;
    private String profilePicture;
}
