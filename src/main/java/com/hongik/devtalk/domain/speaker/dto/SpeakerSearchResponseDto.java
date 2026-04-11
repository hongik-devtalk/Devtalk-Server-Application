package com.hongik.devtalk.domain.speaker.dto;


import com.hongik.devtalk.domain.Seminar;
import com.hongik.devtalk.domain.Session;
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

    private String subtitle;

    private String description;
    private String organization;
    private String profileUrl;
    private String history;

    //entity -> DTO 변환

    public static SpeakerSearchResponseDto from(Speaker speaker) {

        Session session = speaker.getSessions().isEmpty()
                ? null
                : speaker.getSessions().get(0);

        return SpeakerSearchResponseDto.builder()
                .speakerId(speaker.getId())
                .speakerName(speaker.getName())
                .subtitle(session != null ? session.getTitle() : null)
                .description(session != null ? session.getDescription() : null)
                .history(speaker.getHistory())
                .organization(speaker.getOrganization())
                .profileUrl(speaker.getProfileUrl())
                .build();
    }

}
