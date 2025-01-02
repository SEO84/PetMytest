package com.busanit501.bootproject.repository;

import com.busanit501.bootproject.domain.Pet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PetRepository extends JpaRepository<Pet, Integer> {

    List<Pet> findByUserUserId(Integer userId);

    Optional<Pet> findByPetIdAndUserUserId(Integer petId, Integer userId);

    Page<Pet> findByUserUserId(Integer userId, Pageable pageable);

    // 추가 메서드 예시
    List<Pet> findByNameContaining(String name);
}
