package com.hongik.devtalk.domain.mainpage.dto;

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
public class FaqLinkRequestDto {
    
    @NotBlank(message = "URL은 필수 입력 값입니다.")
    @Pattern(
        regexp = "^https?://.*", 
        message = "URL은 http:// 또는 https://로 시작해야 합니다."
    )
    private String url; // FAQ 링크 URL (http/https 필수)
}

