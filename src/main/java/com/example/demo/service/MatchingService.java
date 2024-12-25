package com.example.demo.service;

import com.example.demo.domain.MatchingRooms;
import com.example.demo.domain.RoomParticipants;
import com.example.demo.domain.RoomParticipantsId;
import com.example.demo.domain.Users;
import com.example.demo.dto.MatchingRoomDTO;
import com.example.demo.dto.RoomParticipantDTO;
import com.example.demo.repository.MatchingRoomRepository;
import com.example.demo.repository.RoomParticipantRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class MatchingService {

    private final MatchingRoomRepository roomRepository;
    private final RoomParticipantRepository participantRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    /**
     * 매칭방 생성 (hostId가 방장)
     */
    public MatchingRoomDTO createRoom(MatchingRoomDTO roomDTO) {
        Users host = userRepository.findById(roomDTO.getHostId())
                .orElseThrow(() -> new RuntimeException("Host not found"));

        // maxParticipants 범위 체크 (1~10)
        if (roomDTO.getMaxParticipants() < 1 || roomDTO.getMaxParticipants() > 10) {
            throw new RuntimeException("최대 참가인원은 1~10명 범위만 가능합니다.");
        }

        MatchingRooms room = modelMapper.map(roomDTO, MatchingRooms.class);
        room.setHost(host);

        // 방장 1명 포함
        room.setCurrentParticipants(1);

        MatchingRooms savedRoom = roomRepository.save(room);
        return modelMapper.map(savedRoom, MatchingRoomDTO.class);
    }

    /**
     * 참가 신청 (Pending 상태)
     */
    public RoomParticipantDTO requestJoin(Integer roomId, Integer userId) {
        MatchingRooms room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 이미 신청 또는 참여 중인지 확인
        RoomParticipantsId pk = new RoomParticipantsId(roomId, userId);
        if (participantRepository.existsById(pk)) {
            throw new RuntimeException("이미 신청 또는 참여 중입니다.");
        }

        // 방 상태가 Closed면 신청 불가
        if (room.getStatus() == MatchingRooms.RoomStatus.Closed) {
            throw new RuntimeException("이미 닫힌 방입니다.");
        }

        // 인원이 이미 가득 찼는지 확인
        if (room.getCurrentParticipants() >= room.getMaxParticipants()) {
            throw new RuntimeException("인원이 가득 찼습니다.");
        }

        // 신청 저장
        RoomParticipants participant = RoomParticipants.builder()
                .roomId(roomId)
                .userId(userId)
                .status(RoomParticipants.ParticipantStatus.Pending)
                .joinedAt(LocalDateTime.now())
                .build();

        participantRepository.save(participant);
        return modelMapper.map(participant, RoomParticipantDTO.class);
    }

    /**
     * 방장 승인 / 거절
     */
    public RoomParticipantDTO updateParticipantStatus(Integer roomId, Integer userId,
                                                      RoomParticipants.ParticipantStatus status) {
        // 방 조회
        MatchingRooms room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        // 참가자 조회
        RoomParticipantsId pk = new RoomParticipantsId(roomId, userId);
        RoomParticipants participant = participantRepository.findById(pk)
                .orElseThrow(() -> new RuntimeException("Participant not found"));

        // 승인 시 인원수 증가 (기존 status가 Pending일 때만 반영)
        if (status == RoomParticipants.ParticipantStatus.Accepted &&
                participant.getStatus() == RoomParticipants.ParticipantStatus.Pending) {

            if (room.getCurrentParticipants() >= room.getMaxParticipants()) {
                throw new RuntimeException("최대 인원수를 초과했습니다.");
            }
            room.setCurrentParticipants(room.getCurrentParticipants() + 1);
            roomRepository.save(room);
        }

        // 거절 시 (추가 로직은 자유)
        // 승인 -> 거절로 바뀌는 케이스는 흔치 않으니, 보통 Pending에서만 거절 처리

        participant.setStatus(status);
        participantRepository.save(participant);

        return modelMapper.map(participant, RoomParticipantDTO.class);
    }

    /**
     * 매칭방 닫기
     */
    public MatchingRoomDTO closeRoom(Integer roomId) {
        MatchingRooms room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        room.setStatus(MatchingRooms.RoomStatus.Closed);
        MatchingRooms updated = roomRepository.save(room);
        return modelMapper.map(updated, MatchingRoomDTO.class);
    }
}

