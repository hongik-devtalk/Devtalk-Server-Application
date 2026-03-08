package com.hongik.devtalk.domain.seminar.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeminarUpdateRequestDTO {

    @NotNull
    @Schema(description = "세미나 회차 번호", example = "1")
    private Integer seminarNum;

    @NotNull
    @Schema(description = "세미나 일시", example = "2025-12-10T10:00:00")
    private LocalDateTime seminarDate;

    @NotBlank
    @Schema(description = "세미나 장소", example = "홍익대학교 R801호")
    private String place;

    @NotBlank
    @Schema(description = "세미나 주제", example = "AI와 미래 사회")
    private String topic;

    @NotBlank
    @Schema(description = "세미나 소제목", example= "인공지능이 바꾸는 산업과 우리의 삶")
    private String subtitle;

    @NotBlank
    @Schema(description = "세미나 설명", example= "본 세미나는 인공지능 기술이 산업, 사회, 일상에 어떤 변화를 가져오는지 살펴보고 앞으로 우리가 준비해야 할 방향에 대해 논의합니다.")
    private String description;

    @Schema(description = "세미나 신청 시작일", example = "2025-12-01T09:00:00")
    private LocalDateTime applyStartDate;

    @Schema(description = "세미나 신청 종료일", example = "2025-12-05T18:00:00")
    private LocalDateTime applyEndDate;

    @Schema(description = "세미나 온라인 링크(삭제 시 null로)", example = "https://youtube.com/123456")
    private String liveLink;

    @Valid @NotNull
    @Schema(description = "연사 정보 목록")
    private List<SpeakerUpdateRequest> speakers;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SpeakerUpdateRequest {

        @NotNull
        @Schema(description = "연사 ID", example = "10")
        private Long speakerId;

        @NotBlank
        @Schema(description = "연사 이름", example = "홍길동")
        private String name;

        @NotBlank
        @Schema(description = "연사 소속", example = "구글코리아")
        private String organization;

        @NotBlank
        @Schema(description = "연사 이력", example = "전 네이버 AI 연구원, 현 구글코리아 시니어 엔지니어")
        private String history;

        @Schema(
                description = "강연 태그 목록",
                example = "[\"#AI\", \"#BACKEND\"]"
        )
        private List<String> sessionTags;

        @NotBlank
        @Schema(description = "강연 제목", example = "AI 윤리와 책임 있는 기술")
        private String sessionTitle;

        @NotBlank
        @Schema(description = "강연 내용", example = "이번 강연에서는 인공지능 기술의 윤리적 과제와 사회적 영향에 대해 이야기합니다.")
        private String sessionContent;
    }
}
