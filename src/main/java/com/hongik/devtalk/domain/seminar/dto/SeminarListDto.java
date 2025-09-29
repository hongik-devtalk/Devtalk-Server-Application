package com.hongik.devtalk.domain.seminar.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class SeminarListDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SeminarResDtoList {
        List<SeminarResDto> seminarList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SeminarResDto {
        int seminarNum;
        String seminarTopic;
        LocalDateTime seminarDate;
        String place;
        String imageUrl;
    }


}
