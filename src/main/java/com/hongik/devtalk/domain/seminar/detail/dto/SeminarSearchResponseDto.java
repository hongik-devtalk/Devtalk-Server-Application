package com.hongik.devtalk.domain.seminar.detail.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.hongik.devtalk.domain.Seminar;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeminarSearchResponseDto {

    private Long seminarId;

    private int seminarNum;


    private String topic;
    private String thumbnailUrl;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime endDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime seminarDate;

    private String place;


    //entity-> DTO로 변환 !

    public static SeminarSearchResponseDto from(Seminar seminar) {
        return SeminarSearchResponseDto.builder()
                .seminarId(seminar.getId())
                .seminarNum(seminar.getSeminarNum())
                .topic(seminar.getTopic())
                .thumbnailUrl(seminar.getThumbnailUrl())
                .startDate(seminar.getStartDate())
                .endDate(seminar.getEndDate())
                .seminarDate(seminar.getSeminarDate())
                .place(seminar.getPlace())
                .build();

    }

}
