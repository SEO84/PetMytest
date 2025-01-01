package com.busanit501.bootproject.repository;

import com.busanit501.bootproject.domain.Pet;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Integer> {
    @Transactional(readOnly = true)
    List<Pet> findByUserUserId(Integer userId);
}

