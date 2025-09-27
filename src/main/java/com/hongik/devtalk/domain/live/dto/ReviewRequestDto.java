package com.hongik.devtalk.domain.live.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "세미나 리뷰 작성 요청 DTO")
public class ReviewRequestDto {

    @Schema(description = "좋았던 점", example = "너무 유익하고 좋은 강의였습니다")
    private String strength;

    @Schema(description = "아쉬웠던 점", example = "강의실이 너무 추웠어요")
    private String improvement;

    @Schema(description = "다음 세미나 희망 주제", example = "AI 활용에 대해서 강연 듣고 싶어요")
    private String nextTopic;

    @Schema(description = "별점 (1~5)", example = "4")
    private Integer score;
}