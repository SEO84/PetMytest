package com.busanit501.bootproject.service;

import com.busanit501.bootproject.domain.MatchingRoom;
import com.busanit501.bootproject.domain.Pet;
import com.busanit501.bootproject.domain.RoomParticipant;
import com.busanit501.bootproject.domain.User;
import com.busanit501.bootproject.dto.MatchingRoomDTO;
import com.busanit501.bootproject.exception.ResourceNotFoundException;
import com.busanit501.bootproject.repository.MatchingRoomRepository;
import com.busanit501.bootproject.repository.PetRepository;
import com.busanit501.bootproject.repository.RoomParticipantRepository;
import com.busanit501.bootproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MatchingService {

    private final MatchingRoomRepository roomRepository;
    private final RoomParticipantRepository participantRepository;
    private final PetRepository petRepository;
    private final UserRepository userRepository;

    @Autowired
    public MatchingService(MatchingRoomRepository roomRepository,
                           RoomParticipantRepository participantRepository,
                           PetRepository petRepository,
                           UserRepository userRepository) {
        this.roomRepository = roomRepository;
        this.participantRepository = participantRepository;
        this.petRepository = petRepository;
        this.userRepository = userRepository;
    }

    // 모든 매칭방을 조회합니다.
    public List<MatchingRoom> getAllRooms() {
        return roomRepository.findAll();
    }

    public MatchingRoom getRoomById(Integer roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("매칭방을 찾을 수 없습니다. ID: " + roomId));
    }

    /**
     * 매칭방을 생성합니다.
     *
     * @param dto      매칭방 생성 DTO
     * @param hostUser 매칭방 호스트 사용자
     */
    @Transactional
    public void createRoom(MatchingRoomDTO dto, User hostUser) {
        MatchingRoom room = new MatchingRoom();
        room.setHost(hostUser);
        room.setTitle(dto.getTitle());
        room.setDescription(dto.getDescription());
        room.setPlace(dto.getPlace());
        room.setMeetingDate(dto.getMeetingDate());
        room.setMeetingTime(dto.getMeetingTime());
        room.setMaxParticipants(dto.getMaxParticipants());
        room.setUser(hostUser);

        MatchingRoom savedRoom = roomRepository.save(room);

        // 선택된 모든 펫에 대해 참가자 생성
        List<Pet> pets = petRepository.findAllById(dto.getPetIds());
        for (Pet pet : pets) {
            RoomParticipant participant = new RoomParticipant();
            participant.setMatchingRoom(savedRoom);
            participant.setUser(hostUser);
            participant.setPet(pet);
            participant.setStatus(RoomParticipant.ParticipantStatus.Accepted); // 호스트의 펫은 자동 승인
            participantRepository.save(participant);
        }
    }


    /**
     * 사용자와 매칭방에 대한 참가 신청을 처리합니다.
     *
     * @param roomId 매칭방 ID
     * @param userId 사용자 ID
     * @param petIds 신청할 펫 ID 목록
     */
    @Transactional
    public void applyRoom(Integer roomId, Integer userId, List<Integer> petIds) {
        MatchingRoom room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("매칭방을 찾을 수 없습니다."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));

        // 이미 참가 신청 여부 확인
        List<RoomParticipant> existingParticipants = participantRepository.findAllByMatchingRoomAndUser(room, user);
        if (!existingParticipants.isEmpty()) {
            throw new RuntimeException("이미 참가 신청을 했습니다.");
        }

        List<Pet> pets = petRepository.findAllById(petIds);
        if (pets.size() != petIds.size()) {
            throw new ResourceNotFoundException("일부 펫을 찾을 수 없습니다.");
        }

        // 참가 인원 초과 확인 (호스트 포함)
        long currentParticipants = participantRepository.countByMatchingRoom(room);
        if (currentParticipants + pets.size() > room.getMaxParticipants()) {
            throw new RuntimeException("참가 인원이 초과되었습니다.");
        }

        // 참가자 생성: 각 펫에 대해 별도의 참가자 생성
        for (Pet pet : pets) {
            RoomParticipant participant = new RoomParticipant();
            participant.setMatchingRoom(room);
            participant.setUser(user);
            participant.setPet(pet);
            participant.setStatus(RoomParticipant.ParticipantStatus.Pending);
            participantRepository.save(participant);
        }
    }

    /**
     * 참가자를 승인합니다.
     *
     * @param roomId 매칭방 ID
     * @param userId 참가자 사용자 ID
     */
    @Transactional
    public void acceptParticipant(Integer roomId, Integer userId) {
        MatchingRoom room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("매칭방을 찾을 수 없습니다."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));

        List<RoomParticipant> participants = participantRepository.findAllByMatchingRoomAndUser(room, user);
        if (participants.isEmpty()) {
            throw new ResourceNotFoundException("참가 신청을 찾을 수 없습니다.");
        }

        for (RoomParticipant participant : participants) {
            if (participant.getStatus() == RoomParticipant.ParticipantStatus.Accepted) {
                throw new RuntimeException("이미 승인된 참가 신청입니다.");
            }
            participant.setStatus(RoomParticipant.ParticipantStatus.Accepted);
            participantRepository.save(participant);
        }
    }

    /**
     * 참가자를 거절합니다.
     *
     * @param roomId 매칭방 ID
     * @param userId 참가자 사용자 ID
     */
    @Transactional
    public void rejectParticipant(Integer roomId, Integer userId) {
        MatchingRoom room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("매칭방을 찾을 수 없습니다."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));

        List<RoomParticipant> participants = participantRepository.findAllByMatchingRoomAndUser(room, user);
        if (participants.isEmpty()) {
            throw new ResourceNotFoundException("참가 신청을 찾을 수 없습니다.");
        }

        for (RoomParticipant participant : participants) {
            if (participant.getStatus() == RoomParticipant.ParticipantStatus.Rejected) {
                throw new RuntimeException("이미 거절된 참가 신청입니다.");
            }
            participant.setStatus(RoomParticipant.ParticipantStatus.Rejected);
            participantRepository.save(participant);
        }
    }

    /**
     * 매칭방의 참가자 목록을 조회합니다.
     *
     * @param roomId 매칭방 ID
     * @return 참가자 목록
     */
    public List<RoomParticipant> getParticipantsByRoomId(Integer roomId) {
        MatchingRoom room = getRoomById(roomId);
        return participantRepository.findAllByMatchingRoom_RoomId(roomId);
    }

    /**
     * 특정 상태의 참가자 목록을 필터링합니다.
     *
     * @param participants 참가자 목록
     * @param status        필터링할 상태
     * @return 필터링된 참가자 목록
     */
    public List<RoomParticipant> filterParticipants(List<RoomParticipant> participants, RoomParticipant.ParticipantStatus status) {
        return participants.stream()
                .filter(p -> p.getStatus() == status)
                .collect(Collectors.toList());
    }
}
