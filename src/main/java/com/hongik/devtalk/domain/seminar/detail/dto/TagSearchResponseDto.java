package com.hongik.devtalk.domain.seminar.detail.dto;

import com.hongik.devtalk.domain.speaker.dto.SpeakerSearchResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TagSearchResponseDto {
    private List<SeminarSearchResponseDto> seminars;
    private List<SpeakerSearchResponseDto> speakers;
}