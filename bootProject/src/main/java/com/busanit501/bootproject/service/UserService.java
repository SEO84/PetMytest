package com.busanit501.bootproject.service;

import com.busanit501.bootproject.domain.Pet;
import com.busanit501.bootproject.domain.User;
import com.busanit501.bootproject.dto.UserLoginDTO;
import com.busanit501.bootproject.dto.UserRegisterDTO;
import com.busanit501.bootproject.repository.PetRepository;
import com.busanit501.bootproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PetRepository petRepository;

    @Autowired
    public UserService(UserRepository userRepository, PetRepository petRepository) {
        this.userRepository = userRepository;
        this.petRepository = petRepository;
    }

    /**
     * 사용자 회원가입과 반려동물 등록
     *
     * @param dto 사용자 및 반려동물 등록 정보 DTO
     */
    @Transactional
    public void registerWithPet(UserRegisterDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("이미 등록된 이메일입니다.");
        }

        User user = mapDtoToUser(dto);
        user.setPassword(dto.getPassword()); // 비밀번호 암호화 제거
        User savedUser = userRepository.save(user); // 사용자 저장

        Pet pet = mapDtoToPet(dto, savedUser);
        petRepository.save(pet); // 반려동물 저장
    }

    /**
     * 사용자 로그인
     *
     * @param dto 사용자 로그인 정보 DTO
     * @return 로그인 성공한 사용자
     */
    @Transactional(readOnly = true)
    public User login(UserLoginDTO dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        if (!dto.getPassword().equals(user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        return user;
    }

    /**
     * 사용자 ID로 사용자 조회
     *
     * @param userId 사용자 ID
     * @return User 객체 (없을 경우 null)
     */
    @Transactional(readOnly = true)
    public User findById(Integer userId) {
        return userRepository.findById(userId).orElse(null);
    }

    /**
     * 이메일로 사용자 조회
     *
     * @param email 사용자 이메일
     * @return User 객체 (없을 경우 null)
     */
    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    // --------------------- 내부 헬퍼 메서드 ---------------------

    /**
     * UserRegisterDTO를 User 엔티티로 매핑
     *
     * @param dto UserRegisterDTO
     * @return 매핑된 User 엔티티
     */
    private User mapDtoToUser(UserRegisterDTO dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword()); // 비밀번호 해싱 제거
        user.setName(dto.getName());
        user.setAge(dto.getAge());
        user.setGender(dto.getGender());
        user.setAddress(dto.getAddress());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setIsVerified(false); // 초기값 설정
        return user;
    }

    /**
     * UserRegisterDTO를 Pet 엔티티로 매핑
     *
     * @param dto  UserRegisterDTO
     * @param user 사용자 엔티티
     * @return 매핑된 Pet 엔티티
     */
    private Pet mapDtoToPet(UserRegisterDTO dto, User user) {
        Pet pet = new Pet();
        pet.setUser(user); // 소유자 설정
        pet.setName(dto.getPetName());
        pet.setType(dto.getPetType());
        pet.setAge(dto.getPetAge());
        pet.setGender(dto.getPetGender());
        pet.setWeight(dto.getPetWeight());
        pet.setPersonality(dto.getPetPersonality());
        return pet;
    }
}
