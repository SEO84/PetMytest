package com.example.demo.repository;

import com.example.demo.domain.Galleries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GalleryRepository extends JpaRepository<Galleries, Integer> {
    // 커스텀 메소드 예시:
    // List<Galleries> findByUserUserIdOrderByUploadedAtDesc(Integer userId);
}
