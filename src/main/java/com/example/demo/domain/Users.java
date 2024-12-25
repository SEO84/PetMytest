package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(nullable = false, unique = true, length = 255)
    private String email; // 로그인 ID

    @Column(nullable = false, length = 255)
    private String password; // 암호화된 비밀번호

    @Column(nullable = false, length = 100)
    private String name; // 사용자 이름

    private Integer age;  // 사용자 나이

    @Enumerated(EnumType.STRING)
    private Gender gender; // 'Male', 'Female', 'Other'

    private String address; // 주소 (동까지)

    private String profilePicture; // 프로필 사진 URL

    private String phoneNumber; // 전화번호

    @Builder.Default
    private Boolean isVerified = false; // 계정 인증 여부

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now(); // 계정 생성 시간

    // enum 정의: 성별(Male, Female, Other)
    public enum Gender {
        Male, Female, Other
    }

    // (예시) 1:N 관계 설정:
    // @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    // private List<Pets> petsList = new ArrayList<>();
}
