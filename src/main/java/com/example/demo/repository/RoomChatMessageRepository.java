package com.example.demo.repository;

import com.example.demo.domain.RoomChatMessages;
import com.example.demo.domain.RoomParticipants;
import com.example.demo.domain.RoomParticipantsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomChatMessageRepository extends JpaRepository<RoomChatMessages, Integer> {

    /**
     * 특정 방(roomId) 채팅 목록을 sentAt(보낸 시각) 오름차순으로 조회.
     */
    List<RoomChatMessages> findByRoomIdOrderBySentAtAsc(Integer roomId);

    // 필요 시 커스텀 쿼리 추가 가능
    // ex) 최근 n개만 조회:
    // @Query(value = "SELECT r FROM RoomChatMessages r WHERE r.roomId = :roomId ORDER BY r.sentAt DESC LIMIT :count",
    //        nativeQuery = true)
    // List<RoomChatMessages> findTopNMessages(@Param("roomId") Integer roomId, @Param("count") int count);
}
