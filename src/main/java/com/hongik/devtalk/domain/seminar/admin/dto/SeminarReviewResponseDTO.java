package com.hongik.devtalk.domain.seminar.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeminarReviewResponseDTO {
    private Long reviewId;
    private Integer score;
    private String department;
    private Integer grade;
    private String content;
    private String nextTopic;
    private Boolean isPublic;
}
