package com.example.demo.repository;

import com.example.demo.domain.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Posts, Integer> {
    // 예) 카테고리별 게시글 조회
    // List<Posts> findByCategory(Posts.PostCategory category);
}
