package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Entity
@Table(name = "room_chat_messages")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomChatMessages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_message_id")
    private Integer chatMessageId;

    // 그룹 채팅이므로 room_id 외래키
    private Integer roomId;

    // 발신 사용자 정보
    private Integer senderId;

    @Lob
    private String content;

    @Builder.Default
    private LocalDateTime sentAt = LocalDateTime.now();
}

