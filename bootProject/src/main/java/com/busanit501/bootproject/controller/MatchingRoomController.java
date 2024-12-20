package com.busanit501.bootproject.controller;

import com.busanit501.bootproject.dto.MatchingRoomCreateRequest;
import com.busanit501.bootproject.domain.MatchingRoom;
import com.busanit501.bootproject.service.MatchingRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matching")
@RequiredArgsConstructor
public class MatchingRoomController {

    private final MatchingRoomService matchingRoomService;

    /**
     * 매칭방 생성
     * POST /api/matching/rooms
     */
    @PostMapping("/rooms")
    public MatchingRoom createRoom(@RequestBody MatchingRoomCreateRequest request) {
        return matchingRoomService.createRoom(request);
    }

    /**
     * 매칭방 리스트 조회
     * GET /api/matching/rooms
     */
    @GetMapping("/list")
    public List<MatchingRoom> getRoomList() {
        return matchingRoomService.getRoomList();
    }

    /**
     * 매칭방 상세 조회
     * GET /api/matching/rooms/{roomId}
     */
    @GetMapping("/rooms/{roomId}")
    public MatchingRoom getRoomDetail(@PathVariable Long roomId) {
        return matchingRoomService.getRoomDetail(roomId);
    }

    /**
     * 매칭방 참가 신청
     * POST /api/matching/rooms/{roomId}/apply
     */
    @PostMapping("/rooms/{roomId}/apply")
    public String applyRoom(
            @PathVariable Long roomId,
            @RequestParam Long userId,
            @RequestParam(required = false) Long petId
    ) {
        matchingRoomService.applyRoom(roomId, userId, petId);
        return "매칭방 참가 신청 완료";
    }

    /**
     * 참가 신청 승인
     * POST /api/matching/rooms/{roomId}/approve
     */
    @PostMapping("/rooms/{roomId}/approve")
    public String approveParticipant(
            @PathVariable Long roomId,
            @RequestParam Long targetUserId,
            @RequestParam Long hostUserId
    ) {
        matchingRoomService.approveParticipant(roomId, targetUserId, hostUserId);
        return "승인 완료";
    }

    /**
     * 참가 신청 거절
     * POST /api/matching/rooms/{roomId}/reject
     */
    @PostMapping("/rooms/{roomId}/reject")
    public String rejectParticipant(
            @PathVariable Long roomId,
            @RequestParam Long targetUserId,
            @RequestParam Long hostUserId
    ) {
        matchingRoomService.rejectParticipant(roomId, targetUserId, hostUserId);
        return "거절 완료";
    }
}
