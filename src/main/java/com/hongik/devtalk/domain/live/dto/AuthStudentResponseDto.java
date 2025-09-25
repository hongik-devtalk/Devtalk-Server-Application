package com.hongik.devtalk.domain.live.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "신청자 인증 성공 응답 DTO")
public class AuthStudentResponseDto {
    @Schema(description = "학생 ID", example = "10")
    private Long studentId;

    @Schema(description = "세미나 ID", example = "5")
    private Long seminarId;

    @Schema(description = "서버 액세스 토큰", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNjE2NDk...")
    private String accessToken;

    @Schema(description = "서버 리프레시 토큰", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNjE2NDk...")
    private String refreshToken;
}
