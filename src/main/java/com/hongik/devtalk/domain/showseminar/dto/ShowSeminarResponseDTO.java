package com.hongik.devtalk.domain.showseminar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "����ȭ�� ���� ���̳� ���� DTO")
public class ShowSeminarResponseDTO {

    @Schema(description = "���̳� ID, ���� ���̳��� ���� ��� null")
    private Long seminarId;

    @Schema(description = "���̳� ȸ�� ��ȣ, ���� ���̳��� ���� ��� null")
    private Integer seminarNum;

    @Schema(description = "��û Ȱ��ȭ ����")
    private boolean applicantActivate;

    @Schema(description = "���̺� Ȱ��ȭ ����")
    private boolean liveActivate;

    @Schema(description = "���� ������ �̹��� URL")
    private String mainPosterImageUrl;

    @Schema(description = "���� ī�� ����")
    private MainCards mainCards;

    @Getter
    @Builder
    @AllArgsConstructor
    @Schema(description = "���� ī�� 3��")
    public static class MainCards {
        private SeminarRoundCard card1;
        private SessionCard card2;
        private SessionCard card3;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @Schema(description = "ī��1: ȸ�� ����")
    public static class SeminarRoundCard {
        private String imageUrl;
        private String seminarTitle;
        private String schedule;
        private String place;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @Schema(description = "ī��2/3: ���� ����")
    public static class SessionCard {
        private String imageUrl;
        private String seminarTitle;
        private String oneLineSummary;
        private String speakerName;
    }
}