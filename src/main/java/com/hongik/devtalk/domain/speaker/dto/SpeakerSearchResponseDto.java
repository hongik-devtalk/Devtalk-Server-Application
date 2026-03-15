package com.hongik.devtalk.domain.speaker.dto;


import com.hongik.devtalk.domain.Speaker;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class SpeakerSearchResponseDto {

    private Long speakerId;
    private String speakerName;
    private String organization;
    private String profileUrl;

    //entity -> DTO 변환

    public static SpeakerSearchResponseDto from(Speaker speaker) {
        return SpeakerSearchResponseDto.builder()
                .speakerId(speaker.getId())
                .speakerName(speaker.getName())
                .organization(speaker.getOrganization())
                .profileUrl(speaker.getProfileUrl())
                .build();
    }

}
