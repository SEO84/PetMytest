package com.busanit501.bootproject.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "matching_rooms")
public class MatchingRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Integer roomId;

    // 방장
    @ManyToOne
    @JoinColumn(name = "host_id")
    private User host;

    @Column(nullable = false)
    private String title;

    @Lob
    private String description;

    private String place;

    private LocalDate meetingDate;

    private LocalTime meetingTime;

    private Integer maxParticipants = 10;

    private Integer currentParticipants = 1;

    @Enumerated(EnumType.STRING)
    private RoomStatus status = RoomStatus.Open; // ENUM('Open','Closed')

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    public enum RoomStatus {
        Open, Closed
    }
}
