package com.hongik.devtalk.domain.seminar.detail.dto;


import com.hongik.devtalk.domain.Session;
import com.hongik.devtalk.domain.seminar.admin.dto.SeminarInfoResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeminarDetailSessionResponseDto {
    private Long sessionId;
    private String title;
    private String description;
    private List<String> tags;
    private SeminarDetailSpeakerResponseDto speaker;

    public static SeminarDetailSessionResponseDto from(Session session) {
        return SeminarDetailSessionResponseDto.builder()
                .sessionId(session.getId())
                .title(session.getTitle())
                .description(session.getDescription())
                .tags(session.getTags())
                .speaker(SeminarDetailSpeakerResponseDto.from(session.getSpeaker()))
                .build();
    }
}
