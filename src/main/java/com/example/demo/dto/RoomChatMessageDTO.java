package com.example.demo.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomChatMessageDTO {
    private Integer chatMessageId;
    private Integer roomId;
    private Integer senderId;
    private String content;
    private String sentAt; // yyyy-MM-dd HH:mm:ss 형식 등
}

