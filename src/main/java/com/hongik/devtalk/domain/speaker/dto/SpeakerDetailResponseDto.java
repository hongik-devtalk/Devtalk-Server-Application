package com.hongik.devtalk.domain.speaker.dto;

import com.hongik.devtalk.domain.Session;
import com.hongik.devtalk.domain.Speaker;
import lombok.*;

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
    private Long seminarId;
    private int seminarNum;
    private String subtitle;
    private String description;


    public static SpeakerDetailResponseDto from(Speaker speaker, Session session) {
        return SpeakerDetailResponseDto.builder()
                .speakerId(speaker.getId())
                .speakerName(speaker.getName())
                .organization(speaker.getOrganization())
                .history(speaker.getHistory())
                .profileUrl(speaker.getProfileUrl())
                .seminarId(session.getSeminar().getId())
                .seminarNum(session.getSeminar().getSeminarNum())
                .subtitle(session.getTitle())
                .description(session.getDescription())
                .build();
    }

}
