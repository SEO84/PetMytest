package com.busanit501.bootproject.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "room_participants")
@IdClass(RoomParticipantId.class)
public class RoomParticipant {

    @Id
    @Column(name = "room_id")
    private Integer roomId;

    @Id
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "pet_id")
    private Integer petId;

    @Enumerated(EnumType.STRING)
    private ParticipantStatus status = ParticipantStatus.Pending;

    private LocalDateTime createdAt = LocalDateTime.now();

    public enum ParticipantStatus {
        Pending, Accepted, Rejected
    }
}
