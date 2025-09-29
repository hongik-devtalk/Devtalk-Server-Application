package com.hongik.devtalk.domain.mainpage.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InquiryLinkResponseDto {
    
    private String linkId;
    private String url;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd (E) HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime updatedAt;

    private String updatedBy;
}
