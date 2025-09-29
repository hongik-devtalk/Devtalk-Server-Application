package com.hongik.devtalk.domain.mainpage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReorderRequestDto {
    
    private List<String> orderedIds; // 원하는 최종 순서의 reviewId 배열(첫 번째가 1위)
}
