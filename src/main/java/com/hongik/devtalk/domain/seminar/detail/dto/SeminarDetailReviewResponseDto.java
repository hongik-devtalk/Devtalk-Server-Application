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
    private int seminarNum;
    private byte score;
    private String strength;

    public static SeminarDetailReviewResponseDto from(Review review) {
        return SeminarDetailReviewResponseDto.builder()
                .reviewId(review.getId())
                .seminarNum(review.getSeminar().getSeminarNum())
                .score(review.getScore())
                .strength(review.getStrength())
                .build();
    }
}
