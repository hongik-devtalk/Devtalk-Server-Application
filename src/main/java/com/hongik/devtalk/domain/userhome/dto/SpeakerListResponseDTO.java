package com.hongik.devtalk.domain.userhome.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SpeakerListResponseDTO {

    private List<SpeakerDTO> speakers;
}
