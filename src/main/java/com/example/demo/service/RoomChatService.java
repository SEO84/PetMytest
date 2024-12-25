package com.example.demo.service;
import com.example.demo.domain.*;
import com.example.demo.dto.MatchingRoomDTO;
import com.example.demo.dto.RoomChatMessageDTO;
import com.example.demo.dto.RoomParticipantDTO;
import com.example.demo.repository.MatchingRoomRepository;
import com.example.demo.repository.RoomChatMessageRepository;
import com.example.demo.repository.RoomParticipantRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RoomChatService {

    private final RoomChatMessageRepository chatMessageRepository;
    private final RoomParticipantRepository participantRepository;
    private final ModelMapper modelMapper;

    /**
     * 메시지 전송 (방 안 채팅)
     */
    public RoomChatMessageDTO sendMessage(RoomChatMessageDTO dto) {
        // 참여자가 Accepted 상태인지 확인하여, 방에 있는 사람만 채팅 가능
        RoomParticipantsId pk = new RoomParticipantsId(dto.getRoomId(), dto.getSenderId());
        RoomParticipants participant = participantRepository.findById(pk)
                .orElseThrow(() -> new RuntimeException("해당 방 참여자가 아닙니다."));

        if (participant.getStatus() != RoomParticipants.ParticipantStatus.Accepted) {
            throw new RuntimeException("승인되지 않은 사용자는 채팅할 수 없습니다.");
        }

        RoomChatMessages entity = modelMapper.map(dto, RoomChatMessages.class);
        entity.setSentAt(LocalDateTime.now());

        RoomChatMessages saved = chatMessageRepository.save(entity);
        return modelMapper.map(saved, RoomChatMessageDTO.class);
    }

    /**
     * 방 채팅 내역 조회
     */
    @Transactional(readOnly = true)
    public List<RoomChatMessageDTO> getChatMessages(Integer roomId) {
        List<RoomChatMessages> messages = chatMessageRepository.findByRoomIdOrderBySentAtAsc(roomId);
        return messages.stream()
                .map(m -> modelMapper.map(m, RoomChatMessageDTO.class))
                .collect(Collectors.toList());
    }
}

