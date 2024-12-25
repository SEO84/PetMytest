package com.example.demo.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDTO {

    private Integer messageId;
    private Integer senderId;
    private Integer receiverId;
    private String content;
    private String sentAt;  // (yyyy-MM-dd HH:mm:ss)
    private Boolean isRead;
}
