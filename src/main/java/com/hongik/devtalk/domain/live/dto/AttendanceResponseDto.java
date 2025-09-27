package com.hongik.devtalk.domain.live.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "출석 체크 성공 응답 DTO")
public class AttendanceResponseDto {

    @Schema(description = "세미나 라이브 스트리밍 URL", example = "https://hongik-devtalk.com/live")
    private String liveUrl;
}