package com.hongik.devtalk.domain.live.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "세미나 리뷰 작성 요청 DTO")
public class ReviewRequestDto {

    @Schema(description = "좋았던 점", example = "너무 유익하고 좋은 강의였습니다")
    private String strength;

    @Schema(description = "별점 (1~5)", example = "4")
    private byte score;
}
