package com.hongik.devtalk.domain.showseminar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "메인화면 노출 세미나 응답 DTO")
public class ShowSeminarResponseDTO {

    @Schema(description = "세미나 ID, 노출 세미나가 없을 경우 null")
    private Long seminarId;

    @Schema(description = "세미나 회차 번호, 노출 세미나가 없을 경우 null")
    private Integer seminarNum;

    @Schema(description = "신청 활성화 여부")
    private boolean applicantActivate;

    @Schema(description = "라이브 활성화 여부")
    private boolean liveActivate;

    @Schema(description = "메인 포스터 이미지 URL")
    private String mainPosterImageUrl;

    @Schema(description = "메인 카드 정보")
    private MainCards mainCards;

    @Getter
    @Builder
    @AllArgsConstructor
    @Schema(description = "메인 카드 3종")
    public static class MainCards {
        private SeminarRoundCard card1;
        private SessionCard card2;
        private SessionCard card3;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @Schema(description = "카드1: 회차 정보")
    public static class SeminarRoundCard {
        private String imageUrl;
        private String seminarTitle;
        private String schedule;
        private String place;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @Schema(description = "카드2/3: 세션 정보")
    public static class SessionCard {
        private String imageUrl;
        private String seminarTitle;
        private String oneLineSummary;
        private String speakerName;
    }
}