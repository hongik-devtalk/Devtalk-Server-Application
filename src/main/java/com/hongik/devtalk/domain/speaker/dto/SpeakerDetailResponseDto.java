package com.hongik.devtalk.domain.speaker.dto;

import com.hongik.devtalk.domain.Speaker;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class SpeakerDetailResponseDto {

    private Long speakerId;
    private String speakerName;
    private String organization;
    //이력
    private String history;
    private String profileUrl;
    private List<String> speakerTags;


    public static SpeakerDetailResponseDto from(Speaker speaker) {
        return SpeakerDetailResponseDto.builder()
                .speakerId(speaker.getId())
                .speakerName(speaker.getName())
                .organization(speaker.getOrganization())
                .history(speaker.getHistory())
                .profileUrl(speaker.getProfileUrl())
                .speakerTags(
                        speaker.getSpeakerTags() == null
                                ? List.of()
                                : speaker.getSpeakerTags().stream()
                                .map(speakerTag -> speakerTag.getTag().getTagText())
                                .toList()
                )
                .build();
    }

}
