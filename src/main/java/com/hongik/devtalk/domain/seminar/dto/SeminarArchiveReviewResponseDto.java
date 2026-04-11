package com.hongik.devtalk.domain.seminar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "세미나 아카이브 리뷰 작성 성공 응답 DTO")
public class SeminarArchiveReviewResponseDto {

    @Schema(description = "생성된 리뷰 ID", example = "100")
    private Long reviewId;

    @Schema(description = "리뷰를 작성한 학생의 학번", example = "C123456")
    private String studentNum;


    @Schema(description = "리뷰가 달린 세미나 아이디", example = "1")
    private Long seminarId;

    @Schema(description = "리뷰가 달린 세미나 회차", example = "4")
    private int seminarNum;

    @Schema(description = "세미나 한줄 요약", example = "AI와 해킹에 관한 모든 것")
    private String description;

    @Schema(description = "별점 (1~5)", example = "4")
    private byte score;

    @Schema(description = "한줄평", example = "너무 유익하고 좋은 강의였습니다")
    private String totalContent;



}
