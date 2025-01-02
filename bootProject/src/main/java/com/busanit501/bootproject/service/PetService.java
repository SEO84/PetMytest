// PetService.java
package com.busanit501.bootproject.service;

import com.busanit501.bootproject.domain.Pet;
import com.busanit501.bootproject.domain.User;
import com.busanit501.bootproject.dto.PetDTO;
import com.busanit501.bootproject.enums.Gender;
import com.busanit501.bootproject.repository.PetRepository;
import com.busanit501.bootproject.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PetService {

    private final PetRepository petRepository;
    private final UserRepository userRepository;

    public PetService(PetRepository petRepository, UserRepository userRepository) {
        this.petRepository = petRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Pet createPet(Integer userId, PetDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found."));
        Pet pet = new Pet();
        pet.setUser(user);
        pet.setName(dto.getName());
        pet.setType(dto.getType());
        pet.setAge(dto.getAge());
        pet.setGender(dto.getPetGender()); // 직접 할당
        pet.setWeight(dto.getWeight());
        pet.setPersonality(dto.getPersonality());
        // createdAt과 updatedAt은 @CreationTimestamp와 @UpdateTimestamp로 자동 설정
        return petRepository.save(pet);
    }

    // 로그인 유저의 모든 펫 조회
    @Transactional(readOnly = true)
    public List<Pet> findAllByUserId(Integer userId){
        return petRepository.findByUserUserId(userId);
    }
}
