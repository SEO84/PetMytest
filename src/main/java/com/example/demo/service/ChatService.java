package com.example.demo.service;

import com.example.demo.domain.Messages;
import com.example.demo.domain.Users;
import com.example.demo.dto.MessageDTO;
import com.example.demo.repository.MessageRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    /**
     * 메시지 전송
     */
    public MessageDTO sendMessage(MessageDTO messageDTO) {
        // sender, receiver 유효성 체크
        Users sender = userRepository.findById(messageDTO.getSenderId())
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        Users receiver = userRepository.findById(messageDTO.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        Messages message = modelMapper.map(messageDTO, Messages.class);
        message.setSender(sender);
        message.setReceiver(receiver);

        Messages saved = messageRepository.save(message);
        return modelMapper.map(saved, MessageDTO.class);
    }

    /**
     * 메시지 읽음 처리
     */
    public void markAsRead(Integer messageId) {
        Optional<Messages> optional = messageRepository.findById(messageId);
        if (optional.isPresent()) {
            Messages msg = optional.get();
            msg.setIsRead(true);
            // JpaRepository save 호출 생략해도 엔티티 변경감지로 업데이트 됨
        }
    }

    /**
     * 메시지 상세 조회
     */
    @Transactional(readOnly = true)
    public MessageDTO getMessage(Integer messageId) {
        Messages msg = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        return modelMapper.map(msg, MessageDTO.class);
    }

    // 예시) 유저 간 대화 목록, 페이징 등은 필요 시 구현
    // public List<MessageDTO> getConversation(Integer userA, Integer userB) { ... }
}
