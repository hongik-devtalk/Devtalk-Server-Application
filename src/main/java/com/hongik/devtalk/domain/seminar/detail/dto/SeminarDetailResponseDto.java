package com.hongik.devtalk.domain.seminar.detail.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hongik.devtalk.domain.LiveFile;
import com.hongik.devtalk.domain.Seminar;
import com.hongik.devtalk.domain.SessionImage;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeminarDetailResponseDto {
    private Long seminarId;
    private int seminarNum;
    private String topic;
    private String thumbnail;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime endDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime seminarDate;

    private String place;

    private List<String> fileUrls;

    public static SeminarDetailResponseDto from(Seminar seminar){

        //발표자료 url
        List<String> urls = seminar.getLiveFiles().stream()
                .map(LiveFile::getFileUrl)
                .toList();

        return SeminarDetailResponseDto.builder()
                .seminarId(seminar.getId())
                .seminarNum(seminar.getSeminarNum())
                .topic(seminar.getTopic())
                .thumbnail(seminar.getThumbnail())
                .startDate(seminar.getStartDate())
                .endDate(seminar.getEndDate())
                .place(seminar.getPlace())
                .fileUrls(urls)
                .build();
    }
}

