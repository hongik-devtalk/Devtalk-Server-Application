package com.hongik.devtalk.domain.live.dto;

import com.hongik.devtalk.domain.enums.AttendanceStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "출석 체크 성공 응답 DTO")
public class AttendanceResponseDto {

    @Schema(description = "세미나 라이브 스트리밍 URL", example = "https://hongik-devtalk.com/live")
    private String liveUrl;

    @Schema(description = "세미나 라이브 출석체크 상태", example = "PRESENT")
    private AttendanceStatus attendanceStatus;

    private LocalDateTime attendTime;
}