package com.busanit501.bootproject.repository;

import com.busanit501.bootproject.domain.MatchingRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchingRoomRepository extends JpaRepository<MatchingRoom, Long> {
    // 필요하면 커스텀 쿼리 추가
}
