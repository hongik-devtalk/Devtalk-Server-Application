package com.hongik.devtalk.domain.seminar.admin.dto;

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
public class SeminarInfoResponseDTO {
    private Long seminarId;
    private Integer seminarNum;
    private String topic;
    private LocalDateTime seminarDate;
    private String place;
    private LocalDateTime activeStartDate;
    private LocalDateTime activeEndDate;
    private LocalDateTime applyStartDate;
    private LocalDateTime applyEndDate;
    private String liveLink;

    private FileInfo thumbnail;
    private List<FileInfo> materials;
    private List<SpeakerResponse> speakers;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FileInfo {
        private String fileName;
        private String fileExtension;
        private Long fileSize;
        private String fileUrl;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SpeakerResponse {
        private Long speakerId;
        private String name;
        private String organization;
        private String history;
        private String sessionTitle;
        private String sessionContent;
        private FileInfo profile;
    }
}
