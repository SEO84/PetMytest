package com.busanit501.bootproject.service;

import com.busanit501.bootproject.domain.Pet;
import com.busanit501.bootproject.domain.User;
import com.busanit501.bootproject.dto.UserLoginDTO;
import com.busanit501.bootproject.dto.UserRegisterDTO;
import com.busanit501.bootproject.repository.PetRepository;
import com.busanit501.bootproject.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PetRepository petRepository;

    public UserService(UserRepository userRepository, PetRepository petRepository) {
        this.userRepository = userRepository;
        this.petRepository = petRepository;
    }

    @Transactional
    public void registerWithPet(UserRegisterDTO dto) {
        // 1) User 생성
        if (userRepository.findByEmail(dto.getEmail()) != null) {
            throw new RuntimeException("이미 등록된 이메일입니다.");
        }
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword()); // 실제 운영시 해싱 필요
        user.setName(dto.getName());
        user.setAge(dto.getAge());
        user.setGender(dto.getGender().equalsIgnoreCase("Male")
                ? User.Gender.Male : User.Gender.Female);
        user.setAddress(dto.getAddress());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setIsVerified(false);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user); // DB 저장

        // 2) Pet 생성
        Pet pet = new Pet();
        pet.setUser(savedUser); // 소유자(외래키) 연결
        pet.setName(dto.getPetName());
        pet.setType(dto.getPetType());
        pet.setAge(dto.getPetAge());
        pet.setGender(dto.getPetGender().equalsIgnoreCase("Male")
                ? Pet.Gender.Male : Pet.Gender.Female);
        pet.setWeight(dto.getPetWeight());
        pet.setPersonality(dto.getPetPersonality());
        pet.setCreatedAt(LocalDateTime.now());
        pet.setUpdatedAt(LocalDateTime.now());

        petRepository.save(pet); // DB 저장
    }

    // 회원가입
    @Transactional
    public User register(UserRegisterDTO dto) {
        // 이미 존재하는 email인지 체크
        if (userRepository.findByEmail(dto.getEmail()) != null) {
            throw new RuntimeException("이미 등록된 이메일입니다.");
        }
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword()); // 실제 운영시 해싱 권장
        user.setName(dto.getName());
        user.setAge(dto.getAge());
        user.setGender(
                dto.getGender().equalsIgnoreCase("Male")
                        ? User.Gender.Male
                        : User.Gender.Female
        );
        user.setAddress(dto.getAddress());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setIsVerified(false);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    // 로그인
    @Transactional(readOnly = true)
    public User login(UserLoginDTO dto) {
        User user = userRepository.findByEmail(dto.getEmail());
        if (user == null) return null;
        if (!user.getPassword().equals(dto.getPassword())) return null;
        return user;
    }

    // 사용자 한 명 조회
    @Transactional(readOnly = true)
    public User findById(Integer userId) {
        return userRepository.findById(userId).orElse(null);
    }
}
