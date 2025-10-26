package com.hongik.devtalk.domain.showseminar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "홈화면 노출 세미나 등록 DTO")
public class ShowSeminarRequestDTO {
    @Schema(description = "노출할 SeminarNum 없으면 null")
    Integer seminarNum; // null 값 처리해야함

    @Schema(description = "신청 활성화 여부")
    boolean applicantActivate;

    @Schema(description = "라이브 활성화 여부")
    boolean liveActivate;
}

