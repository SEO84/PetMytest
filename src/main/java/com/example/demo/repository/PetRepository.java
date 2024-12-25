package com.example.demo.repository;

import com.example.demo.domain.Pets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetRepository extends JpaRepository<Pets, Integer> {
    // 필요하면 커스텀 쿼리 추가
    // List<Pets> findByUserUserId(Integer userId);
}
