package com.example.demo.repository;

import com.example.demo.domain.MatchingRooms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchingRoomRepository extends JpaRepository<MatchingRooms, Integer> {
    // 예) 특정 상태의 방만 찾기
    // List<MatchingRooms> findByStatus(MatchingRooms.RoomStatus status);
}
