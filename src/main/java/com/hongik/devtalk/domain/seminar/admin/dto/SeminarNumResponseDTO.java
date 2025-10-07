package com.hongik.devtalk.domain.seminar.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeminarNumResponseDTO {
    private Long seminarId;
    private Integer seminarNum;
}