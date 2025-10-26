package com.hongik.devtalk.domain.seminar.admin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hongik.devtalk.domain.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

        public static FileInfo from(String name, String ext, Long size, String url) {
            return FileInfo.builder()
                    .fileName(name)
                    .fileExtension(ext)
                    .fileSize(size)
                    .fileUrl(url)
                    .build();
        }
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

    // DTO 변환
    public static SeminarInfoResponseDTO from(Seminar seminar, Live live,
                                              List<LiveFile> materials,
                                              List<Session> sessions) {
        return SeminarInfoResponseDTO.builder()
                .seminarId(seminar.getId())
                .seminarNum(seminar.getSeminarNum())
                .topic(seminar.getTopic())
                .seminarDate(seminar.getSeminarDate())
                .place(seminar.getPlace())
                .applyStartDate(seminar.getStartDate())
                .applyEndDate(seminar.getEndDate())
                .liveLink(live != null ? live.getLiveUrl() : null)
                .thumbnail(FileInfo.from(
                        seminar.getThumbnailFileName(),
                        seminar.getThumbnailFileExtension(),
                        seminar.getThumbnailFileSize(),
                        seminar.getThumbnailUrl()
                ))
                .materials(materials.stream()
                        .map(m -> FileInfo.from(m.getFileName(), m.getFileExtension(), m.getFileSize(), m.getFileUrl()))
                        .collect(Collectors.toList()))
                .speakers(sessions.stream()
                        .map(SeminarInfoResponseDTO::toSpeakerResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    // Speaker + Session → SpeakerResponse 변환
    private static SpeakerResponse toSpeakerResponse(Session session) {
        Speaker speaker = session.getSpeaker();
        return SpeakerResponse.builder()
                .speakerId(speaker.getId())
                .name(speaker.getName())
                .organization(speaker.getOrganization())
                .history(speaker.getHistory())
                .sessionTitle(session.getTitle())
                .sessionContent(session.getDescription())
                .profile(FileInfo.from(
                        speaker.getProfileFileName(),
                        speaker.getProfileFileExtension(),
                        speaker.getProfileFileSize(),
                        speaker.getProfileUrl()
                ))
                .build();
    }
}
