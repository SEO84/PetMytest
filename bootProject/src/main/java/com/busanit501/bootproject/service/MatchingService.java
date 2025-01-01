package com.busanit501.bootproject.service;

import com.busanit501.bootproject.domain.MatchingRoom;
import com.busanit501.bootproject.domain.RoomParticipant;
import com.busanit501.bootproject.domain.User;
import com.busanit501.bootproject.dto.MatchingRoomDTO;
import com.busanit501.bootproject.repository.MatchingRoomRepository;
import com.busanit501.bootproject.repository.RoomParticipantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MatchingService {

    private final MatchingRoomRepository matchingRoomRepository;
    private final RoomParticipantRepository roomParticipantRepository;

    public MatchingService(MatchingRoomRepository matchingRoomRepository,
                           RoomParticipantRepository roomParticipantRepository) {
        this.matchingRoomRepository = matchingRoomRepository;
        this.roomParticipantRepository = roomParticipantRepository;
    }

    // 매칭방 생성
    @Transactional
    public MatchingRoom createRoom(User host, MatchingRoomDTO dto) {
        MatchingRoom room = new MatchingRoom();
        room.setHost(host);
        room.setTitle(dto.getTitle());
        room.setDescription(dto.getDescription());
        room.setPlace(dto.getPlace());
        room.setMeetingDate(dto.getMeetingDate());
        room.setMeetingTime(dto.getMeetingTime());
        room.setMaxParticipants(
                dto.getMaxParticipants() == null ? 10 : dto.getMaxParticipants()
        );
        room.setCurrentParticipants(1);
        room.setCreatedAt(LocalDateTime.now());
        room.setUpdatedAt(LocalDateTime.now());

        MatchingRoom savedRoom = matchingRoomRepository.save(room);

        // 방장도 참여자 테이블에 기본 등록(여기선 Pending이나 Accepted로 넣어도 됨)
        RoomParticipant rp = new RoomParticipant();
        rp.setRoomId(savedRoom.getRoomId());
        rp.setUserId(host.getUserId());
        rp.setPetId(null); // 방장이 가진 반려견 id를 적어도 됨
        rp.setCreatedAt(LocalDateTime.now());
        // rp.setStatus(RoomParticipant.ParticipantStatus.Accepted);
        roomParticipantRepository.save(rp);

        return savedRoom;
    }

    // 매칭방 리스트
    @Transactional(readOnly = true)
    public List<MatchingRoom> getAllRooms() {
        return matchingRoomRepository.findAll();
    }

    // 매칭방 상세
    @Transactional(readOnly = true)
    public MatchingRoom getRoomById(Integer roomId) {
        return matchingRoomRepository.findById(roomId).orElse(null);
    }

    // 참여 신청
    @Transactional
    public void applyRoom(Integer roomId, Integer userId, Integer petId) {
        // 이미 등록된 레코드 있는지 확인
        RoomParticipant existing = roomParticipantRepository.findById(
                new com.busanit501.bootproject.domain.RoomParticipantId(roomId, userId)
        ).orElse(null);

        if (existing != null) {
            throw new RuntimeException("이미 신청한 방입니다.");
        }

        RoomParticipant rp = new RoomParticipant();
        rp.setRoomId(roomId);
        rp.setUserId(userId);
        rp.setPetId(petId);
        rp.setCreatedAt(LocalDateTime.now());
        rp.setStatus(RoomParticipant.ParticipantStatus.Pending);
        roomParticipantRepository.save(rp);
    }

    // 참가 신청 승인
    @Transactional
    public void acceptParticipant(Integer roomId, Integer userId) {
        RoomParticipant rp = roomParticipantRepository.findById(
                new com.busanit501.bootproject.domain.RoomParticipantId(roomId, userId)
        ).orElseThrow(() -> new RuntimeException("참여 신청 정보가 없습니다."));
        rp.setStatus(RoomParticipant.ParticipantStatus.Accepted);
        roomParticipantRepository.save(rp);

        // 매칭방 currentParticipants + 1
        MatchingRoom room = matchingRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("매칭방 정보가 없습니다."));
        room.setCurrentParticipants(room.getCurrentParticipants() + 1);
        matchingRoomRepository.save(room);
    }

    // 참가 신청 거절
    @Transactional
    public void rejectParticipant(Integer roomId, Integer userId) {
        RoomParticipant rp = roomParticipantRepository.findById(
                new com.busanit501.bootproject.domain.RoomParticipantId(roomId, userId)
        ).orElseThrow(() -> new RuntimeException("참여 신청 정보가 없습니다."));
        rp.setStatus(RoomParticipant.ParticipantStatus.Rejected);
        roomParticipantRepository.save(rp);
    }

    // 해당 방의 참가자 목록
    @Transactional(readOnly = true)
    public List<RoomParticipant> getParticipantsByRoomId(Integer roomId) {
        return roomParticipantRepository.findByRoomId(roomId);
    }
}
