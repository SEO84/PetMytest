package com.busanit501.bootproject.domain;

import com.busanit501.bootproject.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pets extends BaseEntity { // BaseEntity 상속

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pet_id")
    private Long petId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users owner; // 반려동물 주인 (User와 매핑)

    @Column(nullable = false)
    private String name; // 반려동물 이름

    @Column(nullable = false)
    private String type; // 반려동물 종류 (예: 개, 고양이)

    private Integer age; // 반려동물 나이

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('Male', 'Female')")
    private Gender gender; // 반려동물 성별 (Male/Female)

    @Column(columnDefinition = "TEXT")
    private String personality; // 반려동물 성격

    private Float weight; // 반려동물 몸무게

    private String profilePicture; // 반려동물 프로필 사진 경로

    public enum Gender {
        Male,
        Female
    }
}
