package com.hongik.devtalk.domain.seminar.dto;


import com.hongik.devtalk.domain.Speaker;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeminarDetailSpeakerResponseDto {
    private Long SpeakerId;
    private String name;
    private String organization;
    private String history;
    private String profileUrl;

    // Speaker 엔티티를 DTO로 변환하는 정적 메서드
    public static SeminarDetailSpeakerResponseDto from(Speaker speaker) {
        return SeminarDetailSpeakerResponseDto.builder()
                .SpeakerId(speaker.getId())
                .name(speaker.getName())
                .organization(speaker.getOrganization())
                .history(speaker.getHistory())
                .profileUrl(speaker.getProfileUrl())
                .build();
    }
}
