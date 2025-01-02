package com.busanit501.bootproject.repository;

import com.busanit501.bootproject.domain.MatchingRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface MatchingRoomRepository extends JpaRepository<MatchingRoom, Integer> {

    Optional<MatchingRoom> findByTitle(String title);

    // 예시: 특정 날짜 이후의 매칭 방을 조회
    Page<MatchingRoom> findByMeetingDateAfter(LocalDate date, Pageable pageable);

    // 필요한 추가 메서드 정의
}
