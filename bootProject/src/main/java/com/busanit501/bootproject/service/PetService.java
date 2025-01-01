package com.busanit501.bootproject.service;

import com.busanit501.bootproject.domain.Pet;
import com.busanit501.bootproject.domain.User;
import com.busanit501.bootproject.dto.PetDTO;
import com.busanit501.bootproject.repository.PetRepository;
import com.busanit501.bootproject.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        pet.setGender(dto.getGender().equalsIgnoreCase("Male")
                ? Pet.Gender.Male
                : Pet.Gender.Female);
        pet.setWeight(dto.getWeight());
        pet.setPersonality(dto.getPersonality());
        pet.setCreatedAt(LocalDateTime.now());
        pet.setUpdatedAt(LocalDateTime.now());
        return petRepository.save(pet);
    }

    // 로그인 유저의 모든 펫 조회
    public List<Pet> findAllByUserId(Integer userId){
        return petRepository.findByUserUserId(userId);
    }
}

