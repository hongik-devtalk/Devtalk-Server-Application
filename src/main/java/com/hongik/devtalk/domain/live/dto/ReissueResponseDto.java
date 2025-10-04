package com.hongik.devtalk.domain.live.dto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReissueResponseDto {
    private String accessToken;
    private String refreshToken;
}
