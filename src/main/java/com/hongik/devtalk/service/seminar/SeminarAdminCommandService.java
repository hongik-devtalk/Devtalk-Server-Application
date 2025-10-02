package com.hongik.devtalk.service.seminar;

import com.hongik.devtalk.domain.Review;
import com.hongik.devtalk.global.apiPayload.code.GeneralErrorCode;
import com.hongik.devtalk.global.apiPayload.exception.GeneralException;
import com.hongik.devtalk.repository.review.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SeminarAdminCommandService {

    private final ReviewRepository reviewRepository;

    /**
     * 후기 ID로 특정 후기를 홈 화면에 노출
     *
     * @param reviewId 후기 ID
     * @throws GeneralException 후기가 존재하지 않을 경우
     */
    @Transactional
    public void exposeReviewToHome(Long reviewId) {
        // 후기 존재 여부 확인
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.REVIEW_NOT_FOUND));

        review.updateIsNote(true);
    }

    /**
     * 후기 ID로 특정 후기를 홈 화면에서 숨김
     *
     * @param reviewId 후기 ID
     * @throws GeneralException 후기가 존재하지 않을 경우
     */
    @Transactional
    public void hideReviewFromHome(Long reviewId) {
        // 후기 존재 여부 확인
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.REVIEW_NOT_FOUND));

        review.updateIsNote(false);
    }

    /**
     * 후기 ID로 특정 후기를 영구 삭제
     *
     * @param reviewId 후기 ID
     * @throws GeneralException 후기가 존재하지 않을 경우
     */
    @Transactional
    public void deleteReview(Long reviewId) {
        // 후기 존재 여부 확인
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.REVIEW_NOT_FOUND));

        reviewRepository.delete(review);
    }
}