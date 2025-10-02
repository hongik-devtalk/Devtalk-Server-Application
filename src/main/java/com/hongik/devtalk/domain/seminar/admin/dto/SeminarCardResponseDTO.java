package com.hongik.devtalk.domain.seminar.admin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeminarCardResponseDTO {
    private Long seminarId;
    private Integer seminarNum;
    private String thumbnail;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd (E) HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime seminarDate;

    private String place;
    private String topic;
}