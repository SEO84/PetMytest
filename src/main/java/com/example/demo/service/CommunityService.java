package com.example.demo.service;

import com.example.demo.domain.Comments;
import com.example.demo.domain.Likes;
import com.example.demo.domain.Posts;
import com.example.demo.domain.Users;
import com.example.demo.dto.CommentDTO;
import com.example.demo.dto.LikeDTO;
import com.example.demo.dto.PostDTO;
import com.example.demo.repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CommunityService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    /**
     * 게시글 작성
     */
    public PostDTO createPost(PostDTO dto) {
        Users user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Posts post = modelMapper.map(dto, Posts.class);
        post.setUser(user);

        Posts saved = postRepository.save(post);
        return modelMapper.map(saved, PostDTO.class);
    }

    /**
     * 게시글 수정
     */
    public PostDTO updatePost(PostDTO dto) {
        Posts post = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // 타이틀, 본문 등 필요한 필드만 수정
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        // 카테고리 변경 로직 필요 시 추가

        // 변경사항 저장 후 반환
        Posts updated = postRepository.save(post);
        return modelMapper.map(updated, PostDTO.class);
    }

    /**
     * 게시글 상세 조회
     */
    @Transactional(readOnly = true)
    public PostDTO getPostById(Integer postId) {
        Posts post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        return modelMapper.map(post, PostDTO.class);
    }

    /**
     * 댓글 작성
     */
    public CommentDTO createComment(CommentDTO dto) {
        Users user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Posts post = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Comments comment = modelMapper.map(dto, Comments.class);
        comment.setUser(user);
        comment.setPost(post);

        Comments saved = commentRepository.save(comment);
        return modelMapper.map(saved, CommentDTO.class);
    }

    /**
     * 게시글의 댓글 목록
     */
    @Transactional(readOnly = true)
    public List<CommentDTO> getCommentsByPostId(Integer postId) {
        // ★ 언더스코어로 객체와 필드 구분 (post.postId → post_PostId)
        List<Comments> commentList = commentRepository.findByPost_PostIdOrderByCreatedAtDesc(postId);
        return commentList.stream()
                .map(c -> modelMapper.map(c, CommentDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * 좋아요 등록
     */
    public LikeDTO createLike(LikeDTO dto) {
        Users user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Posts post = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Likes like = modelMapper.map(dto, Likes.class);
        like.setUser(user);
        like.setPost(post);

        Likes saved = likeRepository.save(like);
        return modelMapper.map(saved, LikeDTO.class);
    }

    /**
     * 특정 게시글 좋아요 수
     */
    @Transactional(readOnly = true)
    public long getLikeCount(Integer postId) {
        // ★ 언더스코어로 객체와 필드 구분 (post.postId → post_PostId)
        return likeRepository.countByPost_PostId(postId);
    }
}
