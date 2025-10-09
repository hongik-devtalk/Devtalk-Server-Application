package com.hongik.devtalk.service.seminar;

import com.hongik.devtalk.domain.Review;
import com.hongik.devtalk.domain.seminar.admin.dto.SeminarReviewResponseDTO;
import com.hongik.devtalk.global.apiPayload.code.GeneralErrorCode;
import com.hongik.devtalk.global.apiPayload.exception.GeneralException;
import com.hongik.devtalk.repository.review.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeminarReviewService {

    private final ReviewRepository reviewRepository;

    /**
     * 후기 ID로 특정 후기를 홈 화면에 노출
     *
     * @param reviewId 후기 ID
     * @throws GeneralException 후기가 존재하지 않거나 비공개일 경우
     */
    @Transactional
    public void exposeReviewToHome(Long reviewId) {
        // 후기 존재 여부 확인
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.REVIEW_NOT_FOUND));

        // 비공개 후기일 경우 에러
        if (!review.isPublic()) {
            throw new GeneralException(GeneralErrorCode.REVIEW_NOT_PUBLIC);
        }

        // 다음 순서값 - 현재 노출 중인 후기 중 displayOrder 최댓값 + 1
        Integer maxDisplayOrder = reviewRepository.findMaxDisplayOrder();
        int nextDisplayOrder = (maxDisplayOrder == null) ? 1 : maxDisplayOrder + 1;

        review.updateIsNote(true);
        review.updateDisplayOrder(nextDisplayOrder);
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
        review.updateDisplayOrder(null);
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

    /**
     * 세미나 ID로 해당 세미나의 후기 목록 조회
     *
     * @param seminarId 세미나 ID
     * @return 후기 DTO 리스트
     */
    @Transactional(readOnly = true)
    public List<SeminarReviewResponseDTO> getSeminarReviews(Long seminarId) {
        return reviewRepository.findBySeminarIdWithStudentAndDepartments(seminarId)
                .stream()
                .map(SeminarReviewResponseDTO::from)
                .toList();
    }
}
