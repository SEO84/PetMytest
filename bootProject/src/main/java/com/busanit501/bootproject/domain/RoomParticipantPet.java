package com.busanit501.bootproject.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "room_participant_pets")
public class RoomParticipantPet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_participant_id")
    private RoomParticipant roomParticipant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    private Pet pet;
}
