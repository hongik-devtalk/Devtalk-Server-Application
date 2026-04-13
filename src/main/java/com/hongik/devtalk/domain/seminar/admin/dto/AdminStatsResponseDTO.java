package com.hongik.devtalk.domain.seminar.admin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class AdminStatsResponseDTO {

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";

    private AdminStatsResponseDTO() {
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "세미나 카드 조회수 통계 응답")
    public static class SeminarViewsResponseDTO {

        @Schema(description = "세미나 ID", example = "1")
        private Long seminarId;

        @JsonFormat(pattern = DATE_TIME_PATTERN)
        @Schema(description = "조회 시작일시", example = "2026-03-01T00:00:00", type = "string", format = "date-time")
        private LocalDateTime from;

        @JsonFormat(pattern = DATE_TIME_PATTERN)
        @Schema(description = "조회 종료일시", example = "2026-03-31T00:00:00", type = "string", format = "date-time")
        private LocalDateTime to;

        @Schema(description = "기간 내 총 조회수", example = "128")
        private int totalViewCount;

        @Schema(description = "일자별 조회수 목록")
        private List<SeminarViewPointDTO> viewPoints;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "일자별 세미나 카드 조회수")
    public static class SeminarViewPointDTO {

        @JsonFormat(pattern = DATE_TIME_PATTERN)
        @Schema(description = "조회 일시", example = "2026-03-01T00:00:00", type = "string", format = "date-time")
        private LocalDateTime date;

        @Schema(description = "해당 일자의 카드 조회수", example = "12")
        private int viewCount;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "검색어 통계 응답")
    public static class SearchKeywordStatsResponseDTO {

        @Schema(description = "검색 대상 구분", example = "ALL", allowableValues = {"ALL", "SEMINAR", "SPEAKER"})
        private String target;

        @JsonFormat(pattern = DATE_TIME_PATTERN)
        @Schema(description = "조회 시작일시", example = "2026-03-01T00:00:00", type = "string", format = "date-time")
        private LocalDateTime from;

        @JsonFormat(pattern = DATE_TIME_PATTERN)
        @Schema(description = "조회 종료일시", example = "2026-03-31T00:00:00", type = "string", format = "date-time")
        private LocalDateTime to;

        @Schema(description = "검색어 통계 목록")
        private List<TopKeywordDTO> keywords;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "검색어별 집계 정보")
    public static class TopKeywordDTO {

        @Schema(description = "검색어", example = "spring")
        private String keyword;

        @Schema(description = "기간 내 검색 횟수", example = "8")
        private int searchCount;
    }
}
