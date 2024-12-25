package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "room_participants")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(RoomParticipantsId.class)
public class RoomParticipants {

    @Id
    @Column(name = "room_id")
    private Integer roomId;

    @Id
    @Column(name = "user_id")
    private Integer userId;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ParticipantStatus status = ParticipantStatus.Pending;

    @Builder.Default
    private LocalDateTime joinedAt = LocalDateTime.now();

    public enum ParticipantStatus {
        Pending, Accepted, Rejected
    }
}

