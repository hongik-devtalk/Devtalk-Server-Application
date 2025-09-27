package com.hongik.devtalk.domain.mainpage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteReviewResponseDto {
    
    private String reviewId;
}
