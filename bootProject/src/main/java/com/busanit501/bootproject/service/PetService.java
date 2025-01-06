package com.busanit501.bootproject.service;

import com.busanit501.bootproject.domain.Pet;
import com.busanit501.bootproject.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PetService {

    private final PetRepository petRepository;

    @Autowired
    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    /**
     * 사용자 ID로 모든 펫 조회
     */
    public List<Pet> findAllByUserId(Integer userId) {
        return petRepository.findAllByUser_UserId(userId);
    }

    /**
     * 펫 ID로 펫 조회
     */
    public Optional<Pet> findById(Integer petId) {
        return petRepository.findById(petId);
    }
}
