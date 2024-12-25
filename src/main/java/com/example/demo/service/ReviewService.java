package com.example.demo.service;

import com.example.demo.domain.Reviews;
import com.example.demo.domain.Users;
import com.example.demo.dto.ReviewDTO;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    /**
     * 리뷰 작성
     */
    public ReviewDTO createReview(ReviewDTO reviewDTO) {
        // 리뷰어, 리뷰대상 유저 확인
        Users reviewer = userRepository.findById(reviewDTO.getReviewerId())
                .orElseThrow(() -> new RuntimeException("Reviewer not found"));
        Users reviewed = userRepository.findById(reviewDTO.getReviewedId())
                .orElseThrow(() -> new RuntimeException("Reviewed user not found"));

        // DTO -> Entity
        Reviews review = modelMapper.map(reviewDTO, Reviews.class);
        review.setReviewer(reviewer);
        review.setReviewed(reviewed);

        // 저장
        Reviews saved = reviewRepository.save(review);
        return modelMapper.map(saved, ReviewDTO.class);
    }

    /**
     * 리뷰 수정
     */
    public ReviewDTO updateReview(ReviewDTO reviewDTO) {
        Reviews review = reviewRepository.findById(reviewDTO.getReviewId())
                .orElseThrow(() -> new RuntimeException("Review not found"));

        // 점수, 코멘트만 수정 가능하다고 가정
        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());

        // 저장
        Reviews updated = reviewRepository.save(review);
        return modelMapper.map(updated, ReviewDTO.class);
    }

    /**
     * 리뷰 삭제
     */
    public void deleteReview(Integer reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    /**
     * 단일 리뷰 조회
     */
    @Transactional(readOnly = true)
    public ReviewDTO getReviewById(Integer reviewId) {
        Reviews review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        return modelMapper.map(review, ReviewDTO.class);
    }
}
