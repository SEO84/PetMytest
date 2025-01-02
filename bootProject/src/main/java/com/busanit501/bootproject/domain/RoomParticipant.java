// RoomParticipant.java
package com.busanit501.bootproject.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "room_participants")
public class RoomParticipant {

    @EmbeddedId
    private RoomParticipantId id = new RoomParticipantId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("roomId")
    @JoinColumn(name = "room_id")
    private MatchingRoom matchingRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("petId") // petId 매핑
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ParticipantStatus status;

    public enum ParticipantStatus {
        Pending,
        Accepted,
        Rejected
    }

    // 편의 메서드
    public void setUser(User user) {
        this.user = user;
        this.id.setUserId(user.getUserId());
    }

    public void setMatchingRoom(MatchingRoom matchingRoom) {
        this.matchingRoom = matchingRoom;
        this.id.setRoomId(matchingRoom.getRoomId());
    }

    public void setPet(Pet pet) {
        this.pet = pet;
        this.id.setPetId(pet.getPetId());
    }
}
