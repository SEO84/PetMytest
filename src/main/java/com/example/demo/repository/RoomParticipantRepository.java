package com.example.demo.repository;

import com.example.demo.domain.RoomParticipants;
import com.example.demo.domain.RoomParticipantsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomParticipantRepository extends JpaRepository<RoomParticipants, RoomParticipantsId> {
    // 필요 시 커스텀 쿼리:
    // List<RoomParticipants> findByRoomId(Integer roomId);
    // List<RoomParticipants> findByUserId(Integer userId);
}
