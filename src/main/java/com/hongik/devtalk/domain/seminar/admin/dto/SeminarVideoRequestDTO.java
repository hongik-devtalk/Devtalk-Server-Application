package com.hongik.devtalk.domain.seminar.admin.dto;


import com.hongik.devtalk.domain.Seminar;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeminarVideoRequestDTO {

    @NotNull
    @Schema(description = "세미나 회차 번호", example = "1")
    private Integer seminarNum;


    @Schema(description = "세미나 녹화영상 url")
    @Pattern(
            regexp = "^(https?://).+",
            message = "올바른 URL 형식이 아닙니다."
    )
    private String seminarVideoUrl;


}
