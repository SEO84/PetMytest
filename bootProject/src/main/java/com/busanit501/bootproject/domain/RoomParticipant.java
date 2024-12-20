package com.busanit501.bootproject.domain;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "room_participants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(RoomParticipantId.class)
public class RoomParticipant {

    @Id
    @Column(name = "room_id")
    private Long roomId; // 매칭방 ID

    @Id
    @Column(name = "user_id")
    private Long userId; // 참가자 사용자 ID

    // 해당 참가자가 어떤 펫으로 참여하는지
    @Column(name = "pet_id")
    private Long petId;

    @Enumerated(EnumType.STRING)
    private ParticipantStatus status; // Pending, Accepted, Rejected

    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = ParticipantStatus.Pending;
        }
    }

    public enum ParticipantStatus {
        Pending,
        Accepted,
        Rejected
    }
}
