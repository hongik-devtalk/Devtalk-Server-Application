package com.hongik.devtalk.controller.mainpage.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDto {
    
    private String reviewId;
    private Integer rating; // 별점 (1~5)
    private String title; // 제목/작성자 식별(옵션)
    private String content; // 내용 요약
    private Integer order; // 홈 노출 순위 (낮을수록 상단)
    private Boolean visible; // 홈 노출 여부

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd (E) HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime createdAt; // 생성일시
}
