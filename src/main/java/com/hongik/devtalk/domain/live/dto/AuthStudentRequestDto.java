package com.hongik.devtalk.domain.live.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "신청자 인증 요청 DTO")
public class AuthStudentRequestDto {

    @Schema(description = "학생 학번", example = "C123456")
    private String studentNum;

    @Schema(description = "학생 이름", example = "홍길동")
    private String name;
}
