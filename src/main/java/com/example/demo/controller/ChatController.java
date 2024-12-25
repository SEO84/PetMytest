package com.example.demo.controller;

import com.example.demo.dto.MessageDTO;
import com.example.demo.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    /**
     * 메시지 전송
     */
    @PostMapping("/messages")
    public ResponseEntity<MessageDTO> sendMessage(@RequestBody MessageDTO dto) {
        MessageDTO sent = chatService.sendMessage(dto);
        return ResponseEntity.ok(sent);
    }

    /**
     * 메시지 읽음 처리
     */
    @PutMapping("/messages/{messageId}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Integer messageId) {
        chatService.markAsRead(messageId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 메시지 상세 조회
     */
    @GetMapping("/messages/{messageId}")
    public ResponseEntity<MessageDTO> getMessage(@PathVariable Integer messageId) {
        MessageDTO found = chatService.getMessage(messageId);
        if (found == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(found);
    }
}
