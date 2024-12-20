package com.busanit501.bootproject.repository;

import com.busanit501.bootproject.domain.RoomParticipant;
import com.busanit501.bootproject.domain.RoomParticipantId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomParticipantRepository extends JpaRepository<RoomParticipant, RoomParticipantId> {
    List<RoomParticipant> findByRoomId(Long roomId);
    List<RoomParticipant> findByUserId(Long userId);
    // 필요에 따라 추가 메서드 정의
}
