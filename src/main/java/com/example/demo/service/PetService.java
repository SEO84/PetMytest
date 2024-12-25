package com.example.demo.service;

import com.example.demo.domain.Pets;
import com.example.demo.domain.Users;
import com.example.demo.dto.PetDTO;
import com.example.demo.repository.PetRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PetService {

    private final PetRepository petRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    /**
     * 반려동물 등록
     */
    public PetDTO createPet(PetDTO petDTO) {
        // 주인(Users) 존재 여부 확인
        Users user = userRepository.findById(petDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // DTO -> Entity
        Pets pet = modelMapper.map(petDTO, Pets.class);
        pet.setUser(user);

        // 저장
        Pets savedPet = petRepository.save(pet);

        // Entity -> DTO
        return modelMapper.map(savedPet, PetDTO.class);
    }

    /**
     * 반려동물 수정
     */
    public PetDTO updatePet(PetDTO petDTO) {
        Pets pet = petRepository.findById(petDTO.getPetId())
                .orElseThrow(() -> new RuntimeException("Pet not found"));

        // 필요 필드만 업데이트
        pet.setName(petDTO.getName());
        pet.setType(petDTO.getType());
        pet.setAge(petDTO.getAge());
        // etc...

        Pets updatedPet = petRepository.save(pet);
        return modelMapper.map(updatedPet, PetDTO.class);
    }

    /**
     * 반려동물 삭제
     */
    public void deletePet(Integer petId) {
        petRepository.deleteById(petId);
    }

    /**
     * (예시) 특정 사용자 소유의 모든 반려동물 조회
     */
//    @Transactional(readOnly = true)
//    public List<PetDTO> getPetsByUserId(Integer userId) {
//        List<Pets> petsList = petRepository.findByUserUserId(userId);
//        return petsList.stream()
//                .map(pet -> modelMapper.map(pet, PetDTO.class))
//                .collect(Collectors.toList());
//    }

}
