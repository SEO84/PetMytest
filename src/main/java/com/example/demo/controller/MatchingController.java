package com.example.demo.controller;

import com.example.demo.domain.RoomParticipants;
import com.example.demo.dto.MatchingRoomDTO;
import com.example.demo.dto.RoomParticipantDTO;
import com.example.demo.service.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/matching")
@RequiredArgsConstructor
public class MatchingController {

    private final MatchingService matchingService;

    // 방 생성
    @PostMapping("/rooms")
    public ResponseEntity<MatchingRoomDTO> createRoom(@RequestBody MatchingRoomDTO dto) {
        MatchingRoomDTO created = matchingService.createRoom(dto);
        return ResponseEntity.ok(created);
    }

    // 방 닫기
    @PutMapping("/rooms/{roomId}/close")
    public ResponseEntity<MatchingRoomDTO> closeRoom(@PathVariable Integer roomId) {
        MatchingRoomDTO closed = matchingService.closeRoom(roomId);
        return ResponseEntity.ok(closed);
    }

    // 참가 신청
    @PostMapping("/rooms/{roomId}/join")
    public ResponseEntity<RoomParticipantDTO> joinRoom(@PathVariable Integer roomId,
                                                       @RequestParam Integer userId) {
        RoomParticipantDTO result = matchingService.requestJoin(roomId, userId);
        return ResponseEntity.ok(result);
    }

    // 승인 / 거절
    @PutMapping("/rooms/{roomId}/participants/{userId}")
    public ResponseEntity<RoomParticipantDTO> updateParticipantStatus(
            @PathVariable Integer roomId,
            @PathVariable Integer userId,
            @RequestParam RoomParticipants.ParticipantStatus status
    ) {
        RoomParticipantDTO updated = matchingService.updateParticipantStatus(roomId, userId, status);
        return ResponseEntity.ok(updated);
    }
}

