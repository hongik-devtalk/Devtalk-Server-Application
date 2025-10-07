package com.hongik.devtalk.domain.mainpage.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"visible", "reviewId", "rating", "title", "content", "department", "grade", "nextTopic", "order", "createdAt"})
public class ReviewResponseDto {
    
    private Boolean visible; // 홈 노출 여부
    private String reviewId;
    private Integer rating; // 별점 (1~5)
    private String title; // 제목/작성자 식별(옵션)
    private String content; // 내용 요약
    private String department; // 학과
    private String grade; // 학년
    private String nextTopic; // 다음 듣고 싶은 주제
    private Integer order; // 홈 노출 순위 (낮을수록 상단)

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd (E) HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime createdAt; // 생성일시
}
