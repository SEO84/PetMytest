package com.busanit501.bootproject.repository;

import com.busanit501.bootproject.domain.RoomParticipant;
import com.busanit501.bootproject.domain.RoomParticipantId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomParticipantRepository extends JpaRepository<RoomParticipant, RoomParticipantId> {
    List<RoomParticipant> findByRoomId(Integer roomId);
}
