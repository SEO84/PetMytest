// src/main/java/com/busanit501/bootproject/service/UserService.java
package com.busanit501.bootproject.service;

import com.busanit501.bootproject.domain.Pet;
import com.busanit501.bootproject.domain.User;
import com.busanit501.bootproject.dto.UserLoginDTO;
import com.busanit501.bootproject.dto.UserRegisterDTO;
import com.busanit501.bootproject.enums.Gender;
import com.busanit501.bootproject.repository.PetRepository;
import com.busanit501.bootproject.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PetRepository petRepository;
    private final PasswordEncoder passwordEncoder;

    // 생성자 주입
    public UserService(UserRepository userRepository, PetRepository petRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.petRepository = petRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 사용자 회원가입과 반려동물 등록
     */
    @Transactional
    public void registerWithPet(UserRegisterDTO dto) {
        // 이메일 중복 체크
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("이미 등록된 이메일입니다.");
        }

        // 사용자 생성
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword())); // 비밀번호 해싱
        user.setName(dto.getName());
        user.setAge(dto.getAge());
        user.setGender(dto.getGender());
        user.setAddress(dto.getAddress());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setIsVerified(false);
        // createdAt과 updatedAt은 @CreationTimestamp와 @UpdateTimestamp로 자동 설정

        User savedUser = userRepository.save(user); // DB 저장

        // 반려동물 생성
        Pet pet = new Pet();
        pet.setUser(savedUser); // 소유자(외래키) 연결
        pet.setName(dto.getPetName());
        pet.setType(dto.getPetType());
        pet.setAge(dto.getPetAge());
        pet.setGender(dto.getPetGender());
        pet.setWeight(dto.getPetWeight());
        pet.setPersonality(dto.getPetPersonality());
        // createdAt과 updatedAt은 @CreationTimestamp와 @UpdateTimestamp로 자동 설정

        petRepository.save(pet); // DB 저장
    }

    /**
     * 사용자 로그인
     */
    @Transactional(readOnly = true)
    public User login(UserLoginDTO dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
        return user;
    }

    /**
     * 사용자 한 명 조회
     */
    @Transactional(readOnly = true)
    public User findById(Integer userId) {
        return userRepository.findById(userId).orElse(null);
    }

    /**
     * 이메일로 사용자 조회
     */
    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}
