package com.example.demo.controller;
import com.example.demo.domain.RoomParticipants;
import com.example.demo.dto.MatchingRoomDTO;
import com.example.demo.dto.RoomChatMessageDTO;
import com.example.demo.dto.RoomParticipantDTO;
import com.example.demo.service.MatchingService;
import com.example.demo.service.RoomChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/roomchat")
@RequiredArgsConstructor
public class RoomChatController {

    private final RoomChatService roomChatService;

    /**
     * 채팅 보내기
     */
    @PostMapping("/messages")
    public ResponseEntity<RoomChatMessageDTO> sendMessage(@RequestBody RoomChatMessageDTO dto) {
        RoomChatMessageDTO saved = roomChatService.sendMessage(dto);
        return ResponseEntity.ok(saved);
    }

    /**
     * 방 채팅 목록 조회
     */
    @GetMapping("/messages/{roomId}")
    public ResponseEntity<List<RoomChatMessageDTO>> getMessages(@PathVariable Integer roomId) {
        List<RoomChatMessageDTO> list = roomChatService.getChatMessages(roomId);
        return ResponseEntity.ok(list);
    }
}

