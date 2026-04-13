package com.hongik.devtalk.domain.seminar.dto;

import com.hongik.devtalk.domain.LiveFile;
import com.hongik.devtalk.domain.seminar.admin.dto.SeminarInfoResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
public class SeminarFileResponseDto {
    private Long seminarId;
    private List<FileInfo> fileInfos; // 단일 객체에서 리스트로 변경

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FileInfo {
        private String fileName;
        private String fileExtension;
        private Long fileSize;
        private String fileUrl;

        // 반환 타입을 SeminarFileResponseDto.FileInfo로 수정
        public static FileInfo from(LiveFile liveFile) {
            return FileInfo.builder()
                    .fileName(liveFile.getFileName())
                    .fileExtension(liveFile.getFileExtension())
                    .fileSize(liveFile.getFileSize())
                    .fileUrl(liveFile.getFileUrl())
                    .build();
        }
    }
}
