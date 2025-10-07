package com.hongik.devtalk.service.mainpage;

import com.hongik.devtalk.domain.Review;
import com.hongik.devtalk.domain.Student;
import com.hongik.devtalk.domain.mainpage.dto.DeleteReviewResponseDto;
import com.hongik.devtalk.domain.mainpage.dto.ReorderResponseDto;
import com.hongik.devtalk.domain.mainpage.dto.ReviewResponseDto;
import com.hongik.devtalk.global.apiPayload.code.GeneralErrorCode;
import com.hongik.devtalk.global.apiPayload.exception.GeneralException;
import com.hongik.devtalk.repository.review.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;

    /**
     * 후기 카드 전체 조회 (메인페이지 노출용)
     * isPublic = true AND isNote = true인 후기만 반환
     */
    public List<ReviewResponseDto> getAllReviews() {
        // isPublic = true AND isNote = true인 후기만 반환
        List<Review> reviews = reviewRepository.findAllOrderByDisplayOrder();
        
        return reviews.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * 후기 카드 순위 일괄 변경
     * @param orderedIds 원하는 최종 순서의 reviewId 배열(첫 번째가 1위)
     */
    @Transactional
    public ReorderResponseDto reorderReviews(List<String> orderedIds) {
        if (orderedIds == null || orderedIds.isEmpty()) {
            throw new GeneralException(GeneralErrorCode.INVALID_PARAMETER);
        }

        // 1. 홈 화면에 표시되어야 하는 모든 후기 조회 (isNote = true AND isPublic = true)
        List<Review> displayedReviews = reviewRepository.findByIsNoteTrueAndIsPublicTrue();
        
        // 2. 요청된 ID 개수와 실제 표시되어야 하는 후기 개수 비교
        if (orderedIds.size() != displayedReviews.size()) {
            log.error("표시되어야 하는 후기 개수({})와 요청된 ID 개수({})가 일치하지 않습니다.", 
                     displayedReviews.size(), orderedIds.size());
            throw new GeneralException(GeneralErrorCode.INCOMPLETE_REVIEW_ORDER);
        }
        
        // 3. 표시되어야 하는 후기의 ID 집합 생성
        Set<Long> displayedReviewIds = displayedReviews.stream()
                .map(Review::getId)
                .collect(Collectors.toSet());
        
        // 4. 요청된 ID들을 Long으로 변환하고 검증
        Set<Long> requestedReviewIds = new HashSet<>();
        for (String reviewIdStr : orderedIds) {
            Long reviewId;
            try {
                reviewId = Long.parseLong(reviewIdStr);
            } catch (NumberFormatException e) {
                log.error("잘못된 reviewId 형식: {}", reviewIdStr);
                throw new GeneralException(GeneralErrorCode.INVALID_PARAMETER);
            }
            requestedReviewIds.add(reviewId);
        }
        
        // 5. 두 집합이 동일한지 확인 (누락되거나 추가된 ID가 없는지)
        if (!displayedReviewIds.equals(requestedReviewIds)) {
            log.error("표시되어야 하는 후기 ID와 요청된 ID가 일치하지 않습니다. " +
                     "표시되어야 하는 ID: {}, 요청된 ID: {}", 
                     displayedReviewIds, requestedReviewIds);
            throw new GeneralException(GeneralErrorCode.INCOMPLETE_REVIEW_ORDER);
        }

        int updatedCount = 0;

        // 6. 각 reviewId에 대해 순서 업데이트
        for (int i = 0; i < orderedIds.size(); i++) {
            Long reviewId = Long.parseLong(orderedIds.get(i));
            
            Review review = reviewRepository.findById(reviewId)
                    .orElseThrow(() -> new GeneralException(GeneralErrorCode.REVIEW_NOT_FOUND));

            // isNote가 true이고 isPublic이 true인지 재확인 (이중 검증)
            if (!review.isNote() || !review.isPublic()) {
                log.error("홈 화면에 표시할 수 없는 후기가 포함되었습니다. reviewId: {}, isNote: {}, isPublic: {}", 
                         reviewId, review.isNote(), review.isPublic());
                throw new GeneralException(GeneralErrorCode.INCOMPLETE_REVIEW_ORDER);
            }

            // displayOrder를 1부터 시작하도록 설정 (i+1)
            review.updateDisplayOrder(i + 1);
            updatedCount++;
        }

        log.info("후기 순서 변경 완료: {} 건", updatedCount);

        return ReorderResponseDto.builder()
                .updatedCount(updatedCount)
                .build();
    }

    /**
     * 후기 카드 삭제
     * @param reviewId 후기 ID
     */
    @Transactional
    public DeleteReviewResponseDto deleteReview(String reviewId) {
        Long id;
        
        try {
            id = Long.parseLong(reviewId);
        } catch (NumberFormatException e) {
            log.error("잘못된 reviewId 형식: {}", reviewId);
            throw new GeneralException(GeneralErrorCode.INVALID_PARAMETER);
        }

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.REVIEW_NOT_FOUND));

        reviewRepository.delete(review);
        
        log.info("후기 삭제 완료: reviewId={}", reviewId);

        return DeleteReviewResponseDto.builder()
                .reviewId(reviewId)
                .build();
    }

    /**
     * Review 엔티티를 DTO로 변환
     */
    private ReviewResponseDto convertToDto(Review review) {
        Student student = review.getStudent();
        
        // 학과 처리 (복수 전공 가능)
        String department = null;
        if (student.getDepartments() != null && !student.getDepartments().isEmpty()) {
            department = student.getDepartments().stream()
                    .map(Enum::name)
                    .collect(Collectors.joining(", "));
        }
        if (student.getDepartmentEtc() != null && !student.getDepartmentEtc().isEmpty()) {
            department = department != null 
                    ? department + ", " + student.getDepartmentEtc() 
                    : student.getDepartmentEtc();
        }
        
        // 학년 처리
        String grade = null;
        if (student.getGrade() != null) {
            grade = student.getGrade() + "학년";
        } else if (student.getGradeEtc() != null) {
            grade = student.getGradeEtc();
        }
        
        return ReviewResponseDto.builder()
                .visible(review.isNote())
                .reviewId(String.valueOf(review.getId()))
                .rating(Integer.valueOf(review.getScore()))
                .title(null) // 엔티티에 없는 필드
                .content(review.getStrength())
                .department(department)
                .grade(grade)
                .nextTopic(review.getNextTopic())
                .order(review.getDisplayOrder())
                .createdAt(review.getCreatedAt())
                .build();
    }
}

