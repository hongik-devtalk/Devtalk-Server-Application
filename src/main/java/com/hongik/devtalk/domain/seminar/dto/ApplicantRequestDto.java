package com.hongik.devtalk.domain.seminar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "세미나 신청 요청 DTO")
public class ApplicantRequestDto {
    @Schema(description = "학번", example = "C211182")
    private String studentNum;

    @Schema(description = "이름", example = "황신애")
    private String name;

    @Schema(description = "학년", example = "3")
    private Integer grade;

    @Schema(description = "전화번호", example = "010-1234-5678")
    private String phone;

    @Schema(description = "학과명", example = "컴퓨터공학과")
    private String departmentName;

    @Schema(description = "참여 방식 (ONLINE or OFFLINE)", example = "ONLINE")
    private String participationType;

    @Schema(description = "유입 경로 (ex. INSTAGRAM, EVERYTIME, HOMEPAGE)", example = "INSTAGRAM")
    private String inflowPath;

    @Schema(description = "사전 질문 목록")
    private List<QuestionDto> questions;


}
