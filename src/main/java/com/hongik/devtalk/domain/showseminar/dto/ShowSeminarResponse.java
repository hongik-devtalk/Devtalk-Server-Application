package com.hongik.devtalk.domain.showseminar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "홈화면 노출 세미나 응답 DTO")
public class ShowSeminarResponse {

    @Schema(description = "세미나 ID, 노출 세미나가 없을 경우 null")
    private Long seminarId;

    @Schema(description = "세미나 넘버, 노출 세미나가 없을 경우 null")
    private Integer seminarNum;

    @Schema(description = "신청 활성화 여부")
    private boolean applicantActivate;

    @Schema(description = "라이브 활성화 여부")
    private boolean liveActivate;
}
