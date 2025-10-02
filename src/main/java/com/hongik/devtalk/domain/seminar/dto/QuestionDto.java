package com.hongik.devtalk.domain.seminar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "사전 질문 DTO")
public class QuestionDto {

    @Schema(description = "세션 아이디", example = "1")
    private Long sessionId;

    @Schema(description = "사전 질문 내용", example = "LLM의 미래가 궁금해요")
    private String content;
}
