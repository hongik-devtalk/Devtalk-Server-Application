package com.hongik.devtalk.domain.userhome.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserHomeSeminarInfoResponseDTO {

    private Long seminarId;
    private int seminarNum;
    private String topic;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd (E) HH:mm", timezone = "Asia/Seoul")
    @Schema(type = "string", example = "2025.10.03 (금) 18:00")
    private LocalDateTime seminarDate;

    private String place;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd (E) HH:mm", timezone = "Asia/Seoul")
    @Schema(type = "string", example = "2025.10.01 (수) 14:00")
    private LocalDateTime startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd (E) HH:mm", timezone = "Asia/Seoul")
    @Schema(type = "string",example = "2025.10.02 (목) 21:00")
    private LocalDateTime endDate;

    // 해당 세미나의 세션 아이디 리스트
    private List<Long> sessionIds;

}
