package com.hongik.devtalk.domain.seminar.dto;


import com.hongik.devtalk.domain.Seminar;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "세미나 url 조회 response DTO")

public class SeminarVideoResponseDTO {
    private Integer seminarNum;
    private Long seminarId;
    private String topic;
    private String seminarVideoUrl;

    //엔티티를 DTO로 전환
    public static SeminarVideoResponseDTO from(Seminar seminar)
    {
        return SeminarVideoResponseDTO.builder()
                .seminarNum(seminar.getSeminarNum())
                .seminarId(seminar.getId())
                .topic(seminar.getTopic())
                .seminarVideoUrl(seminar.getSeminarVideoUrl())
                .build();
    }
}
