package com.hongik.devtalk.domain.seminar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "세미나 아카이브 리뷰 작성 요청 DTO")

public class SeminarArchiveReviewRequestDto {

    @NotBlank
    @Size(max=100)
    @Schema(description = "한줄평", example = "너무 유익하고 좋은 강의였습니다")
    private String totalContent;

    @NotNull
    @Min(1)
    @Max(5)
    @Schema(description = "별점 (1~5)", example = "4")
    private byte score;

}
