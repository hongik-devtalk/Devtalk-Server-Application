package com.hongik.devtalk.domain.seminar.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hongik.devtalk.domain.LiveFile;
import com.hongik.devtalk.domain.Seminar;
import com.hongik.devtalk.domain.SessionImage;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeminarDetailResponseDto {
    private Long seminarId;
    private int seminarNum;
    private String topic;
    private String ImageUrl;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime endDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime seminarDate;

    private String place;

    private String fileUrl;

}

