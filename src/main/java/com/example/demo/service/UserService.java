package com.example.demo.service;

import com.example.demo.domain.Users;
import com.example.demo.dto.UserDTO;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 사용자 관리 (회원가입, 로그인, 프로필 수정 등) 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional  // 클래스 레벨에 붙이면 public 메서드 모두 트랜잭션 적용
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    /**
     * 회원 가입
     */
    public UserDTO createUser(UserDTO userDTO) {
        // 1. DTO -> Entity 변환
        Users user = modelMapper.map(userDTO, Users.class);

        // (예시) 추가 검증/비밀번호 암호화 로직
        // user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 2. DB 저장
        Users savedUser = userRepository.save(user);

        // 3. Entity -> DTO 변환 후 반환
        return modelMapper.map(savedUser, UserDTO.class);
    }

    /**
     * 사용자 조회 (단일)
     */
    @Transactional(readOnly = true)
    public UserDTO getUserById(Integer userId) {
        Optional<Users> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            return modelMapper.map(optionalUser.get(), UserDTO.class);
        }
        return null; // or throw CustomException
    }

    /**
     * 사용자 정보 업데이트
     */
    public UserDTO updateUser(UserDTO userDTO) {
        Users user = userRepository.findById(userDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 필요한 필드만 수정
        user.setName(userDTO.getName());
        user.setAge(userDTO.getAge());
        user.setAddress(userDTO.getAddress());
        // etc...

        // 변경 사항 저장
        Users updatedUser = userRepository.save(user);

        return modelMapper.map(updatedUser, UserDTO.class);
    }

    /**
     * 사용자 삭제(탈퇴)
     */
    public void deleteUser(Integer userId) {
        userRepository.deleteById(userId);
    }

    /**
     * (예시) 이메일로 사용자 찾기
     */
    @Transactional(readOnly = true)
    public UserDTO getUserByEmail(String email) {
        // Optional<Users> optionalUser = userRepository.findByEmail(email);
        // if (optionalUser.isPresent()) {
        //     return modelMapper.map(optionalUser.get(), UserDTO.class);
        // }
        // return null;
        // (UserRepository에 findByEmail(...) 메서드가 있어야 함)
        return null;
    }
}
