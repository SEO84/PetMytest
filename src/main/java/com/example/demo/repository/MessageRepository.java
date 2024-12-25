package com.example.demo.repository;

import com.example.demo.domain.Messages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Messages, Integer> {
    // 1:1 채팅 등 조회
    // List<Messages> findBySenderUserIdAndReceiverUserIdOrderBySentAtAsc(Integer senderId, Integer receiverId);
}
