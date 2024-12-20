package com.busanit501.bootproject.domain;

import com.busanit501.bootproject.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Users extends BaseEntity { // BaseEntity 상속

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(unique = true, nullable = false)
    private String email; // 사용자 이메일 (로그인 ID로 사용)

    @Column(nullable = false)
    private String password; // 암호화된 비밀번호

    @Column(nullable = false)
    private String name; // 사용자 이름

    private Integer age; // 나이

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('Male', 'Female')")
    private Gender gender; // 성별 (Male/Female)

    private String address; // 주소

    private String profilePicture; // 프로필 사진 경로

    private String phoneNumber; // 전화번호

    @Column(nullable = false)
    private Boolean isVerified; // 계정 인증 여부 (기본값: false)

    @PrePersist
    public void onCreate() {
        this.isVerified = false; // 기본값
    }

    public enum Gender {
        Male,
        Female
    }
}
