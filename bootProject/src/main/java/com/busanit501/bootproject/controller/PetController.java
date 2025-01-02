// PetController.java
package com.busanit501.bootproject.controller;

import com.busanit501.bootproject.domain.Pet;
import com.busanit501.bootproject.domain.User;
import com.busanit501.bootproject.dto.PetDTO;
import com.busanit501.bootproject.repository.PetRepository;
import com.busanit501.bootproject.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pet")
public class PetController {

    private final PetRepository petRepository;
    private final UserRepository userRepository;

    @Autowired
    public PetController(PetRepository petRepository, UserRepository userRepository) {
        this.petRepository = petRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerPet(@Valid @RequestBody PetDTO petDTO, Authentication authentication) {
        try {
            String email = authentication.getName();
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

            Pet pet = new Pet();
            pet.setUser(user);
            pet.setName(petDTO.getName());
            pet.setType(petDTO.getType());
            pet.setAge(petDTO.getAge());
            pet.setGender(petDTO.getPetGender());
            pet.setWeight(petDTO.getWeight());
            pet.setPersonality(petDTO.getPersonality());

            Pet savedPet = petRepository.save(pet);

            // 응답 데이터 구성
            return ResponseEntity.ok(new PetResponse(savedPet.getPetId(), savedPet.getName()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    // 응답용 DTO 클래스
    static class PetResponse {
        private Integer petId;
        private String name;

        public PetResponse(Integer petId, String name) {
            this.petId = petId;
            this.name = name;
        }

        public Integer getPetId() {
            return petId;
        }

        public String getName() {
            return name;
        }
    }

    // 에러 응답용 DTO 클래스
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
