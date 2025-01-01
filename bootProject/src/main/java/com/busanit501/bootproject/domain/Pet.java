package com.busanit501.bootproject.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "pets")
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pet_id")
    private Integer petId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;  // 소유자

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 50)
    private String type; // 예: "Beagle", "Pome", etc.

    private Integer age;

    @Enumerated(EnumType.STRING)
    private Gender gender; // ENUM('Male','Female')

    @Lob
    private String personality;

    private Float weight;

    private String profilePicture;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    public enum Gender {
        Male, Female
    }
}
