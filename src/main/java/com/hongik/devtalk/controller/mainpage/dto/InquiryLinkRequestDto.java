package com.hongik.devtalk.controller.mainpage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InquiryLinkRequestDto {
    
    @NotBlank(message = "URL은 필수 입력 값입니다.")
    @Pattern(
        regexp = "^https?://.*", 
        message = "URL은 http:// 또는 https://로 시작해야 합니다."
    )
    private String url; // 카카오톡 채널/오픈채팅 URL (http/https 필수)
}
