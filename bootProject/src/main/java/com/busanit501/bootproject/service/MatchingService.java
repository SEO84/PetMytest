// MatchingService.java
package com.busanit501.bootproject.service;

import com.busanit501.bootproject.domain.MatchingRoom;
import com.busanit501.bootproject.domain.Pet;
import com.busanit501.bootproject.domain.RoomParticipant;
import com.busanit501.bootproject.domain.User;
import com.busanit501.bootproject.dto.MatchingRoomDTO;
import com.busanit501.bootproject.enums.Gender;
import com.busanit501.bootproject.repository.MatchingRoomRepository;
import com.busanit501.bootproject.repository.PetRepository;
import com.busanit501.bootproject.repository.RoomParticipantRepository;
import com.busanit501.bootproject.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MatchingService {

    private final MatchingRoomRepository matchingRoomRepository;
    private final RoomParticipantRepository roomParticipantRepository;
    private final UserRepository userRepository;
    private final PetRepository petRepository;

    public MatchingService(MatchingRoomRepository matchingRoomRepository,
                           RoomParticipantRepository roomParticipantRepository,
                           UserRepository userRepository,
                           PetRepository petRepository) {
        this.matchingRoomRepository = matchingRoomRepository;
        this.roomParticipantRepository = roomParticipantRepository;
        this.userRepository = userRepository;
        this.petRepository = petRepository;
    }

    /**
     * 모든 매칭방 조회
     */
    @Transactional(readOnly = true)
    public List<MatchingRoom> getAllRooms() {
        return matchingRoomRepository.findAll();
    }

    /**
     * 특정 매칭방 조회
     */
    @Transactional(readOnly = true)
    public MatchingRoom getRoomById(Integer roomId) {
        return matchingRoomRepository.findById(roomId).orElse(null);
    }

    /**
     * 특정 방의 모든 참가자 조회
     */
    @Transactional(readOnly = true)
    public List<RoomParticipant> getParticipantsByRoomId(Integer roomId) {
        return roomParticipantRepository.findByMatchingRoom_RoomId(roomId);
    }

    /**
     * 매칭방 생성
     */
    @Transactional
    public MatchingRoom createRoom(MatchingRoomDTO dto, User hostUser) {
        // 방장의 반려동물 조회
        Pet hostPet = petRepository.findById(dto.getHostPetId())
                .orElseThrow(() -> new RuntimeException("방장의 반려동물을 찾을 수 없습니다."));

        // 매칭방 생성
        MatchingRoom room = new MatchingRoom();
        room.setTitle(dto.getTitle());
        room.setDescription(dto.getDescription());
        room.setPlace(dto.getPlace());
        room.setMeetingDate(dto.getMeetingDate());
        room.setMeetingTime(dto.getMeetingTime());
        room.setMaxParticipants(dto.getMaxParticipants());
        room.setCurrentParticipants(1); // 방장 1명으로 초기화
        room.setHostPet(hostPet);

        MatchingRoom savedRoom = matchingRoomRepository.save(room);

        // 방장 참가자 등록 (승인 상태)
        RoomParticipant hostParticipant = new RoomParticipant();
        hostParticipant.setMatchingRoom(savedRoom);
        hostParticipant.setUser(hostUser);
        hostParticipant.setPet(hostPet);
        hostParticipant.setStatus(RoomParticipant.ParticipantStatus.Accepted);

        // 중복 신청 방지
        boolean exists = roomParticipantRepository.existsByMatchingRoom_RoomIdAndUser_UserIdAndPet_PetId(
                savedRoom.getRoomId(), hostUser.getUserId(), hostPet.getPetId()
        );
        if (exists) {
            throw new RuntimeException("이미 방장이 신청한 방입니다.");
        }

        roomParticipantRepository.save(hostParticipant);

        // 추가 펫 처리
        if (dto.getAdditionalPetIds() != null && !dto.getAdditionalPetIds().isEmpty()) {
            for (Integer petId : dto.getAdditionalPetIds()) {
                Pet additionalPet = petRepository.findById(petId)
                        .orElseThrow(() -> new RuntimeException("추가 반려동물을 찾을 수 없습니다."));

                // 방 참여자 등록
                RoomParticipant additionalParticipant = new RoomParticipant();
                additionalParticipant.setMatchingRoom(savedRoom);
                additionalParticipant.setUser(hostUser); // 방장과 동일한 사용자
                additionalParticipant.setPet(additionalPet);
                additionalParticipant.setStatus(RoomParticipant.ParticipantStatus.Accepted);

                // 중복 방지
                boolean additionalExists = roomParticipantRepository.existsByMatchingRoom_RoomIdAndUser_UserIdAndPet_PetId(
                        savedRoom.getRoomId(), hostUser.getUserId(), additionalPet.getPetId()
                );
                if (additionalExists) {
                    throw new RuntimeException("이미 추가된 반려동물입니다: " + additionalPet.getName());
                }

                roomParticipantRepository.save(additionalParticipant);

                // 참가자 수 증가
                savedRoom.setCurrentParticipants(savedRoom.getCurrentParticipants() + 1);

                // 최대 참가자 수 초과 방지
                if (savedRoom.getCurrentParticipants() > savedRoom.getMaxParticipants()) {
                    throw new RuntimeException("최대 참가 인원을 초과하였습니다.");
                }
            }
            matchingRoomRepository.save(savedRoom); // 변경사항 저장
        }

        return savedRoom;
    }

    /**
     * 참가 신청
     */
    @Transactional
    public void applyRoom(Integer roomId, Integer userId, Integer petId) {
        if (petId == null) {
            throw new RuntimeException("펫을 선택해야 합니다.");
        }

        // 방, 사용자, 펫 존재 여부 확인
        MatchingRoom room = matchingRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("매칭방을 찾을 수 없습니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("펫을 찾을 수 없습니다."));

        // 이미 동일한 roomId, userId, petId로 신청한 경우 방지
        boolean exists = roomParticipantRepository.existsByMatchingRoom_RoomIdAndUser_UserIdAndPet_PetId(
                roomId, userId, petId
        );
        if (exists) {
            RoomParticipant existingParticipant = roomParticipantRepository.findByMatchingRoom_RoomIdAndUser_UserIdAndPet_PetId(
                    roomId, userId, petId
            ).orElse(null);
            if (existingParticipant != null && existingParticipant.getStatus() != RoomParticipant.ParticipantStatus.Rejected) {
                throw new RuntimeException("이미 신청한 방입니다.");
            }
        }

        // 참가자 수 확인
        if (room.getCurrentParticipants() >= room.getMaxParticipants()) {
            throw new RuntimeException("매칭방의 최대 인원에 도달하였습니다.");
        }

        RoomParticipant rp = new RoomParticipant();
        rp.setMatchingRoom(room);
        rp.setUser(user);
        rp.setPet(pet);
        rp.setStatus(RoomParticipant.ParticipantStatus.Pending);
        roomParticipantRepository.save(rp);

        // 참가자 수 증가
        room.setCurrentParticipants(room.getCurrentParticipants() + 1);
        matchingRoomRepository.save(room);
    }

    /**
     * 참가 신청 승인
     */
    @Transactional
    public void acceptParticipant(Integer roomId, Integer userId, Integer petId) {
        RoomParticipant rp = roomParticipantRepository.findByMatchingRoom_RoomIdAndUser_UserIdAndPet_PetId(
                roomId, userId, petId
        ).orElseThrow(() -> new RuntimeException("참여 신청 정보가 없습니다."));
        rp.setStatus(RoomParticipant.ParticipantStatus.Accepted);
        roomParticipantRepository.save(rp);
    }

    /**
     * 참가 신청 거절
     */
    @Transactional
    public void rejectParticipant(Integer roomId, Integer userId, Integer petId) {
        RoomParticipant rp = roomParticipantRepository.findByMatchingRoom_RoomIdAndUser_UserIdAndPet_PetId(
                roomId, userId, petId
        ).orElseThrow(() -> new RuntimeException("참여 신청 정보가 없습니다."));
        rp.setStatus(RoomParticipant.ParticipantStatus.Rejected);
        roomParticipantRepository.save(rp);

        // 참가자 수 감소
        MatchingRoom room = rp.getMatchingRoom();
        room.setCurrentParticipants(room.getCurrentParticipants() - 1);
        matchingRoomRepository.save(room);
    }
}
