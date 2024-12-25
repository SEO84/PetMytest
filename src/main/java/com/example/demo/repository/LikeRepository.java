package com.example.demo.repository;

import com.example.demo.domain.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Likes, Integer> {
    // 예) 특정 게시글의 좋아요 수
    // long countByPostPostId(Integer postId);
    long countByPost_PostId(Integer postId);
}
