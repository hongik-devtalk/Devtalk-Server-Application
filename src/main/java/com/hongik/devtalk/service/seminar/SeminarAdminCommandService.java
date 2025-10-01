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

    @Transactional
    public void exposeReviewToHome(Long reviewId) {
        // 후기 존재 여부 확인
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.REVIEW_NOT_FOUND));

        review.updateIsNote(true);
    }
}