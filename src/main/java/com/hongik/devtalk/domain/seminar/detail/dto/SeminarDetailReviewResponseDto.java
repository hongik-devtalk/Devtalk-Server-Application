package com.hongik.devtalk.domain.seminar.detail.dto;

import com.hongik.devtalk.domain.Review;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeminarDetailReviewResponseDto {
    private Long reviewId;
    private byte score;
    private String strength;

    public static SeminarDetailReviewResponseDto from(Review review) {
        return SeminarDetailReviewResponseDto.builder()
                .reviewId(review.getId())
                .score(review.getScore())
                .strength(review.getStrength())
                .build();
    }
}
