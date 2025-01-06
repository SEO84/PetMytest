package com.busanit501.bootproject.controller;

import com.busanit501.bootproject.domain.Pet;
import com.busanit501.bootproject.domain.User;
import com.busanit501.bootproject.dto.PetDTO;
import com.busanit501.bootproject.repository.PetRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pet")
public class PetController {

    private final PetRepository petRepository;

    @Autowired
    public PetController(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    /**
     * 반려동물 등록 API
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerPet(@Valid @RequestBody PetDTO petDTO, HttpSession session) {
        try {
            // 사용자 인증 체크
            User loginUser = getLoginUser(session);
            if (loginUser == null) {
                throw new RuntimeException("로그인이 필요합니다.");
            }

            // 반려동물 저장
            Pet savedPet = savePet(petDTO, loginUser);

            // 성공 응답 반환
            return ResponseEntity.ok(new PetResponse(savedPet.getPetId(), savedPet.getName()));
        } catch (RuntimeException e) {
            // 실패 응답 반환
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    // --------------------- 내부 헬퍼 메서드 ---------------------

    /**
     * 세션에서 로그인된 사용자 가져오기
     *
     * @param session HttpSession 객체
     * @return 로그인된 사용자 (또는 null)
     */
    private User getLoginUser(HttpSession session) {
        return (User) session.getAttribute("loginUser");
    }

    /**
     * 반려동물 엔티티 저장
     *
     * @param petDTO    반려동물 정보 DTO
     * @param loginUser 로그인된 사용자
     * @return 저장된 반려동물 엔티티
     */
    private Pet savePet(PetDTO petDTO, User loginUser) {
        Pet pet = new Pet();
        pet.setUser(loginUser);
        pet.setName(petDTO.getName());
        pet.setType(petDTO.getType());
        pet.setAge(petDTO.getAge());
        pet.setGender(petDTO.getGender());
        pet.setWeight(petDTO.getWeight());
        pet.setPersonality(petDTO.getPersonality());
        return petRepository.save(pet);
    }

    // --------------------- 응답 DTO 클래스 ---------------------

    /**
     * 반려동물 응답용 DTO
     */
    static class PetResponse {
        private Integer id;
        private String name;

        public PetResponse(Integer id, String name) {
            this.id = id;
            this.name = name;
        }

        public Integer getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * 에러 응답용 DTO
     */
    static class ErrorResponse {
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
