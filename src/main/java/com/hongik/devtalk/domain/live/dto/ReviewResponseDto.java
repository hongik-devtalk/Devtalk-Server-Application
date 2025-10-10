package com.hongik.devtalk.domain.live.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "세미나 리뷰 작성 성공 응답 DTO")
public class ReviewResponseDto {

    @Schema(description = "생성된 리뷰 ID", example = "100")
    private Long reviewId;

    @Schema(description = "리뷰를 작성한 학생의 학번", example = "C123456")
    private String studentNum;

    @Schema(description = "리뷰가 달린 세미나 아이디", example = "1")
    private Long seminarId;

    @Schema(description = "리뷰가 달린 세미나 회차", example = "4")
    private int seminarNum;
}
