package com.hongik.devtalk.domain.seminar.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class SeminarCardResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SeminarCardDTOList {
        private List<SeminarCardDTO> seminarList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SeminarCardDTO {

        private Long seminarId;
        private int seminarNum;
        private String seminarTopic;
        private LocalDateTime seminarDate;
        private String place;
        private String imageUrl;
    }
}