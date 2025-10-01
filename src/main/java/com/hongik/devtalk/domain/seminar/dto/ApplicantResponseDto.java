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
@Schema(description = "세미나 신청 응답 DTO")
public class ApplicantResponseDto {
    @Schema(description = "신청한 학생의 ID", example = "10")
    private Long studentId;

    @Schema(description = "신청된 세미나의 ID", example = "5")
    private Long seminarId;

    @Schema(description = "생성된 신청 정보의 ID", example = "3")
    private Long applicantId;
}
