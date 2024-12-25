package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pets")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pets {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pet_id")
    private Integer petId;

    // 주인 사용자 ID (외래 키)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @Column(nullable = false, length = 100)
    private String name; // 반려동물 이름

    @Column(nullable = false, length = 50)
    private String type; // 반려동물 종류 (개, 고양이 등)

    private Integer age; // 반려동물 나이

    @Enumerated(EnumType.STRING)
    private Gender gender; // 'Male', 'Female'

    @Lob
    private String personality; // 반려동물 성격

    private Float weight; // 반려동물 몸무게

    private String profilePicture; // 반려동물 프로필 사진 URL

    // enum 정의
    public enum Gender {
        Male, Female
    }
}
