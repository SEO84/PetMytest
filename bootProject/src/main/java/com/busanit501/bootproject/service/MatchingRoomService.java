package com.busanit501.bootproject.service;

import com.busanit501.bootproject.dto.MatchingRoomCreateRequest;
import com.busanit501.bootproject.domain.MatchingRoom;
import com.busanit501.bootproject.domain.RoomParticipant;
import com.busanit501.bootproject.domain.RoomParticipant.ParticipantStatus;
import com.busanit501.bootproject.domain.RoomParticipantId;
import com.busanit501.bootproject.repository.MatchingRoomRepository;
import com.busanit501.bootproject.repository.RoomParticipantRepository;
import com.busanit501.bootproject.domain.Users;
import com.busanit501.bootproject.repository.UserRepository; // 가정
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MatchingRoomService {

    private final MatchingRoomRepository matchingRoomRepository;
    private final RoomParticipantRepository roomParticipantRepository;
    private final UserRepository userRepository; // 가정: UserRepository에서 User를 조회한다고 가정

    /**
     * 매칭방 생성
     */
    public MatchingRoom createRoom(MatchingRoomCreateRequest request) {
        // 방장 정보 가져오기
        Users host = userRepository.findById(request.getHostId())
                .orElseThrow(() -> new RuntimeException("방장 유저가 존재하지 않습니다."));

        // 매칭방 엔티티 생성
        MatchingRoom room = MatchingRoom.builder()
                .host(host)
                .title(request.getTitle())
                .description(request.getDescription())
                .place(request.getPlace())
                .meetingDate(request.getMeetingDate())
                .meetingTime(request.getMeetingTime())
                .maxParticipants(request.getMaxParticipants())
                .build();

        // 매칭방 저장
        MatchingRoom saved = matchingRoomRepository.save(room);

        // 방장은 바로 Accepted로 참가자 테이블에 등록
        RoomParticipant hostParticipant = RoomParticipant.builder()
                .roomId(saved.getRoomId())
                .userId(request.getHostId())
                .petId(request.getPetId())
                .status(ParticipantStatus.Accepted)
                .build();

        roomParticipantRepository.save(hostParticipant);

        return saved;
    }

    /**
     * 매칭방 리스트 조회
     */
    @Transactional(readOnly = true)
    public List<MatchingRoom> getRoomList() {
        return matchingRoomRepository.findAll();
    }

    /**
     * 매칭방 상세 조회
     */
    @Transactional(readOnly = true)
    public MatchingRoom getRoomDetail(Long roomId) {
        return matchingRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("해당 매칭방이 존재하지 않습니다."));
    }

    /**
     * 매칭방 참가 신청
     */
    public void applyRoom(Long roomId, Long userId, Long petId) {
        // 1) 이미 참가 신청한 적이 있는지 확인
        RoomParticipantId rpId = new RoomParticipantId(roomId, userId);
        if (roomParticipantRepository.findById(rpId).isPresent()) {
            throw new RuntimeException("이미 참가 신청을 하셨습니다.");
        }

        // 2) 매칭방 존재 여부 확인
        MatchingRoom room = matchingRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("해당 매칭방이 존재하지 않습니다."));

        // 3) 현재 참가자 수가 최대 참가자 수 미만인지 확인
        if (room.getCurrentParticipants() >= room.getMaxParticipants()) {
            throw new RuntimeException("이미 최대 인원에 도달했습니다.");
        }

        // 4) 참가자 정보 저장 (상태는 Pending)
        RoomParticipant participant = RoomParticipant.builder()
                .roomId(roomId)
                .userId(userId)
                .petId(petId)
                .status(ParticipantStatus.Pending)
                .build();
        roomParticipantRepository.save(participant);
    }

    /**
     * 참가 신청 승인
     */
    public void approveParticipant(Long roomId, Long targetUserId, Long hostUserId) {
        // 매칭방 조회
        MatchingRoom room = matchingRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("매칭방이 존재하지 않습니다."));

        // 방장인지 확인
        if (!room.getHost().getUserId().equals(hostUserId)) {
            throw new RuntimeException("방장만 승인할 수 있습니다.");
        }

        // 참가자 조회
        RoomParticipantId rpId = new RoomParticipantId(roomId, targetUserId);
        RoomParticipant participant = roomParticipantRepository.findById(rpId)
                .orElseThrow(() -> new RuntimeException("참가 신청이 존재하지 않습니다."));

        // 승인 처리
        participant.setStatus(ParticipantStatus.Accepted);
        roomParticipantRepository.save(participant);

        // 매칭방 현재 참가자 수 +1
        room.setCurrentParticipants(room.getCurrentParticipants() + 1);

        // 만약 현재 참가자가 maxParticipants에 도달하면 상태를 Closed로 변경
        if (room.getCurrentParticipants().equals(room.getMaxParticipants())) {
            room.setStatus(MatchingRoom.RoomStatus.Closed);
        }

        matchingRoomRepository.save(room);
    }

    /**
     * 참가 신청 거절
     */
    public void rejectParticipant(Long roomId, Long targetUserId, Long hostUserId) {
        // 매칭방 조회
        MatchingRoom room = matchingRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("매칭방이 존재하지 않습니다."));

        // 방장인지 확인
        if (!room.getHost().getUserId().equals(hostUserId)) {
            throw new RuntimeException("방장만 거절할 수 있습니다.");
        }

        // 참가자 조회
        RoomParticipantId rpId = new RoomParticipantId(roomId, targetUserId);
        RoomParticipant participant = roomParticipantRepository.findById(rpId)
                .orElseThrow(() -> new RuntimeException("참가 신청이 존재하지 않습니다."));

        // 거절 처리
        participant.setStatus(ParticipantStatus.Rejected);
        roomParticipantRepository.save(participant);
    }
}
