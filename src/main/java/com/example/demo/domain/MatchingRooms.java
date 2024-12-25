package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "matching_rooms")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchingRooms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Integer roomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id")
    private Users host;

    @Column(nullable = false)
    private String title;

    @Lob
    private String description;

    // 최대 참가 인원 (디폴트 10명)
    @Builder.Default
    private Integer maxParticipants = 10;

    @Builder.Default
    private Integer currentParticipants = 1; // 방장 1명 포함

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private RoomStatus status = RoomStatus.Open; // Open or Closed

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum RoomStatus {
        Open, Closed
    }
}

