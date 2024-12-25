package com.example.demo.controller;

import com.example.demo.dto.CommentDTO;
import com.example.demo.dto.LikeDTO;
import com.example.demo.dto.PostDTO;
import com.example.demo.service.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/community")
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityService communityService;

    /**
     * 게시글 작성
     */
    @PostMapping("/posts")
    public ResponseEntity<PostDTO> createPost(@RequestBody PostDTO dto) {
        PostDTO created = communityService.createPost(dto);
        return ResponseEntity
                .created(URI.create("/api/community/posts/" + created.getPostId()))
                .body(created);
    }

    /**
     * 게시글 수정
     */
    @PutMapping("/posts/{postId}")
    public ResponseEntity<PostDTO> updatePost(@PathVariable Integer postId,
                                              @RequestBody PostDTO dto) {
        dto.setPostId(postId);
        PostDTO updated = communityService.updatePost(dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * 게시글 상세 조회
     */
    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostDTO> getPost(@PathVariable Integer postId) {
        PostDTO found = communityService.getPostById(postId);
        if (found == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(found);
    }

    // 필요하다면 게시글 삭제도 추가
    // @DeleteMapping("/posts/{postId}")

    /**
     * 댓글 작성
     */
    @PostMapping("/comments")
    public ResponseEntity<CommentDTO> createComment(@RequestBody CommentDTO dto) {
        CommentDTO created = communityService.createComment(dto);
        return ResponseEntity.ok(created);
    }

    /**
     * 특정 게시글의 댓글 목록
     */
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<CommentDTO>> getComments(@PathVariable Integer postId) {
        List<CommentDTO> comments = communityService.getCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    /**
     * 좋아요 등록
     */
    @PostMapping("/likes")
    public ResponseEntity<LikeDTO> createLike(@RequestBody LikeDTO dto) {
        LikeDTO created = communityService.createLike(dto);
        return ResponseEntity.ok(created);
    }

    /**
     * 특정 게시글 좋아요 수
     */
    @GetMapping("/posts/{postId}/likes/count")
    public ResponseEntity<Long> getLikeCount(@PathVariable Integer postId) {
        long count = communityService.getLikeCount(postId);
        return ResponseEntity.ok(count);
    }
}
