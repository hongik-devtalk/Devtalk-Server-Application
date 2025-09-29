package com.hongik.devtalk.domain.seminar.detail.dto;


import com.hongik.devtalk.domain.Session;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeminarDetailSessionResponseDto {
    private Long sessionId;
    private String title;
    private String description;
    private SeminarDetailSpeakerResponseDto speaker;

    public static SeminarDetailSessionResponseDto from(Session session) {
        return SeminarDetailSessionResponseDto.builder()
                .sessionId(session.getId())
                .title(session.getTitle())
                .description(session.getDescription())
                .speaker(SeminarDetailSpeakerResponseDto.from(session.getSpeaker()))
                .build();
    }
}
