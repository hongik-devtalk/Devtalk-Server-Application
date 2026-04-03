package com.hongik.devtalk.domain.seminar.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
public class AdminStatsResponseDTO {

    private AdminStatsResponseDTO() {
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "관리자 카드 조회수 통계 응답")
    public static class SeminarViewsResponseDTO {

        @Schema(description = "세미나 ID", example = "1")
        private Long seminarId;

        @Schema(description = "조회 시작일", example = "2026-03-01", type = "string", format = "date")
        private String from;

        @Schema(description = "조회 종료일", example = "2026-03-31", type = "string", format = "date")
        private String to;

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

        @Schema(description = "조회 일자", example = "2026-03-01", type = "string", format = "date")
        private String date;

        @Schema(description = "해당 일자의 카드 조회수", example = "12")
        private int viewCount;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "관리자 검색어 통계 응답")
    public static class SearchKeywordStatsResponseDTO {

        @Schema(description = "검색 대상 구분", example = "ALL", allowableValues = {"ALL", "SEMINAR", "SPEAKER"})
        private String target;

        @Schema(description = "조회 시작일", example = "2026-03-01", type = "string", format = "date")
        private String from;

        @Schema(description = "조회 종료일", example = "2026-03-31", type = "string", format = "date")
        private String to;

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
